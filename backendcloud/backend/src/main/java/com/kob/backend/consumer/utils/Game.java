package com.kob.backend.consumer.utils;

import com.alibaba.fastjson2.JSONObject;
import com.kob.backend.consumer.WebSocketServer;
import com.kob.backend.mapper.RecordMapper;
import com.kob.backend.pojo.Bot;
import com.kob.backend.pojo.Record;
import com.kob.backend.pojo.User;
import com.kob.backend.service.user.bot.UpdateService;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author xzt
 * @version 1.0
 * 用来生成地图
 */
public class Game extends Thread {
    private final Integer rows;
    private final Integer cols;
    private final Integer inner_walls_count;
    private final int[][] g;  // 0表示空地，1表示障碍物
    private final static int[] dx = {-1, 0, 1, 0}, dy = {0, 1, 0, -1};
    private final Player playerA, playerB;  // 记录两个蛇的位置，playerA是左下角，playerB是右上角

    private Integer nextStepA = null;  // 记录两个玩家的下一步操作
    private Integer nextStepB = null;

    private ReentrantLock lock = new ReentrantLock();  // 定义一个锁，用来保证nextStepA和nextStepB的读写一致性，因为会涉及到两个线程的读写操作
    private String status = "playing";  // 游戏状态， playing ---> finished
    private String loser = "";  // all 平局，A：A输，B：B输

    private final static String addBotUrl = "http://127.0.0.1:3002/bot/add/";

    public Game(Integer rows, Integer cols, Integer inner_walls_count, Integer idA, Bot botA, Integer idB, Bot botB) {
        this.rows = rows;
        this.cols = cols;
        this.inner_walls_count = inner_walls_count;
        this.g = new int[rows][cols];

        Integer botIdA = -1, botIdB = -1;
        String botCodeA = "", botCodeB = "";  // 因为如果前面传过来的botId是-1，则BotA是null，所以需要加判断。
        if(botA != null) {
            botIdA = botA.getId();
            botCodeA = botA.getContent();
        }
        if(botB != null) {
            botIdB = botB.getId();
            botCodeB = botB.getContent();
        }

        playerA = new Player(idA, botIdA, botCodeA, this.rows - 2, 1, new ArrayList<>());
        playerB = new Player(idB, botIdB, botCodeB, 1, this.cols - 2, new ArrayList<>());
    }

    public Player getPlayerA() {
        return playerA;
    }
    public Player getPlayerB() {
        return playerB;
    }

    public void setNextStepA(Integer nextStepA) {
        lock.lock();
        try {
            this.nextStepA = nextStepA;
        } finally {
            lock.unlock();
        }

    }

    public void setNextStepB(Integer nextStepB) {
        lock.lock();
        try {
            this.nextStepB = nextStepB;
        } finally {
            lock.unlock();
        }

    }

    // 返回地图
    public int[][] getG() {
        return g;
    }

    /**
     * 判断地图的连通性
     * @param sx 起始横坐标
     * @param sy 起始纵坐标
     * @param tx 目标横坐标
     * @param ty 目标纵坐标
     * @return true表示连通，false表示不连通
     */
    private boolean check_connectivity(int sx, int sy, int tx, int ty) {
        if(sx == tx && sy == ty) return true;
        g[sx][sy] = 1;
        for (int i = 0; i < 4; i++) {
            int x = sx + dx[i];
            int y = sy + dy[i];
            if(x >= 0 && x < this.rows && y >= 0 && y < this.cols && g[x][y] == 0) {
                if(check_connectivity(x, y, tx, ty)) {
                    g[sx][sy] = 0;
                    return true;
                }
            }
        }
        g[sx][sy] = 0;
        return false;
    }

