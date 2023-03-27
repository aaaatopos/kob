package com.kob.matchingsystem.service;

/**
 * @author xzt
 * @version 1.0
 */
public interface MatchingService {
    public String addPlayer(Integer userId, Integer rating);
    public String removePlayer(Integer userId);
}
