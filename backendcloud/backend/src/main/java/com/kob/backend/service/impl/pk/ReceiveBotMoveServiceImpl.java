package com.kob.backend.service.impl.pk;

import com.kob.backend.consumer.WebSocketServer;
import com.kob.backend.consumer.utils.Game;
import com.kob.backend.service.pk.ReceiveBotMoveService;
import org.springframework.stereotype.Service;

/**
 * @author xzt
 * @version 1.0
 */
@Service
public class ReceiveBotMoveServiceImpl implements ReceiveBotMoveService {
    @Override
    public String receiveBotMove(Integer userId, Integer direction) {
        System.out.println("receive bot move: " + userId + " " + direction);

        // 给对应Bot的玩家设置移动方向
        if(WebSocketServer.users.get(userId) != null) {
            Game game = WebSocketServer.users.get(userId).game;
            if(game != null) {
                if (game.getPlayerA().getId().equals(userId)) {  // 如果是蛇A
                    game.setNextStepA(direction);
                } else if (game.getPlayerB().getId().equals(userId)) {  // 如果是蛇B
                    game.setNextStepB(direction);
                }
            }
        }
        return "receive Bot move success";
    }
}
