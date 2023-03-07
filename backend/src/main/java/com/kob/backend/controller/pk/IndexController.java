package com.kob.backend.controller.pk;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xzt
 * @version 1.0
 */
@Controller
@RequestMapping("/pk")
public class IndexController {

    @RequestMapping("/index/")
    public String index() {
        return "pk/index.html";  //返回tmeplates/pk/index.html
    }
}
