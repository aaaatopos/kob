package com.kob.backend.controller.user.bot;

import com.kob.backend.service.user.bot.RemoveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author xzt
 * @version 1.0
 */
@RestController
@RequestMapping("/user/bot")
public class RemoveController {
    @Autowired
    private RemoveService removeService;

    @PostMapping("/remove")
    public Map<String, String> remove(@RequestParam Map<String, String> data) {
        return removeService.remove(data);
    }
}
