package com.gofocus.wxshop.main.shiro;

import com.gofocus.wxshop.main.generate.User;
import com.gofocus.wxshop.main.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: GoFocus
 * @Date: 2020-06-17 9:21
 * @Description:
 */

@Component
public class UserContext {

    private static UserThreadLocal currentUser;

    @Autowired
    public UserContext(UserService userService) {
        init(userService);
    }

    private static void init(UserService userService) {
        if (currentUser == null) {
            currentUser = new UserThreadLocal(userService);
        }
    }

    public static User getCurrentUser() {
        return currentUser.get();
    }

    public static void setCurrentUser(User user) {
        currentUser.set(user);
    }

    public static void remove() {
        currentUser.remove();
    }

}
