package com.kob.botrunningsystem.service.impl;

import com.kob.botrunningsystem.service.BotRunningService;
import com.kob.botrunningsystem.service.impl.utils.BotPool;
import org.springframework.stereotype.Service;

/**
 * @author xzt
 * @version 1.0
 */
@Service
public class BotRunningServiceImpl implements BotRunningService {

    public final static BotPool botPool = new BotPool();

    /**
     * 添加一个Bot
     * @param userId 用户id
     * @param botCode 需要执行的Bot代码
     * @param input 输入，当前地图的信息，两个玩家的位置，走过的格子
     * @return
     */
    @Override
    public String addBot(Integer userId, String botCode, String input) {
        System.out.println("add bot: " + userId + " " + botCode + " " + input);
        botPool.addBot(userId, botCode, input);
        return null;
    }
}
