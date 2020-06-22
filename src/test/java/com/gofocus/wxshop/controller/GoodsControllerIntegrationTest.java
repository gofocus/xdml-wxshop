package com.gofocus.wxshop.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.gofocus.wxshop.AbstractIntegrationTest;
import com.gofocus.wxshop.WxshopApplication;
import com.gofocus.wxshop.entity.Goods;
import com.gofocus.wxshop.entity.PaginationResponse;
import com.gofocus.wxshop.entity.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static javax.servlet.http.HttpServletResponse.*;

/**
 * @Author: GoFocus
 * @Date: 2020-06-19 16:53
 * @Description:
 */

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = WxshopApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {"spring.config.location=classpath:application-test.yml"})
class GoodsControllerIntegrationTest extends AbstractIntegrationTest {

    @Test
    void createGoods() throws JsonProcessingException {
        String cookie = loginAndGetCookie();
        Goods goods = new Goods();
        goods.setName("肥皂");
        goods.setDescription("纯天然无污染肥皂");
        goods.setDetails("这是一块好肥皂");
        goods.setImgUrl("https://img.url");
        goods.setPrice(500L);
        goods.setStock(10);
        goods.setShopId(1L);
        HttpResponse httpResponse = httpPost("/api/v1/goods", goods, cookie);
        Response<Goods> goodsResponse = readResponseBody(httpResponse, new TypeReference<Response<Goods>>() {
        });

        Assertions.assertEquals(httpResponse.getCode(), SC_CREATED);
        Assertions.assertEquals(goodsResponse.getData().getShopId(), 1L);
        Assertions.assertEquals(goodsResponse.getData().getName(), "肥皂");
        Assertions.assertEquals(goodsResponse.getData().getStatus(), "ok");

        goods.setShopId(3L);
        HttpResponse httpResponse2 = httpPost("/api/v1/goods", goods, cookie);
        Assertions.assertEquals(SC_FORBIDDEN, httpResponse2.getCode());

        goods.setShopId(1L);
        HttpResponse httpResponse3 = httpPost("/api/v1/goods", goods, null);
        Assertions.assertEquals(SC_UNAUTHORIZED, httpResponse3.getCode());

    }

    @Test
    void deleteGoods() throws JsonProcessingException {
        String cookie = loginAndGetCookie();
        HttpResponse httpResponse = httpDelete("/api/v1/goods/1", cookie);
        Response<Goods> goodsResponse = readResponseBody(httpResponse, new TypeReference<Response<Goods>>() {
        });
        Assertions.assertEquals("goods1", goodsResponse.getData().getName());
        Assertions.assertEquals("deleted", goodsResponse.getData().getStatus());

        HttpResponse httpResponse2 = httpDelete("/api/v1/goods/9999", cookie);
        Assertions.assertEquals(SC_NOT_FOUND, httpResponse2.getCode());

        HttpResponse httpResponse3 = httpDelete("/api/v1/goods/3", cookie);
        Assertions.assertEquals(SC_FORBIDDEN, httpResponse3.getCode());
    }


    @Test
    void getGoods() throws JsonProcessingException {
        String cookie = loginAndGetCookie();

        HttpResponse httpResponse = httpGet("/api/v1/goods?pageSize=3&pageNum=2", cookie);
        PaginationResponse<Goods> goods = readResponseBody(httpResponse, new TypeReference<PaginationResponse<Goods>>() {
        });

        Assertions.assertEquals(SC_OK, httpResponse.getCode());
        Assertions.assertEquals(4, goods.getData().get(0).getId());

        HttpResponse httpResponse2 = httpGet("/api/v1/goods?pageSize=4&pageNum=2&shopId=2", cookie);
        PaginationResponse<Goods> goods2 = readResponseBody(httpResponse2, new TypeReference<PaginationResponse<Goods>>() {
        });

        Assertions.assertEquals(SC_OK, httpResponse.getCode());
        Assertions.assertEquals(3, goods2.getData().get(0).getId());
        Assertions.assertEquals(3, goods2.getData().size());

    }


}
