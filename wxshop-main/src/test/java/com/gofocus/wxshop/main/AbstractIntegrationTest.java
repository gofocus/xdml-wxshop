package com.gofocus.wxshop.main;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kevinsawicki.http.HttpRequest;
import com.gofocus.wxshop.main.entity.LoginResponse;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.ClassicConfiguration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.Map;

import static com.github.kevinsawicki.http.HttpRequest.*;
import static com.gofocus.wxshop.main.service.TelVerificationServiceTest.VALID_PARAMETER;

/**
 * @Author: GoFocus
 * @Date: 2020-06-19 23:28
 * @Description:
 */
public abstract class AbstractIntegrationTest {

    @Value("${spring.datasource.url}")
    private String dbUrl;
    @Value("${spring.datasource.username}")
    private String dbUsername;
    @Value("${spring.datasource.password}")
    private String dbPassword;
    @Autowired
    private Environment environment;
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        ClassicConfiguration conf = new ClassicConfiguration();
        conf.setDataSource(dbUrl, dbUsername, dbPassword);
        Flyway flyway = new Flyway(conf);
        flyway.clean();
        flyway.migrate();
    }

    protected String loginAndGetCookie() throws JsonProcessingException {
        int responseCode = httpPost("/api/code", VALID_PARAMETER, null).getCode();
        Assertions.assertEquals(HttpStatus.OK.value(), responseCode);

        Map<String, List<String>> headers = httpPost("/api/login", VALID_PARAMETER, null).getHeaders();
        String cookie = getJsessionid(headers);

        HttpResponse httpResponse = httpGet("/api/status", cookie);
        LoginResponse loginResponse = readResponseBody(httpResponse, new TypeReference<LoginResponse>() {
        });
        Assertions.assertTrue(loginResponse.isLogin());
        Assertions.assertEquals(VALID_PARAMETER.getTel(), loginResponse.getUser().getTel());
        return cookie;
    }

    protected HttpResponse httpGet(String apiName, String cookie) throws JsonProcessingException {
        return doHttpRequest(METHOD_GET, apiName, null, cookie);
    }

    protected HttpResponse httpPost(String apiName, Object parameter, String cookie) throws JsonProcessingException {
        return doHttpRequest(METHOD_POST, apiName, parameter, cookie);
    }

    protected HttpResponse httpDelete(String apiName, String cookie) throws JsonProcessingException {
        return doHttpRequest(METHOD_DELETE, apiName, null, cookie);
    }

    private HttpResponse doHttpRequest(String method, String apiName, Object parameter, String cookie) throws JsonProcessingException {
        HttpRequest httpRequest = new HttpRequest(getUrl(apiName), method);
        if (!"".equals(cookie) && cookie != null) {
            httpRequest.header("Cookie", cookie);
        }
        httpRequest.contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON_VALUE);

        if (parameter != null) {
            httpRequest.send(objectMapper.writeValueAsString(parameter));
        }


        return new HttpResponse(httpRequest.code(), httpRequest.body(), httpRequest.headers());

    }

    public static class HttpResponse {
        private int code;
        private String body;
        private Map<String, List<String>> headers;

        public HttpResponse(int code, String body, Map<String, List<String>> headers) {
            this.code = code;
            this.body = body;
            this.headers = headers;
        }

        public int getCode() {
            return code;
        }

        public String getBody() {
            return body;
        }

        public Map<String, List<String>> getHeaders() {
            return headers;
        }
    }


    protected String getUrl(String apiName) {
        return "http://localhost:" + environment.getProperty("local.server.port") + apiName;
    }

    protected <T> T readResponseBody(HttpResponse response, TypeReference<T> typeReference) throws JsonProcessingException {
        return objectMapper.readValue(response.getBody(), typeReference);
    }

    protected String getJsessionid(Map<String, List<String>> headers) {
        return headers.get("Set-Cookie").stream().filter(str -> str.contains("JSESSIONID")).findFirst().orElse("").split(";")[0];
    }

}
