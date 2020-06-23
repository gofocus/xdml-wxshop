package com.gofocus.wxshop.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.gofocus.wxshop.AbstractIntegrationTest;
import com.gofocus.wxshop.WxshopApplication;
import com.gofocus.wxshop.entity.PaginationResponse;
import com.gofocus.wxshop.entity.ShoppingCartData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * @Author: GoFocus
 * @Date: 2020-06-23 10:36
 * @Description:
 */

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = WxshopApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {"spring.config.location=classpath:application-test.yml"})
public class ShoppingCartIntegrationTest extends AbstractIntegrationTest {

    @Test
    void getShoppingCartGoodsByUserIdSucceed() throws JsonProcessingException {
        String cookie = loginAndGetCookie();
        HttpResponse httpResponse = httpGet("/api/v1/shoppingCart?pageNum=1&pageSize=2", cookie);

        PaginationResponse<ShoppingCartData> paginationResponse = readResponseBody(httpResponse, new TypeReference<PaginationResponse<ShoppingCartData>>() {
        });

        ShoppingCartData shoppingCartData1 = paginationResponse.getData().get(0);
        ShoppingCartData shoppingCartData2 = paginationResponse.getData().get(1);
        Assertions.assertEquals(shoppingCartData1.getGoods().size(), 1);
        Assertions.assertEquals(shoppingCartData2.getGoods().size(), 3);

    }

}
