package com.gofocus.wxshop.main.service;

import com.gofocus.wxshop.main.entity.TelAndCode;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Pattern;

/**
 * @Author: GoFocus
 * @Date: 2020-06-16 13:19
 * @Description:
 */
@Service
public class TelVerificationService {
    private static final String TEL_REGEX = "^1\\d{10}$";
    public static final Pattern TEL_PATTERN = Pattern.compile(TEL_REGEX);

    public boolean verifyTelParameter(TelAndCode telAndCode) {
        String tel = Optional.ofNullable(telAndCode).map(TelAndCode::getTel).orElse("");
        return TEL_PATTERN.matcher(tel).find();
    }
}

