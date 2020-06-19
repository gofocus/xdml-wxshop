package com.gofocus.wxshop.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.gofocus.wxshop.AbstractIntegrationTest;
import com.gofocus.wxshop.WxshopApplication;
import com.gofocus.wxshop.entity.LoginResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

import static com.gofocus.wxshop.service.TelVerificationServiceTest.NULL_TEL;
import static com.gofocus.wxshop.service.TelVerificationServiceTest.VALID_PARAMETER;

/**
 * @Author: GoFocus
 * @Date: 2020-06-16 16:41
 * @Description:
 */

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = WxshopApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application.yml")
public class AuthIntegrationTest extends AbstractIntegrationTest {

    @Test
    public void loginLogoutTest() throws JsonProcessingException {
        boolean isLogin = readResponseBody(httpGet("/api/status", null), new TypeReference<LoginResponse>() {
        }).isLogin();
        Assertions.assertFalse(isLogin);

        int responseCode = httpPost("/api/code", VALID_PARAMETER, null).getCode();
        Assertions.assertEquals(HttpStatus.OK.value(), responseCode);

        Map<String, List<String>> headers = httpPost("/api/login", VALID_PARAMETER, null).getHeaders();
        String jSessionIdCookie = getJsessionid(headers);

        HttpResponse httpResponse = httpGet("/api/status", jSessionIdCookie);
        LoginResponse loginResponse = readResponseBody(httpResponse, new TypeReference<LoginResponse>() {
        });
        Assertions.assertTrue(loginResponse.isLogin());
        Assertions.assertEquals(VALID_PARAMETER.getTel(), loginResponse.getUser().getTel());

        httpGet("/api/logout", jSessionIdCookie);

        HttpResponse httpResponse2 = httpGet("/api/status", null);
        LoginResponse response2 = readResponseBody(httpResponse2, new TypeReference<LoginResponse>() {
        });
        Assertions.assertFalse(response2.isLogin());

    }

    @Test
    public void returnHttpStatusOkWhenParameterIsCorrect() throws JsonProcessingException {
        int responseCode = httpPost("/api/code", VALID_PARAMETER, null).getCode();
        Assertions.assertEquals(HttpServletResponse.SC_OK, responseCode);
    }


    @Test
    public void returnHttpStatusBadRequestWhenParameterIsIncorrect() throws JsonProcessingException {
        int responseCode = httpPost("/api/code", NULL_TEL, null).getCode();
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), responseCode);
    }

}
