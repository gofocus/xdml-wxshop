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
    public static final TelAndCode INVALID_PARAMETER = new TelAndCode("1391234567800000");
    public static final TelAndCode VALID_PARAMETER = new TelAndCode("13912345678", "000000");
    public static final TelAndCode NULL_TEL = new TelAndCode(null);

    @Test
    void verifyTelParameter() {
        Assertions.assertFalse(new TelVerificationService().verifyTelParameter(INVALID_PARAMETER));
        Assertions.assertTrue(new TelVerificationService().verifyTelParameter(VALID_PARAMETER));
        Assertions.assertFalse(new TelVerificationService().verifyTelParameter(NULL_TEL));
    }
}
