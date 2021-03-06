package com.gofocus.wxshop.main.service;

import com.gofocus.wxshop.api.data.GoodsInfo;
import com.gofocus.wxshop.api.data.OrderInfo;
import com.gofocus.wxshop.api.data.PaginationResponse;
import com.gofocus.wxshop.api.data.RpcOrderGoods;
import com.gofocus.wxshop.api.exception.HttpException;
import com.gofocus.wxshop.api.generate.Order;
import com.gofocus.wxshop.api.rpc.OrderRpcService;
import com.gofocus.wxshop.main.entity.GoodsWithNumber;
import com.gofocus.wxshop.main.entity.OrderResponse;
import com.gofocus.wxshop.main.generate.*;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.gofocus.wxshop.api.DataStatus.PENDING;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

/**
 * @Author: GoFocus
 * @Date: 2020-06-26 18:37
 * @Description:
 */

@Service
public class OrderService {

    private final GoodsMapper goodsMapper;
    private final UserMapper userMapper;
    private final ShopMapper shopMapper;
    private final GoodsService goodsService;

    @DubboReference(version = "${wxshop.orderservice.version}")
    private OrderRpcService orderRpcService;

    public OrderService(GoodsMapper goodsMapper, UserMapper userMapper, ShopMapper shopMapper, GoodsService goodsService) {
        this.goodsMapper = goodsMapper;
        this.userMapper = userMapper;
        this.shopMapper = shopMapper;
        this.goodsService = goodsService;
    }

    public OrderResponse placeOrder(OrderInfo orderInfo, Long userId) {
        Order order = createOrder(userId);
        goodsService.deductGoods(orderInfo);
        Map<Long, GoodsWithNumber> id2GoodsWithNumberMap = id2GoodsWithNumberMap(orderInfo);
        order.setTotalPrice(calculateTotalPrice(id2GoodsWithNumberMap));
        Order createdOrder = orderRpcService.placeOrder(orderInfo, order);
        Long shopId = id2GoodsWithNumberMap.values().stream().findFirst().orElseThrow(() -> HttpException.badRequest("参数不合法")).getShopId();
        order.setShopId(shopId);
        Shop shop = shopMapper.selectByPrimaryKey(shopId);
        return OrderResponse.of(createdOrder, shop, new ArrayList<>(id2GoodsWithNumberMap.values()));
    }

    private Long calculateTotalPrice(Map<Long, GoodsWithNumber> id2GoodsWithNumberMap) {
        long totalPrice = 0L;
        for (GoodsWithNumber goodsWithNumber : id2GoodsWithNumberMap.values()) {
            totalPrice += goodsWithNumber.getNumber() * goodsWithNumber.getPrice();
        }
        return totalPrice;
    }

    private Map<Long, GoodsWithNumber> id2GoodsWithNumberMap(OrderInfo orderInfo) {
        return orderInfo.getGoods().stream()
                .collect(toMap(GoodsInfo::getId, goodsInfo -> {
                            Goods goods = goodsMapper.selectByPrimaryKey(goodsInfo.getId());
                            if (goods == null) {
                                throw HttpException.badRequest("goods id非法：" + goodsInfo.getId());
                            }
                            int number = goodsInfo.getNumber();
                            if (number <= 0) {
                                throw HttpException.badRequest("goods number非法：" + number);
                            }
                            return new GoodsWithNumber(goods, number);
                        }
                ));
    }

    private Order createOrder(Long userId) {
        Order order = new Order();
        order.setUserId(userId);
        order.setAddress(userMapper.selectByPrimaryKey(userId).getAddress());
        order.setCreatedAt(new Date());
        order.setUpdatedAt(new Date());
        order.setStatus(PENDING.getName());
        order.setExpressCompany(null);
        order.setExpressId(null);
        return order;
    }


    public OrderResponse deleteOrder(long orderId) {
        // RPC
        RpcOrderGoods rpcOrderGoods = orderRpcService.deleteOrder(orderId);
        //组装OrderResponse
        //1.shop
        Order order = rpcOrderGoods.getOrder();
        Shop shop = shopMapper.selectByPrimaryKey(order.getShopId());
        //2.goodsWithNumber
        List<GoodsWithNumber> goodsWithNumbers = getGoodsWithNumbers(rpcOrderGoods);
        return OrderResponse.of(order, shop, goodsWithNumbers);
    }

    private List<GoodsWithNumber> getGoodsWithNumbers(RpcOrderGoods rpcOrderGoods) {
        return rpcOrderGoods.getGoods().stream().map((goodsInfo -> {
            Goods goods = goodsMapper.selectByPrimaryKey(goodsInfo.getId());
            return new GoodsWithNumber(goods, goodsInfo.getNumber());
        })).collect(toList());
    }

    public PaginationResponse<OrderResponse> getOrders(int pageNum, int pageSize, String status, Long userId) {
        PaginationResponse<RpcOrderGoods> rpcOrderGoodsWithPage = orderRpcService.getOrder(pageNum, pageSize, status, userId);

        List<OrderResponse> orderResponses = rpcOrderGoodsWithPage.getData().stream().map(rpcOrderGoods -> {
            Shop shop = shopMapper.selectByPrimaryKey(rpcOrderGoods.getOrder().getShopId());
            return OrderResponse.of(rpcOrderGoods.getOrder(), shop, getGoodsWithNumbers(rpcOrderGoods));
        }).collect(toList());

        return PaginationResponse.pageData(rpcOrderGoodsWithPage.getPageNum(), rpcOrderGoodsWithPage.getPageSize(), rpcOrderGoodsWithPage.getTotalPage(), orderResponses);

    }

    public OrderResponse updateOrder(Order order, Long userId) {
        RpcOrderGoods rpcOrderGoods = orderRpcService.updateOrder(order, userId);
        List<GoodsWithNumber> goodsWithNumbers = getGoodsWithNumbers(rpcOrderGoods);
        Order updatedOrder = rpcOrderGoods.getOrder();
        Shop shop = shopMapper.selectByPrimaryKey(updatedOrder.getShopId());
        return OrderResponse.of(updatedOrder, shop, goodsWithNumbers);
    }
}
