package com.gofocus.wxshop.main.service;

import org.springframework.stereotype.Service;

/**
 * @Author: GoFocus
 * @Date: 2020-06-14 23:10
 * @Description:
 */

@Service
public class MockSmsCodeService implements SmsCodeService {

    @Override
    public String sendSmsCode(String tel) {
        return "000000";
    }


}
