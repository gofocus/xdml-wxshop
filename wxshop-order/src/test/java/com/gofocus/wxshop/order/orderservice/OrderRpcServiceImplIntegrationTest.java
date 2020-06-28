package com.gofocus.wxshop.order.orderservice;

import com.gofocus.wxshop.api.DataStatus;
import com.gofocus.wxshop.api.data.GoodsInfo;
import com.gofocus.wxshop.api.data.OrderInfo;
import com.gofocus.wxshop.api.data.PaginationResponse;
import com.gofocus.wxshop.api.data.RpcOrderGoods;
import com.gofocus.wxshop.api.generate.Order;
import com.gofocus.wxshop.order.OrderApplication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @Author: GoFocus
 * @Date: 2020-06-28 11:10
 * @Description:
 */

@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = OrderApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {"spring.config.location=classpath:application-test.yml"})
class OrderRpcServiceImplIntegrationTest extends AbstractIntegrationTest {

    private Order order;
    private OrderInfo orderInfo;
    @Autowired
    private OrderRpcServiceImpl orderRpcService;

    @BeforeEach
    void setUp() {
        super.setUp();
        MockitoAnnotations.initMocks(this);
        this.order = new Order();
        this.orderInfo = new OrderInfo();
        order.setShopId(1L);
        order.setTotalPrice(1000L);
        order.setUserId(1L);
        order.setStatus(DataStatus.PENDING.getName());
        order.setAddress("水星");
        order.setCreatedAt(new Date());
        order.setUpdatedAt(new Date());
        orderInfo.setGoods(Arrays.asList(new GoodsInfo(1L, 5), new GoodsInfo(2L, 5)));
    }

    @Test
    void placeOrderSucceed() {
        Order order = orderRpcService.placeOrder(orderInfo, this.order);
        assertEquals(this.order.getShopId(), order.getShopId());
        assertEquals(DataStatus.PENDING.getName(), order.getStatus());
        assertEquals(this.order.getAddress(), order.getAddress());
        assertEquals(this.order.getTotalPrice(),order.getTotalPrice());
    }

    @Test
    void cancelOrder() {
    }

    @Test
    void getAllOrdersByUserIdSucceed() {
        placeOrderSucceed();
        placeOrderSucceed();
        placeOrderSucceed();
        placeOrderSucceed();
        placeOrderSucceed();
        PaginationResponse<RpcOrderGoods> rpcOrderGoodsWithPage = orderRpcService.getOrder(1, 2, DataStatus.PENDING.getName(), 1L);
        assertEquals(3, rpcOrderGoodsWithPage.getTotalPage());
        assertEquals(1, rpcOrderGoodsWithPage.getPageNum());
        assertEquals(2, rpcOrderGoodsWithPage.getPageSize());
        assertEquals(1L, rpcOrderGoodsWithPage.getData().get(0).getOrder().getId());
    }
}