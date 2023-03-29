package com.kob.botrunningsystem.service.impl.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xzt
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Bot {
    private Integer userId;
    private String botCode;
    private String input;
}
