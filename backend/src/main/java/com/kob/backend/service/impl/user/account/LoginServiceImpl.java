package com.kob.backend.service.impl.user.account;

import com.kob.backend.pojo.User;
import com.kob.backend.service.impl.utils.UserDetailsImpl;
import com.kob.backend.service.user.account.LoginService;
import com.kob.backend.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.security.PrivateKey;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xzt
 * @version 1.0
 */
@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    private AuthenticationManager authenticationManager;  // 用来验证用户是否能登录成功

    /**
     * 实现用户的登录
     * @param username 用户名
     * @param password 密码
     * @return
     */
    @Override
    public Map<String, String> getToken(String username, String password) {
        // 将用户名密码进行封装，将密码加密
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username, password);
        Authentication authenticate = authenticationManager.authenticate(authenticationToken); // 登录失败，会自动处理

        // 登录成功，取出用户信息
        UserDetailsImpl loginUser = (UserDetailsImpl) authenticate.getPrincipal();
        User user = loginUser.getUser();

        String jwt = JwtUtil.createJWT(user.getId().toString());

        Map<String, String> map = new HashMap<>();
        map.put("error_msg", "success");
        map.put("token", jwt);
        return map;
    }
}
