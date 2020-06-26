package com.gofocus.wxshop.main.controller;

import com.gofocus.wxshop.main.entity.LoginResponse;
import com.gofocus.wxshop.main.entity.TelAndCode;
import com.gofocus.wxshop.main.generate.User;
import com.gofocus.wxshop.main.service.AuthService;
import com.gofocus.wxshop.main.service.TelVerificationService;
import com.gofocus.wxshop.main.service.UserService;
import com.gofocus.wxshop.main.shiro.UserContext;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * @Author: GoFocus
 * @Date: 2020-06-14 20:57
 * @Description:
 */

@RestController
@RequestMapping("/api")
public class AuthController {
    private final UserService userService;
    private final AuthService authService;
    private final TelVerificationService telVerificationService;

    @Autowired
    public AuthController(UserService userService, AuthService authService, TelVerificationService telVerificationService) {
        this.userService = userService;
        this.authService = authService;
        this.telVerificationService = telVerificationService;
    }

    @PostMapping("/code")
    public void code(@RequestBody TelAndCode telAndCode, HttpServletResponse response) {
        if (telVerificationService.verifyTelParameter(telAndCode)) {
            authService.sendVerificationCode(telAndCode.getTel());
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }


    @PostMapping("/login")
    public String login(@RequestBody TelAndCode telAndCode) {
        UsernamePasswordToken token = new UsernamePasswordToken(
                telAndCode.getTel(),
                telAndCode.getCode());
        token.setRememberMe(true);
        SecurityUtils.getSubject().login(token);

        return "当前登录用户名：" + SecurityUtils.getSubject().getPrincipal().toString();
    }

    @GetMapping("/status")
    public Object status() {
        User currentUser = UserContext.getCurrentUser();
        if (currentUser != null) {
            return LoginResponse.login(currentUser);
        } else {
            return LoginResponse.notLogin();
        }
    }

    @GetMapping("/logout")
    public void logOut() {
        SecurityUtils.getSubject().logout();
    }
}
