package com.kob.backend.controller.pk;

import com.kob.backend.service.pk.StartGameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * @author xzt
 * @version 1.0
 */
@RestController
@RequestMapping("/pk")
public class StartGameController {

    @Autowired
    private StartGameService startGameService;

    @PostMapping("/game/start/")
    public String startGame(@RequestParam MultiValueMap<String, String> data) {
        int aId = Integer.parseInt(Objects.requireNonNull(data.getFirst("a_id")));
        int aBotId = Integer.parseInt(Objects.requireNonNull(data.getFirst("a_bot_id")));
        int bId = Integer.parseInt(Objects.requireNonNull(data.getFirst("b_id")));
        int bBotId = Integer.parseInt(Objects.requireNonNull(data.getFirst("b_bot_id")));
        return startGameService.startGame(aId, aBotId, bId, bBotId);
    }
}
