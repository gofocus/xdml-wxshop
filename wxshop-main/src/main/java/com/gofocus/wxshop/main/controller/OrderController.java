package com.gofocus.wxshop.main.controller;

import com.gofocus.wxshop.api.data.OrderInfo;
import com.gofocus.wxshop.main.entity.OrderResponse;
import com.gofocus.wxshop.main.entity.Response;
import com.gofocus.wxshop.main.service.OrderService;
import com.gofocus.wxshop.main.shiro.UserContext;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: GoFocus
 * @Date: 2020-06-24 23:45
 * @Description:
 */

@RestController
@RequestMapping("/api/v1/order")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public Response<OrderResponse> createOrder(@RequestBody OrderInfo orderInfo) {
        OrderResponse orderResponse = orderService.placeOrder(orderInfo, UserContext.getCurrentUser().getId());
        return Response.success(orderResponse);
    }

    @DeleteMapping("{id}")
    public Response<OrderResponse> cancelOrder(@PathVariable String id) {
        OrderResponse orderResponse = orderService.cancelOrder(id);
        return Response.success(orderResponse);
    }

}
