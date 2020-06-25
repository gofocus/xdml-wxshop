package com.gofocus.wxshop.api;

import org.apache.dubbo.config.annotation.DubboService;

/**
 * @Author: GoFocus
 * @Date: 2020-06-25 0:02
 * @Description:
 */

@DubboService(version = "${wxshop.orderservice.version}")
public class OrderServiceImpl implements OrderService {

    @Override
    public void placeOrder(int goodsId, int number) {
        System.out.println("goodsId = " + goodsId);
        System.out.println("number = " + number);
    }
}
