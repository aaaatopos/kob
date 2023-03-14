package com.kob.backend.service.impl.user.account;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.User;
import com.kob.backend.service.user.account.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xzt
 * @version 1.0
 */
@Service
public class RegisterServiceImpl implements RegisterService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Map<String, String> register(String username, String password, String confirmPassword) {
        Map<String, String> map = new HashMap<>();
        if(username == null) {
            map.put("error_msg", "用户名不能为空");
            return map;
        }
        if(password == null || confirmPassword == null) {
            map.put("error_msg", "密码不能为空");
            return map;
        }
        username = username.trim();
        if(username.length() == 0) {
            map.put("error_msg", "用户名不能为空");
            return map;
        }
        if(password.length() == 0 || confirmPassword.length() == 0) {
            map.put("error_msg", "密码不能为空");
            return map;
        }
        if(username.length() > 100) {
            map.put("error_msg", "用户名长度不能大于100");
            return map;
        }
        if(password.length() > 100 || confirmPassword.length() > 100) {
            map.put("error_msg", "密码长度不能大于100");
            return map;
        }
        if(!password.equals(confirmPassword)){
            map.put("error_msg", "两次输入的密码不一致");
            return map;
        }

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        List<User> users = userMapper.selectList(queryWrapper);
        if(!users.isEmpty()) {
            map.put("error_msg", "用户名已存在");
            return map;
        }
        String encodedPassword = passwordEncoder.encode(password);  // 密码加密
        String photo = "https://cdn.acwing.com/media/user/profile/photo/72309_lg_e1afa7c633.jpg";
        User user = new User(null, username, encodedPassword, photo);

        userMapper.insert(user);
        map.put("error_msg", "success");
        return map;
    }
}
