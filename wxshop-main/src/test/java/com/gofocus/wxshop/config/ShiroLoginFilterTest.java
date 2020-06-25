package com.gofocus.wxshop.config;

import com.github.kevinsawicki.http.HttpRequest;
import com.gofocus.wxshop.WxshopApplication;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static java.net.HttpURLConnection.HTTP_UNAUTHORIZED;

/**
 * @Author: GoFocus
 * @Date: 2020-06-17 16:17
 * @Description:
 */

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = WxshopApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {"spring.config.location=classpath:application-test.yml"})
class ShiroLoginFilterTest {

    @Autowired
    Environment environment;

    @Test
    void onAccessDenied() {
        int responseCode = HttpRequest.post(getUrl("/api/any"))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .code();
        Assertions.assertEquals(HTTP_UNAUTHORIZED, responseCode);
    }

    private String getUrl(String apiName) {
        return "http://localhost:" + environment.getProperty("local.server.port") + apiName;
    }
}
