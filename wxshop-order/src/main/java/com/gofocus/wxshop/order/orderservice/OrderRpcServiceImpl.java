package com.gofocus.wxshop.order.orderservice;

import com.gofocus.wxshop.api.DataStatus;
import com.gofocus.wxshop.api.HttpException;
import com.gofocus.wxshop.api.data.GoodsInfo;
import com.gofocus.wxshop.api.data.OrderInfo;
import com.gofocus.wxshop.api.data.RpcOrderGoods;
import com.gofocus.wxshop.api.generate.Order;
import com.gofocus.wxshop.api.generate.OrderGoodsExample;
import com.gofocus.wxshop.api.rpc.OrderRpcService;
import com.gofocus.wxshop.order.dao.OrderGoodsDao;
import com.gofocus.wxshop.order.generate.OrderGoodsMapper;
import com.gofocus.wxshop.order.generate.OrderMapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.function.BooleanSupplier;

import static java.util.stream.Collectors.toList;

/**
 * @Author: GoFocus
 * @Date: 2020-06-25 0:02
 * @Description:
 */

@Service
@DubboService(version = "${wxshop.orderservice.version}")
public class OrderRpcServiceImpl implements OrderRpcService {

    private final OrderMapper orderMapper;
    private final OrderGoodsDao orderGoodsDao;
    private final OrderGoodsMapper orderGoodsMapper;

    public OrderRpcServiceImpl(OrderMapper orderMapper, OrderGoodsDao orderGoodsDao, OrderGoodsMapper orderGoodsMapper) {
        this.orderMapper = orderMapper;
        this.orderGoodsDao = orderGoodsDao;
        this.orderGoodsMapper = orderGoodsMapper;
    }

    @Override
    public Order placeOrder(OrderInfo orderInfo, Order order) {
        verifyOrderParams(order);
        orderMapper.insertSelective(order);
        orderInfo.setOrderId(order.getId());
        orderGoodsDao.insertOrderGoods(orderInfo);
        return order;
    }

    @Override
    public RpcOrderGoods deleteOrder(long orderId) {
        doDeleteOrder(orderId);
        Order order = orderMapper.selectByPrimaryKey(orderId);
        if (order == null) {
            throw HttpException.notFound("订单未找到:" + orderId);
        }
        List<GoodsInfo> goodsInfos = getGoodsInfos(orderId);
        return new RpcOrderGoods(order, goodsInfos);
    }

    private List<GoodsInfo> getGoodsInfos(long orderId) {
        OrderGoodsExample example = new OrderGoodsExample();
        example.createCriteria().andOrderIdEqualTo(orderId);
        return orderGoodsMapper.selectByExample(example).stream().map(orderGood -> new GoodsInfo(orderGood.getGoodsId(), Math.toIntExact(orderGood.getNumber()))).collect(toList());
    }

    private void doDeleteOrder(long orderId) {
        Order order = new Order();
        order.setId(orderId);
        order.setUpdatedAt(new Date());
        order.setStatus(DataStatus.DELETED.getName());
        orderMapper.updateByPrimaryKeySelective(order);
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
