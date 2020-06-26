package com.gofocus.wxshop.main.shiro;

import com.gofocus.wxshop.main.generate.User;
import com.gofocus.wxshop.main.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Component;

/**
 * @Author: GoFocus
 * @Date: 2020-06-16 23:14
 * @Description:
 */

@Component
public class UserThreadLocal extends ThreadLocal<User> {
    private final UserService userService;

    public UserThreadLocal(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected User initialValue() {
        Object userTel = SecurityUtils.getSubject().getPrincipal();
        if (userTel != null) {
            User user = userService.getUserByTel(userTel.toString());
            UserContext.setCurrentUser(user);
            return user;
        }
        return null;
    }
}