    /**
     * 画地图
     */
    private boolean draw() {
        // 将地图初始化
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                g[i][j] = 0;
            }
        }

        // 给四周加墙
        for (int r = 0; r < this.rows; r++) {
            g[r][0] = g[r][this.cols - 1] = 1;
        }
        for (int c = 0; c < this.cols; c++) {
            g[0][c] = g[this.rows - 1][c] = 1;
        }

        // 创建随机障碍物
        Random random = new Random();
        for (int i = 0; i < this.inner_walls_count / 2; i++) {
            for (int j = 0; j < 1000; j++) {
                int r = random.nextInt(this.rows);  // 返回0~this.rows-1中的一个随机值
                int c = random.nextInt(this.cols);
                if(g[r][c] == 1 || g[this.rows - 1 - r][this.cols - 1 - c] == 1) continue;
                if(r == this.rows - 2 && c == 1 || r == 1 && c == this.cols - 2) continue;
                g[r][c] = g[this.rows - 1 - r][this.cols - 1 - c] = 1;
                break;
            }
        }

        // 判断连通性
        return check_connectivity(this.rows - 2, 1, 1, this.cols - 2);
    }

    /**
     * 创建地图
     */
    public void createMap() {
        for (int i = 0; i < 1000; i++) {
            if(draw()) break;
        }
    }

    /**
     * 获取当前的局面，将当前的局面信息编码成字符串：
     * "地图#自己的起始横坐标#自己的起始纵坐标#(我的操作)#对手的起始横坐标#对手的起始纵坐标#(对手的操作)"
     * @param player
     * @return
     */
    private String getInput(Player player) {
        Player me, you;
        if(playerA.getId().equals(player.getId())) {
            me = playerA;
            you = playerB;
        } else {
            me = playerB;
            you = playerA;
        }

        return getMapString() + "#" +
                me.getSx() + "#" +
                me.getSy() + "#(" +
                me.getStepsString() + ")#" +
                you.getSx() + "#" +
                you.getSy() + "#(" +
                you.getStepsString() + ")";
    }

    /**
     * 判断用户是否亲自上阵，如果不是，则需要向botrunningsystem微服务发送请求。
     * @param player 判断的玩家
     */
    private void sendBotCode(Player player) {
        if(player.getBotId().equals(-1)) return ; // 人亲自出马，不需要执行代码
        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("user_id", player.getId().toString());
        data.add("bot_code", player.getBotCode());
        data.add("input", getInput(player));

        WebSocketServer.restTemplate.postForObject(addBotUrl, data, String.class);
    }

    /**
     * 等待两个玩家的下一步操作
     * @return
     */
    private boolean nextStep() {
        try {  // 在接收下一步时先睡200ms，防止在前端渲染的过程中玩家的输入进行遗漏
            Thread.sleep(200);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        sendBotCode(playerA);
        sendBotCode(playerB);

        for (int i = 0; i < 50; i++) {
            try {
                Thread.sleep(100);  // 先睡100ms秒，短暂的释放锁，给玩家输入的时间
                lock.lock();  // 1s过后，拿住锁，然后在判断两名玩家是否已经输入
                try {
                    if(nextStepA != null && nextStepB != null) {
                        playerA.getSteps().add(nextStepA);
                        playerB.getSteps().add(nextStepB);
                        return true;
                    }
                } finally {
                    lock.unlock();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 判断蛇A的头节点是否合法
     * @param cellsA
     * @param cellsB
     * @return
     */
    private boolean check_valid(List<Cell> cellsA, List<Cell> cellsB) {
        int n = cellsA.size();
        Cell cell = cellsA.get(n - 1);  // 新的蛇头
        if(g[cell.x][cell.y] == 1) return false; // 如果蛇的最后一步是障碍物

        for (int i = 0; i < n - 1; i++) {  // 判断和蛇A有没有碰撞
            if(cellsA.get(i).x == cell.x && cellsA.get(i).y == cell.y)
                return false;
        }

        for (int i = 0; i < n - 1; i++) {  // 判断和蛇B有没有碰撞
            if(cellsB.get(i).x == cell.x && cellsB.get(i).y == cell.y)
                return false;
        }
        return true;
    }

    /**
     * 判断两名玩家下一步操作是否合法
     */
    private void judge() {
        // 取出两条蛇的所有节点
        List<Cell> cellsA = playerA.getCells();
        List<Cell> cellsB = playerB.getCells();

        boolean validA = check_valid(cellsA, cellsB);
        boolean validB = check_valid(cellsB, cellsA);
        // 如果两个玩家的操作存在不合法。
        if(!validA || !validB) {
            status = "finished";
            if(!validA && !validB) loser = "all";
            else if(!validA) loser = "A";
            else loser = "B";
        }
    }

    /**
     * 向所有玩家发送信息
     */
    private void sendAllMessage(String message) {
        if(WebSocketServer.users.get(playerA.getId()) != null)
            WebSocketServer.users.get(playerA.getId()).sendMessage(message); // 这里的sendMessage是WebSocketServer里的
        if(WebSocketServer.users.get(playerB.getId()) != null)
            WebSocketServer.users.get(playerB.getId()).sendMessage(message);
    }

    /**
     * 像两个client传送移动信息，这里是两名玩家的移动信息
     */
    private void sendMove() {
        lock.lock();
        try {
            JSONObject resp = new JSONObject();
            resp.put("event", "move");
            resp.put("a_direction", nextStepA);
            resp.put("b_direction", nextStepB);
            sendAllMessage(resp.toJSONString());  // 将移动信息返回给前端
            nextStepA = nextStepB = null;  // 将两个玩家的下一步清空
        } finally {
            lock.unlock();
        }
    }

    /**
     * 将map转为String类型
     * @return
     */
    private String getMapString() {
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                res.append(g[i][j]);
            }
        }
        return res.toString();
    }

    /**
     * 对局结束后，更新玩家的对战积分
     * @param player 玩家
     * @param rating 对战积分
     */
    private void updateUserRating(Player player, Integer rating) {
        User user = WebSocketServer.userMapper.selectById(player.getId());
        user.setRating(rating);
        WebSocketServer.userMapper.updateById(user);
    }

    /**
     * 将对战记录保存到数据库中。在对战结束后先更新两个玩家的对战积分。
     */
    private void saveToDatabase() {
        Integer ratingA = WebSocketServer.userMapper.selectById(playerA.getId()).getRating();
        Integer ratingB = WebSocketServer.userMapper.selectById(playerB.getId()).getRating();

        // 赢一局加5分，输一局减2分。
        if ("A".equals(loser)) {
            ratingA -= 2;
            ratingB += 5;
        } else if ("B".equals(loser)){
            ratingA += 5;
            ratingB -= 2;
        }

        updateUserRating(playerA, ratingA);
        updateUserRating(playerB, ratingB);

        Record record = new Record(
                null,
                playerA.getId(),
                playerA.getSx(),
                playerA.getSy(),
                playerB.getId(),
                playerB.getSx(),
                playerB.getSy(),
                playerA.getStepsString(),
                playerB.getStepsString(),
                getMapString(),
                loser,
                new Date()
        );
        // 保存在数据库
        WebSocketServer.recordMapper.insert(record);
    }

    /**
     * 像两个client公布结果, 这里是游戏结果，
     */
    private void sendResult() {
        JSONObject resp = new JSONObject();
        resp.put("event", "result");
        resp.put("loser", loser);
        saveToDatabase();
        sendAllMessage(resp.toJSONString());
    }

    /**
     * 新线程的入口函数
     */
    @Override
    public void run() {
        // 这里先让线程睡2s，因为匹配成功后前端要等2s才能跳转到对战页面，如果这里不睡2s，直接开始获取下一步操作，则两个AI在2s内可以做出多步操作，不能保证同步了。
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        for (int i = 0; i < 1000; i++) {
            if (nextStep()) {  // 是否获取两条蛇的下一步操作
                judge();
                if ("playing".equals(status)) {  // 两名玩家的下一步操作合法
                    sendMove();
                } else {
                    sendResult();
                    break;
                }
            } else {
                status = "finished";
                lock.lock();  // 涉及nextStepA和nextStepB的读取操作，需要加锁
                try {
                    if (nextStepA == null && nextStepB == null)
                        loser = "all";
                    else if (nextStepA == null)
                        loser = "A";
                    else
                        loser = "B";
                } finally {
                    lock.unlock();
                }
                sendResult();
                break;
            }
        }
    }
}
