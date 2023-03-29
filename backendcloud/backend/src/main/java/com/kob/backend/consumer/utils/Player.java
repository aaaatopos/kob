package com.kob.backend.consumer.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xzt
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Player {
    private Integer id;
    private Integer botId; // -1表示亲自出马，否则表示用AI打
    private String botCode;
    private Integer sx;
    private Integer sy;

    private List<Integer> steps;

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
    public List<Cell> getCells() {
        List<Cell> res = new ArrayList<>();
        int[] dx = {-1, 0, 1, 0}, dy = {0, 1, 0, -1};
        int x = sx, y = sy;

        int step = 0;
        res.add(new Cell(x, y));
        for (int d : steps) {  // 遍历每一回合蛇的移动方向
            x += dx[d];
            y += dy[d];
            res.add(new Cell(x, y));
            if (!check_tail_increasing(++ step)) { // 如果蛇尾不增加,
                res.remove(0); // 需要将蛇尾删掉
            }
        }
        return res;
    }

    /**
     * 将steps转为String保存
     * @return
     */
    public String getStepsString() {
        StringBuilder res = new StringBuilder();
        for(int d: steps) {
            res.append(d);
        }
        return res.toString();
    }
}
