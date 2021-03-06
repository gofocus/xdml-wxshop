package com.gofocus.wxshop.main.mock;

import com.gofocus.wxshop.api.data.OrderInfo;
import com.gofocus.wxshop.api.data.PaginationResponse;
import com.gofocus.wxshop.api.data.RpcOrderGoods;
import com.gofocus.wxshop.api.generate.Order;
import com.gofocus.wxshop.api.rpc.OrderRpcService;
import org.apache.dubbo.config.annotation.DubboService;
import org.mockito.Mock;

/**
 * @Author: GoFocus
 * @Date: 2020-06-25 21:55
 * @Description:
 */

@DubboService(version = "${wxshop.orderservice.version}")
public class MockOrderRpcService implements OrderRpcService {

    @Mock
    public OrderRpcService orderRpcService;

    @Override
    public Order placeOrder(OrderInfo orderInfo, Order order) {
        return orderRpcService.placeOrder(orderInfo, order);
    }

    @Override
    public RpcOrderGoods deleteOrder(long orderId) {
        return orderRpcService.deleteOrder(orderId);
    }

    @Override
    public PaginationResponse<RpcOrderGoods> getOrder(int pageNum, int pageSize, String status, Long userId) {
        return orderRpcService.getOrder(pageNum, pageSize, status, userId);
    }

    @Override
    public RpcOrderGoods updateOrder(Order order, Long userId) {
        return orderRpcService.updateOrder(order, userId);
    }

}
