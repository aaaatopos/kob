package com.kob.backend.controller.pk;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.lang.model.element.NestingKind;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xzt
 * @version 1.0
 */
@RestController
@RequestMapping("/pk")
public class BotInfoController {

    @GetMapping("/getinfo")
    public Map<String, String> getBotInfo(){
        Map<String,String> bot = new HashMap<>();
        bot.put("name", "bot");
        bot.put("rating", "1500");
        return bot;
    }
}
