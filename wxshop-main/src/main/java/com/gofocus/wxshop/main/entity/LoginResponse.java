package com.gofocus.wxshop.main.entity;

import com.gofocus.wxshop.main.generate.User;

/**
 * @Author: GoFocus
 * @Date: 2020-06-17 10:36
 * @Description:
 */
public class LoginResponse {
    private boolean login;
    private User user;

    public static LoginResponse notLogin() {
        return new LoginResponse(false);
    }

    public static LoginResponse login(User user) {
        return new LoginResponse(true, user);
    }

    public LoginResponse() {
    }

    public LoginResponse(boolean login) {
        this.login = login;
    }

    public LoginResponse(boolean login, User user) {
        this.login = login;
        this.user = user;
    }

    public boolean isLogin() {
        return login;
    }

    public User getUser() {
        return user;
    }
}
