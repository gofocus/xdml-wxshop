package com.gofocus.wxshop.controller;

import com.gofocus.wxshop.api.OrderService;
import org.apache.dubbo.config.annotation.DubboReference;
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

    @DubboReference(version = "${wxshop.orderservice.version}")
    private OrderService orderService;

}
