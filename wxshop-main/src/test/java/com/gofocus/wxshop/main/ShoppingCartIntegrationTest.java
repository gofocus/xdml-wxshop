package com.gofocus.wxshop.main;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.gofocus.wxshop.api.data.PaginationResponse;
import com.gofocus.wxshop.main.controller.ShoppingCartController;
import com.gofocus.wxshop.main.entity.AddToShoppingCartItem;
import com.gofocus.wxshop.main.entity.GoodsWithNumber;
import com.gofocus.wxshop.main.entity.Response;
import com.gofocus.wxshop.main.entity.ShoppingCartData;
import com.gofocus.wxshop.main.generate.Shop;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
        assertEquals(2, shoppingCartData1.getGoods().size());
        assertEquals(2, shoppingCartData2.getGoods().size());

    }

    @Test
    void addToShoppingCartSucceed() throws JsonProcessingException {
        String cookie = loginAndGetCookie();

        ShoppingCartController.AddToShoppingCartRequest request = new ShoppingCartController.AddToShoppingCartRequest();
        ArrayList<AddToShoppingCartItem> items = new ArrayList<>();
        items.add(new AddToShoppingCartItem(1L, 100));
        items.add(new AddToShoppingCartItem(2L, 100));
        request.setGoods(items);

        HttpResponse httpResponse = httpPost("/api/v1/shoppingCart", request, cookie);

        Response<ShoppingCartData> shoppingCartDataResponse = readResponseBody(httpResponse, new TypeReference<Response<ShoppingCartData>>() {
        });

        Shop shop = shoppingCartDataResponse.getData().getShop();
        assertEquals(1L, shop.getId());

        int goodsSize = shoppingCartDataResponse.getData().getGoods().size();
        assertEquals(2, goodsSize);
    }

    @Test
    void deleteGoodsInShoppingCart() throws JsonProcessingException {
        String cookie = loginAndGetCookie();

        HttpResponse httpResponse = httpDelete("/api/v1/shoppingCart/1", cookie);

        Response<ShoppingCartData> response = readResponseBody(httpResponse, new TypeReference<Response<ShoppingCartData>>() {
        });

        Shop shop = response.getData().getShop();
        List<GoodsWithNumber> goods = response.getData().getGoods();

        assertEquals(1L, shop.getId());
        assertEquals(1, goods.size());
        assertEquals(2L, goods.get(0).getId());
        assertEquals("ok", goods.get(0).getStatus());
    }

}
