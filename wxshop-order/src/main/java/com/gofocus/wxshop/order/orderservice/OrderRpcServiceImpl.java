package com.gofocus.wxshop.order.orderservice;

import com.gofocus.wxshop.api.DataStatus;
import com.gofocus.wxshop.api.data.GoodsInfo;
import com.gofocus.wxshop.api.data.OrderInfo;
import com.gofocus.wxshop.api.data.PaginationResponse;
import com.gofocus.wxshop.api.data.RpcOrderGoods;
import com.gofocus.wxshop.api.exception.HttpException;
import com.gofocus.wxshop.api.generate.Order;
import com.gofocus.wxshop.api.generate.OrderExample;
import com.gofocus.wxshop.api.generate.OrderGoods;
import com.gofocus.wxshop.api.generate.OrderGoodsExample;
import com.gofocus.wxshop.api.rpc.OrderRpcService;
import com.gofocus.wxshop.api.utils.PageUtil;
import com.gofocus.wxshop.order.dao.OrderDao;
import com.gofocus.wxshop.order.dao.OrderGoodsDao;
import com.gofocus.wxshop.order.generate.OrderGoodsMapper;
import com.gofocus.wxshop.order.generate.OrderMapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.BooleanSupplier;
import java.util.stream.Collectors;

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
    private final OrderDao orderDao;

    public OrderRpcServiceImpl(OrderMapper orderMapper, OrderGoodsDao orderGoodsDao, OrderGoodsMapper orderGoodsMapper, OrderDao orderDao) {
        this.orderMapper = orderMapper;
        this.orderGoodsDao = orderGoodsDao;
        this.orderGoodsMapper = orderGoodsMapper;
        this.orderDao = orderDao;
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
        return getRpcOrderGoods(orderId);
    }

    private RpcOrderGoods getRpcOrderGoods(long orderId) {
        Order order = orderMapper.selectByPrimaryKey(orderId);
        if (order == null) {
            throw HttpException.notFound("订单未找到:" + orderId);
        }
        List<GoodsInfo> goodsInfos = getGoodsInfo(orderId);
        return new RpcOrderGoods(order, goodsInfos);
    }

    @Override
    public PaginationResponse<RpcOrderGoods> getOrder(int pageNum, int pageSize, String status, Long userId) {
        OrderExample example = new OrderExample();
        OrderExample.Criteria criteria = example.createCriteria();
        criteriaAndStatus(status, criteria);
        criteria.andUserIdEqualTo(userId);
        int totalPage = PageUtil.getTotalPage((int) orderMapper.countByExample(example), pageSize);

        pageNum = Math.min(totalPage, pageNum);
        if (pageNum <= 0) {
            pageNum = 1;
        }
        example.setLimit(pageSize);
        example.setOffset((pageNum - 1) * pageSize);
        List<Order> orders = orderMapper.selectByExample(example);

        List<OrderGoods> orderGoodsList = getOrderGoodsList(orders);
        Map<Long, List<OrderGoods>> id2OrderGoodsList = getId2OrderGoodsList(orderGoodsList);

        List<RpcOrderGoods> rpcOrderGoodsList = orders.stream()
                .map(order -> getRpcOrderGoods(order, id2OrderGoodsList))
                .collect(toList());

        return PaginationResponse.pageData(pageNum, pageSize, totalPage, rpcOrderGoodsList);
    }

    @Override
    public RpcOrderGoods updateOrder(Order order, Long userId) {
        Order orderInDb = getOrderByOrderId(order.getId(), userId);
        String status = order.getStatus();
        if (status != null && DataStatus.includeStatus(status)) {
            orderInDb.setStatus(status);
        }
        orderInDb.setUpdatedAt(new Date());
        orderInDb.setExpressCompany(order.getExpressCompany());
        orderInDb.setExpressId(order.getExpressId());
        orderMapper.updateByPrimaryKeySelective(orderInDb);

        List<GoodsInfo> goodsInfo = getGoodsInfo(order.getId());
        return new RpcOrderGoods(order, goodsInfo);
    }

    private Order getOrderByOrderId(long orderId, Long userId) {
        Order order = orderMapper.selectByPrimaryKey(orderId);
        if (order == null) {
            throw HttpException.notFound("订单未找到：" + orderId);
        }
        if (!order.getUserId().equals(userId)) {
            throw HttpException.forbidden("这是别人的订单");
        }
        return order;
    }

    private RpcOrderGoods getRpcOrderGoods(Order order, Map<Long, List<OrderGoods>> id2OrderGoodsList) {
        List<OrderGoods> orderGoods = id2OrderGoodsList.get(order.getId());
        List<GoodsInfo> goodsInfos = getGoodsInfo(orderGoods);
        return new RpcOrderGoods(order, goodsInfos);
    }

    private Map<Long, List<OrderGoods>> getId2OrderGoodsList(List<OrderGoods> orderGoodsList) {
        return orderGoodsList.stream().collect(Collectors.groupingBy(OrderGoods::getOrderId, toList()));
    }

    private List<GoodsInfo> getGoodsInfo(List<OrderGoods> orderGoodsList) {
        return orderGoodsList.stream().map(orderGoods -> {
            Long goodsId = orderGoods.getGoodsId();
            Long goodsNumber = orderGoods.getNumber();
            return new GoodsInfo(goodsId, Math.toIntExact(goodsNumber));
        }).collect(toList());
    }

    private List<OrderGoods> getOrderGoodsList(List<Order> orders) {
        List<Long> orderIds = orders.stream().map(Order::getId).collect(toList());
        OrderGoodsExample example = new OrderGoodsExample();
        example.createCriteria()
                .andOrderIdIn(orderIds);
        return orderGoodsMapper.selectByExample(example);
    }

    private void criteriaAndStatus(String status, OrderExample.Criteria criteria) {
        if (status == null || "".equals(status)) {
            criteria.andStatusNotEqualTo(DataStatus.DELETED.getName());
        } else {
            criteria.andStatusEqualTo(status);
        }
    }

    private List<GoodsInfo> getGoodsInfo(long orderId) {
        OrderGoodsExample example = new OrderGoodsExample();
        example.createCriteria().andOrderIdEqualTo(orderId);
        return orderGoodsMapper.selectByExample(example)
                .stream()
                .map(orderGood -> new GoodsInfo(orderGood.getGoodsId(), Math.toIntExact(orderGood.getNumber())))
                .collect(toList());
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
