package com.gofocus.wxshop.main.controller;

import com.gofocus.wxshop.api.rpc.OrderService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: GoFocus
 * @Date: 2020-06-24 23:45
 * @Description:
 */

@RestController
@RequestMapping("/api/v1/order")
public class OrderController {

    @DubboReference(version = "${wxshop.orderservice.version}", url = "${wxshop.orderservice.url}")
    private OrderService orderService;

    @GetMapping
    public String testRpc() {
        String s = orderService.placeOrder(1, 2);
        System.out.println(s);
        return s;
    }

}
