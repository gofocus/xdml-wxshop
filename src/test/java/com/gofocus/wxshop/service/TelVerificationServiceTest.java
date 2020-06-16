package com.gofocus.wxshop.service;

import com.gofocus.wxshop.entity.TelAndCode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @Author: GoFocus
 * @Date: 2020-06-16 14:19
 * @Description:
 */
class TelVerificationServiceTest {

    @Test
    void verifyTelParameter() {
        Assertions.assertTrue(new TelVerificationService().verifyTelParameter(new TelAndCode("13912345678")));
        Assertions.assertFalse(new TelVerificationService().verifyTelParameter(new TelAndCode("1")));
        Assertions.assertFalse(new TelVerificationService().verifyTelParameter(new TelAndCode(null)));
        Assertions.assertFalse(new TelVerificationService().verifyTelParameter(new TelAndCode("1123455")));
        Assertions.assertFalse(new TelVerificationService().verifyTelParameter(new TelAndCode("1123139123456784525")));
    }
}
