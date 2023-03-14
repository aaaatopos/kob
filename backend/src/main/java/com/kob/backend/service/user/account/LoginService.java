package com.kob.backend.service.user.account;

import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author xzt
 * @version 1.0
 */
public interface LoginService {
    public Map<String, String> getToken(String username, String password);
}
