package com.kob.botrunningsystem.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * 实现一个AIBot
 */
public class Bot implements com.kob.botrunningsystem.utils.BotInterface {
    static class Cell {
        public int x, y;
        public Cell(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    /**
     * 检查当前回合，蛇的身体是否会增长.
     * 前十回合会增长，后面每三回合增长一次。
     * @param step 当前回合数
     * @return
     */
    private boolean check_tail_increasing(int step) {
        if(step <= 10) return true;
        return step % 3 == 1;
    }

    /**
     * 返回蛇的身体，由若干个Cell组成
     * @return
     */
    public List<Cell> getCells(int sx, int sy, String steps) {
        steps = steps.substring(1, steps.length() - 1);  //去除()
        List<Cell> res = new ArrayList<>();
        int[] dx = {-1, 0, 1, 0}, dy = {0, 1, 0, -1};
        int x = sx, y = sy;

        int step = 0;
        res.add(new Cell(x, y));
        for (int i = 0; i < steps.length(); i ++ ) {  // 遍历每一回合蛇的移动方向
            int d = steps.charAt(i) - '0';
            x += dx[d];
            y += dy[d];

            res.add(new Cell(x, y));
            if (!check_tail_increasing(++ step)) { // 如果蛇尾不增加,
                res.remove(0); // 需要将蛇尾删掉
            }
        }
        return res;
    }

    @Override
    public Integer nextMove(String input) {
        String[] strs = input.split("#");
        int[][] g = new int[13][14];
        for(int i = 0, k = 0; i < 13; i ++) {
            for (int j = 0; j < 14; j ++, k ++) {
                if(strs[0].charAt(k) == '1')
                    g[i][j] = 1;
            }
        }
        int aSx = Integer.parseInt(strs[1]), aSy = Integer.parseInt(strs[2]);
        int bSx = Integer.parseInt(strs[4]), bSy = Integer.parseInt(strs[5]);

        List<Cell> aCells = getCells(aSx, aSy, strs[3]);
        List<Cell> bCells = getCells(bSx, bSy, strs[6]);
        for(Cell c : aCells) g[c.x][c.y] = 1;
        for(Cell c : bCells) g[c.x][c.y] = 1;
        int[] dx = {-1, 0, 1, 0}, dy = {0, 1, 0, -1};
        // 判断当前蛇头四个方向哪个方向可以走。
        for (int i = 0; i < 4; i++) {
            int x = aCells.get(aCells.size() - 1).x + dx[i];
            int y = aCells.get(aCells.size() - 1).y + dy[i];
            if(x >= 0 && x < 13 && y >= 0 && y < 14 && g[x][y] == 0) {
                return i;
            }
        }
        return 0;
    }
}
