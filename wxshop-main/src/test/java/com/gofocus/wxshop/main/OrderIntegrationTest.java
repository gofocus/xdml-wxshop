package com.gofocus.wxshop.main;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.gofocus.wxshop.api.DataStatus;
import com.gofocus.wxshop.api.data.GoodsInfo;
import com.gofocus.wxshop.api.data.OrderInfo;
import com.gofocus.wxshop.api.generate.Order;
import com.gofocus.wxshop.main.entity.GoodsWithNumber;
import com.gofocus.wxshop.main.entity.OrderResponse;
import com.gofocus.wxshop.main.entity.Response;
import com.gofocus.wxshop.main.generate.Goods;
import com.gofocus.wxshop.main.mock.MockOrderRpcService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Arrays;

import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;

/**
 * @Author: GoFocus
 * @Date: 2020-06-27 16:45
 * @Description:
 */


@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = WxshopApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {"spring.config.location=classpath:application-test.yml"})
public class OrderIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private MockOrderRpcService mockOrderRpcService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(mockOrderRpcService);

        lenient().when(mockOrderRpcService.orderRpcService.placeOrder(any(), any())).thenAnswer(invocation -> {
            Order order = invocation.getArgument(1);
            order.setId(1L);
            return order;
        });
    }

    @Test
    void createOrderSucceed() throws JsonProcessingException {
        String cookie = loginAndGetCookie();

        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setGoods(new ArrayList<>(Arrays.asList(new GoodsInfo(1L, 3), new GoodsInfo(2L, 3))));

        HttpResponse response = httpPost("/api/v1/order", orderInfo, cookie);
        Response<OrderResponse> orderResponseResponse = readResponseBody(response, new TypeReference<Response<OrderResponse>>() {
        });

        OrderResponse data = orderResponseResponse.getData();
        assertEquals(1L, data.getShop().getId());
        assertEquals(1L, data.getShop().getId());
        assertEquals(1L, data.getShopId());
        assertEquals(DataStatus.PENDING.getName(), data.getStatus());
        assertEquals(600L, data.getTotalPrice());
        assertEquals("火星", data.getAddress());
        assertEquals(Arrays.asList(1L, 2L), data.getGoods().stream().map(Goods::getId).collect(toList()));
        assertEquals(Arrays.asList(3, 3), data.getGoods().stream().map(GoodsWithNumber::getNumber).collect(toList()));
    }

    @Test
    void createOrderDeductStockFails() throws JsonProcessingException {
        String cookie = loginAndGetCookie();

        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setGoods(new ArrayList<>(Arrays.asList(new GoodsInfo(1L, 6), new GoodsInfo(2L, 7))));

        HttpResponse response = httpPost("/api/v1/order", orderInfo, cookie);
        Response<OrderResponse> orderResponseResponse = readResponseBody(response, new TypeReference<Response<OrderResponse>>() {
        });

        String message = orderResponseResponse.getMessage();
        Assertions.assertNotEquals("", message);
        Assertions.assertEquals(HttpStatus.GONE.value(), response.getCode());

        //测试之前扣减库存失败后是否正确回滚
        createOrderSucceed();
    }

}
