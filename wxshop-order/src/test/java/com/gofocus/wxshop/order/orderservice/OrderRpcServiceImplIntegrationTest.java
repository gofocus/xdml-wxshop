package com.gofocus.wxshop.order.orderservice;

import com.gofocus.wxshop.api.data.GoodsInfo;
import com.gofocus.wxshop.api.data.OrderInfo;
import com.gofocus.wxshop.api.generate.Order;
import com.gofocus.wxshop.order.OrderApplication;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.Date;

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
        order.setStatus("PENDING");
        order.setAddress("水星");
        order.setCreatedAt(new Date());
        order.setUpdatedAt(new Date());
        orderInfo.setGoods(Arrays.asList(new GoodsInfo(1L, 5), new GoodsInfo(2L, 5)));

    }

    @Test
    void placeOrderSucceed() {
        Order order = orderRpcService.placeOrder(orderInfo, this.order);

        Assertions.assertEquals(1L, order.getId());

    }

    @Test
    void cancelOrder() {
    }
}