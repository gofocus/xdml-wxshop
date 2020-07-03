package com.gofocus.wxshop.main;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.gofocus.wxshop.api.DataStatus;
import com.gofocus.wxshop.api.data.GoodsInfo;
import com.gofocus.wxshop.api.data.OrderInfo;
import com.gofocus.wxshop.api.data.PaginationResponse;
import com.gofocus.wxshop.api.data.RpcOrderGoods;
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

import java.util.*;

import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

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
    private Order order;
    private List<GoodsInfo> goodsInfos;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(mockOrderRpcService);
        this.order = new Order();
        order.setUpdatedAt(new Date());
        order.setCreatedAt(new Date());
        order.setId(1L);
        order.setAddress("海王星");
        order.setUserId(1L);
        order.setTotalPrice(2333L);
        order.setShopId(1L);

        goodsInfos = Arrays.asList(new GoodsInfo(1L, 5), new GoodsInfo(2L, 5));

        lenient().when(mockOrderRpcService.orderRpcService.placeOrder(any(), any())).thenAnswer(invocation -> {
            Order order = invocation.getArgument(1);
            order.setId(1L);
            return order;
        });
    }

    @Test
    void getOrderFailed4BadParamStatus() throws JsonProcessingException {
        String cookie = loginAndGetCookie();
        HttpResponse httpResponse = httpGet("/api/v1/order?pageNum=1&pageSize=2&status=wrongStatus", cookie);
        assertEquals(HttpStatus.BAD_REQUEST.value(), httpResponse.getCode());
    }

    @Test
    void getOrdersSucceed() throws JsonProcessingException {
        RpcOrderGoods rpcOrderGoods = new RpcOrderGoods();
        order.setStatus(DataStatus.PENDING.getName());
        rpcOrderGoods.setOrder(order);
        rpcOrderGoods.setGoods(goodsInfos);
        when(mockOrderRpcService.orderRpcService.getOrder(anyInt(), anyInt(), anyString(), anyLong())).thenAnswer(invocation -> new PaginationResponse<>(invocation.getArgument(0), invocation.getArgument(1), 3, Collections.singletonList(rpcOrderGoods)));

        String cookie = loginAndGetCookie();
        HttpResponse httpResponse = httpGet("/api/v1/order?pageNum=1&pageSize=2&status=pending", cookie);

        PaginationResponse<OrderResponse> response = readResponseBody(httpResponse, new TypeReference<PaginationResponse<OrderResponse>>() {
        });

        List<OrderResponse> orderResponses = response.getData();
        assertEquals(1, response.getPageNum());
        assertEquals(2, response.getPageSize());
        assertEquals(3, response.getTotalPage());
        assertEquals(1L, orderResponses.get(0).getShopId());
        assertEquals(order.getStatus(), orderResponses.get(0).getStatus());
        assertEquals(2, orderResponses.get(0).getGoods().size());
        assertEquals(5, orderResponses.get(0).getGoods().get(0).getNumber());
        assertEquals(100, orderResponses.get(0).getGoods().get(0).getPrice());

    }

    @Test
    void deleteOrderSucceed() throws JsonProcessingException {
        RpcOrderGoods rpcOrderGoods = new RpcOrderGoods();
        order.setStatus(DataStatus.DELETED.getName());
        rpcOrderGoods.setOrder(order);
        rpcOrderGoods.setGoods(goodsInfos);
        when(mockOrderRpcService.orderRpcService.deleteOrder(anyLong())).thenReturn(rpcOrderGoods);

        createOrderSucceed();
        String cookie = loginAndGetCookie();
        HttpResponse response = httpDelete("/api/v1/order/1", cookie);
        Response<OrderResponse> responseBody = readResponseBody(response, new TypeReference<Response<OrderResponse>>() {
        });
        OrderResponse orderResponse = responseBody.getData();

        assertEquals(1L, orderResponse.getShopId());
        assertEquals(2, orderResponse.getGoods().size());
        assertEquals(DataStatus.DELETED.getName(), orderResponse.getStatus());
        assertEquals(1L, orderResponse.getUserId());
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
        orderInfo.setGoods(new ArrayList<>(Arrays.asList(
                new GoodsInfo(1L, 6),
                new GoodsInfo(2L, 7)
        )));

        HttpResponse response = httpPost("/api/v1/order", orderInfo, cookie);
        Response<OrderResponse> orderResponseResponse = readResponseBody(response, new TypeReference<Response<OrderResponse>>() {
        });

        String message = orderResponseResponse.getMessage();
        Assertions.assertNotEquals("", message);
        assertEquals(HttpStatus.GONE.value(), response.getCode());

        //测试之前扣减库存失败后是否正确回滚
        createOrderSucceed();
    }

}
