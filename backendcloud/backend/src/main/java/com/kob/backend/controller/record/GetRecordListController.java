package com.kob.backend.controller.record;

import com.alibaba.fastjson2.JSONObject;
import com.kob.backend.service.record.GetRecordListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author xzt
 * @version 1.0
 */
@RestController
@RequestMapping("/record")
public class GetRecordListController {
    @Autowired
    private GetRecordListService getRecordListService;

    @GetMapping("/getlist")
    JSONObject getList(@RequestParam Map<String, String> data) {
        Integer pageNum = Integer.parseInt(data.get("pageNum"));
        Integer pageSize = Integer.parseInt(data.get("pageSize"));

        return getRecordListService.getList(pageNum, pageSize);
    }
}
