package com.kob.backend.service.user.account;

import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author xzt
 * @version 1.0
 */
public interface RegisterService {
    public Map<String, String> register(String username, String password, String confirmPassword);
}
