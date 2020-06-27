package com.gofocus.wxshop.order.orderservice;

import com.gofocus.wxshop.api.data.OrderInfo;
import com.gofocus.wxshop.api.generate.Order;
import com.gofocus.wxshop.api.rpc.OrderRpcService;
import com.gofocus.wxshop.order.dao.OrderGoodsDao;
import com.gofocus.wxshop.order.generate.OrderMapper;
import org.apache.dubbo.config.annotation.DubboService;

import java.util.function.BooleanSupplier;

/**
 * @Author: GoFocus
 * @Date: 2020-06-25 0:02
 * @Description:
 */

@DubboService(version = "${wxshop.orderservice.version}")
public class OrderRpcServiceImpl implements OrderRpcService {

    private final OrderMapper orderMapper;
    private final OrderGoodsDao orderGoodsDao;

    public OrderRpcServiceImpl(OrderMapper orderMapper, OrderGoodsDao orderGoodsDao) {
        this.orderMapper = orderMapper;
        this.orderGoodsDao = orderGoodsDao;
    }

    @Override
    public Order placeOrder(OrderInfo orderInfo, Order order) {
        verifyOrderParams(order);
        orderMapper.insertSelective(order);
        orderGoodsDao.insertOrderGoods(orderInfo);
        return order;
    }

    private void verifyOrderParams(Order order) {
        verify(() -> order.getAddress() == null, "address不能为空！");
        verify(() -> order.getTotalPrice() == null || order.getTotalPrice().doubleValue() < 0, "totalPrice非法！");
        verify(() -> order.getUserId() == null, "userId不能为空！");
    }

    private void verify(BooleanSupplier supplier, String message) {
        if (supplier.getAsBoolean()) {
            throw new IllegalArgumentException(message);
        }
    }

}
