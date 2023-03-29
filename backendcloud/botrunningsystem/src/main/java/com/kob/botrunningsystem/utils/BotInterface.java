package com.kob.botrunningsystem.utils;

/**
 * @author xzt
 * @version 1.0
 */
public interface BotInterface {
    /**
     * 获取用户下一步的移动方向
     * @param input 对局情况
     * @return
     */
    Integer nextMove(String input);
}
