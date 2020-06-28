package com.gofocus.wxshop.api.rpc;

import com.gofocus.wxshop.api.data.OrderInfo;
import com.gofocus.wxshop.api.data.PaginationResponse;
import com.gofocus.wxshop.api.data.RpcOrderGoods;
import com.gofocus.wxshop.api.generate.Order;

/**
 * @Author: GoFocus
 * @Date: 2020-06-24 18:58
 * @Description:
 */

public interface OrderRpcService {

    Order placeOrder(OrderInfo orderInfo, Order order);

    RpcOrderGoods deleteOrder(long goodsId);

    PaginationResponse<RpcOrderGoods> getOrder(int pageNum, int pageSize, String status, Long userId);
}
