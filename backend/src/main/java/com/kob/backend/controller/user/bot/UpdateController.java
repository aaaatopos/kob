package com.kob.backend.controller.user.bot;

import com.kob.backend.service.user.bot.UpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author xzt
 * @version 1.0
 */
@RestController
@RequestMapping("/user/bot")
public class UpdateController {
    @Autowired
    private UpdateService updateService;

    @PostMapping("/update")
    public Map<String, String> update(@RequestParam Map<String, String> data){
        return updateService.update(data);
    }
}
