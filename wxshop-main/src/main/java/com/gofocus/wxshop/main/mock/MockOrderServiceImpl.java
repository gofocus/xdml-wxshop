package com.gofocus.wxshop.main.mock;

import com.gofocus.wxshop.api.rpc.OrderService;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * @Author: GoFocus
 * @Date: 2020-06-25 21:55
 * @Description:
 */

@DubboService(version = "${wxshop.orderservice.version}")
public class MockOrderServiceImpl implements OrderService {

    @Override
    public String placeOrder(int goodsId, int number) {
        return "i'm mock service!";
    }
}
