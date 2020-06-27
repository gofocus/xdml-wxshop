package com.gofocus.wxshop.api.rpc;

import com.gofocus.wxshop.api.data.OrderInfo;
import com.gofocus.wxshop.api.generate.Order;

/**
 * @Author: GoFocus
 * @Date: 2020-06-24 18:58
 * @Description:
 */

public interface OrderRpcService {

    Order placeOrder(OrderInfo orderInfo, Order order);

}
