package com.gofocus.wxshop.main.controller;

import com.gofocus.wxshop.api.data.OrderInfo;
import com.gofocus.wxshop.main.entity.OrderResponse;
import com.gofocus.wxshop.main.entity.Response;
import com.gofocus.wxshop.main.exception.HttpException;
import com.gofocus.wxshop.main.service.OrderService;
import com.gofocus.wxshop.main.shiro.UserContext;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

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
    public Response<OrderResponse> createOrder(@RequestBody OrderInfo orderInfo, HttpServletResponse response) {
        try {
            OrderResponse orderResponse = orderService.placeOrder(orderInfo, UserContext.getCurrentUser().getId());
            return Response.success(orderResponse);
        } catch (HttpException e) {
            response.setStatus(e.getStatusCode());
            return Response.failure(e.getMessage());
        }

    }

}
