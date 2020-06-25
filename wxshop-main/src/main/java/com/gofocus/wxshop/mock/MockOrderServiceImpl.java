package com.gofocus.wxshop.mock;

import com.gofocus.wxshop.api.OrderService;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * @Author: GoFocus
 * @Date: 2020-06-25 21:55
 * @Description:
 */

@DubboService(version = "${wxshop.orderservice.version}")
public class MockOrderServiceImpl implements OrderService {

    @Override
    public void placeOrder(int goodsId, int number) {
        System.out.println("i'm mock service!");
    }
}
