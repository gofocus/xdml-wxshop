package com.gofocus.wxshop.order.orderService;

import com.gofocus.wxshop.api.rpc.OrderService;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * @Author: GoFocus
 * @Date: 2020-06-25 0:02
 * @Description:
 */

@DubboService(version = "${wxshop.orderservice.version}")
public class OrderServiceImpl implements OrderService {

    @Override
    public String placeOrder(int goodsId, int number) {
        return "goodsId:" + goodsId + "number:" + number;
    }

}
