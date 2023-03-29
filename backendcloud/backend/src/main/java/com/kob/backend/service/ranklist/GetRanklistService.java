package com.kob.backend.service.ranklist;

import com.alibaba.fastjson2.JSONObject;

/**
 * @author xzt
 * @version 1.0
 */
public interface GetRanklistService {
    JSONObject getList(Integer pageNum, Integer pageSize);
}
