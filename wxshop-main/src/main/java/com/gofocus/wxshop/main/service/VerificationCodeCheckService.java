package com.gofocus.wxshop.main.service;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: GoFocus
 * @Date: 2020-06-14 23:17
 * @Description:
 */

@Service
public class VerificationCodeCheckService {
    private Map<String, String> telToCorrectCode = new ConcurrentHashMap<>();

    public void addCode(String tel, String correctCode) {
        telToCorrectCode.put(tel, correctCode);
    }

    public String getCorrectCode(String tel) {
        return telToCorrectCode.get(tel);
    }
}
