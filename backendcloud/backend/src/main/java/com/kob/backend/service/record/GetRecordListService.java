package com.kob.backend.service.record;

import com.alibaba.fastjson2.JSONObject;

/**
 * @author xzt
 * @version 1.0
 */
public interface GetRecordListService {
    JSONObject getList(Integer pageNum, Integer pageSize);
}
