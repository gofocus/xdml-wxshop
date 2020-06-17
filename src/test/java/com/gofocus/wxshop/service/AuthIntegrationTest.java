package com.gofocus.wxshop.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kevinsawicki.http.HttpRequest;
import com.gofocus.wxshop.WxshopApplication;
import com.gofocus.wxshop.entity.LoginResponse;
import com.gofocus.wxshop.entity.TelAndCode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
public class AuthIntegrationTest {

    @Autowired
    Environment environment;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void loginLogoutTest() throws JsonProcessingException {
        HttpRequest request = httpGet("/api/status");
        boolean isLogin = getLoginResponse(request.body()).isLogin();
        Assertions.assertFalse(isLogin);

        int responseCode = httpPost("/api/code", VALID_PARAMETER).code();
        Assertions.assertEquals(HttpStatus.OK.value(), responseCode);

        Map<String, List<String>> headers = httpPost("/api/login", VALID_PARAMETER).headers();
        String jSessionIdCookie = getJsessionid(headers);

        String statusResponse = httpGet("/api/status").header("Cookie", jSessionIdCookie).body();
        LoginResponse loginResponse = getLoginResponse(statusResponse);
        Assertions.assertTrue(loginResponse.isLogin());
        Assertions.assertEquals(VALID_PARAMETER.getTel(), loginResponse.getUser().getTel());

        httpGet("/api/logout").header("Cookie", jSessionIdCookie).body();

        String statusResponse2 = httpGet("/api/status").header("Cookie", jSessionIdCookie).body();
        LoginResponse response2 = getLoginResponse(statusResponse2);
        Assertions.assertFalse(response2.isLogin());

    }

    private LoginResponse getLoginResponse(String body) throws JsonProcessingException {
        return objectMapper.readValue(body, LoginResponse.class);
    }

    private String getJsessionid(Map<String, List<String>> headers) {
        return headers.get("Set-Cookie").stream().filter(str -> str.contains("JSESSIONID")).findFirst().orElse("").split(";")[0];
    }

    private HttpRequest httpGet(String apiName) {
        return HttpRequest.get(getUrl(apiName))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE);
    }

    private HttpRequest httpPost(String apiName, TelAndCode parameter) throws JsonProcessingException {
        return HttpRequest.post(getUrl(apiName))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .send(objectMapper.writeValueAsString(parameter));
    }

    @Test
    public void returnHttpStatusOkWhenParameterIsCorrect() throws JsonProcessingException {
        int responseCode = httpPost("/api/code", VALID_PARAMETER).code();
        Assertions.assertEquals(HttpServletResponse.SC_OK, responseCode);
    }

    private String getUrl(String apiName) {
        return "http://localhost:" + environment.getProperty("local.server.port") + apiName;
    }

    @Test
    public void returnHttpStatusBadRequestWhenParameterIsIncorrect() throws JsonProcessingException {
        int responseCode = httpPost("/api/code", NULL_TEL).code();
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), responseCode);
    }

}
