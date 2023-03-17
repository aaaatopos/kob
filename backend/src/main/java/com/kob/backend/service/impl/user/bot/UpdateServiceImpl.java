package com.kob.backend.service.impl.user.bot;

import com.kob.backend.mapper.BotMapper;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.Bot;
import com.kob.backend.pojo.User;
import com.kob.backend.service.impl.utils.UserDetailsImpl;
import com.kob.backend.service.user.bot.UpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.xml.crypto.Data;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xzt
 * @version 1.0
 */
@Service
public class UpdateServiceImpl implements UpdateService {

    @Autowired
    private BotMapper botMapper;

    /**
     * 修改Bot信息
     * @param data
     * @return
     */
    @Override
    public Map<String, String> update(Map<String, String> data) {
        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authenticationToken.getPrincipal();
        User user = userDetails.getUser();

        Map<String, String> map = new HashMap<>();

        Integer bot_id = Integer.parseInt(data.get("bot_id"));
        String title = data.get("title");
        String description = data.get("description");
        String content = data.get("content");

        if(title == null || title.length() == 0) {
            map.put("error_msg", "标题不能为空");
            return map;
        }
        if(title.length() > 100) {
            map.put("error_msg", "标题长度不能大于100");
            return map;
        }
        if(description == null && description.length() == 0) {
            description = "这个用户很懒，什么也没留下";
        }
        if(description.length() > 300) {
            map.put("error_msg", "Bot描述的chang不能大于300");
            return map;
        }
        if(content == null || content.length() == 0) {
            map.put("error_msg", "Bot代码不能为空");
            return map;
        }
        if(content.length() > 10000) {
            map.put("error_msg", "Bot代码长度不能大于10000");
            return map;
        }

        Bot bot = botMapper.selectById(bot_id);

        if(bot == null) {
            map.put("error_msg", "Bot不存在或已被删除");
            return map;
        }
        if(!bot.getUserId().equals(user.getId())) {
            map.put("error_msg", "没有权限修改");
            return map;
        }
        Bot newBot = new Bot(
                bot.getId(),
                user.getId(),
                title,
                description,
                content,
                bot.getRating(),
                bot.getCreateTime(),
                new Date()
        );
        botMapper.updateById(newBot);
        map.put("error_msg", "success");
        return map;
    }
}
