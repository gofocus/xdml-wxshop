package com.gofocus.wxshop.controller;

import com.gofocus.wxshop.entity.TelAndCode;
import com.gofocus.wxshop.service.AuthService;
import com.gofocus.wxshop.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @Autowired
    public AuthController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    @PostMapping("/code")
    public void code(@RequestBody TelAndCode telAndCode) {
        authService.sendVerificationCode(telAndCode.getTel());
    }


    @PostMapping("/login")
    public String login(@RequestBody TelAndCode telAndCode) {
        UsernamePasswordToken token = new UsernamePasswordToken(
                telAndCode.getTel(),
                telAndCode.getCode());

        SecurityUtils.getSubject().login(token);

        return SecurityUtils.getSubject().getPrincipal().toString();
    }
}
