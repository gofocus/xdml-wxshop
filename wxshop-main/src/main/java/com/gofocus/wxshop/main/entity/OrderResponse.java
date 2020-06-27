package com.gofocus.wxshop.main.entity;

import com.gofocus.wxshop.api.generate.Order;
import com.gofocus.wxshop.main.generate.Shop;

import java.util.List;

/**
 * @Author: GoFocus
 * @Date: 2020-06-26 23:13
 * @Description:
 */
public class OrderResponse extends Order {
    private Shop shop;
    private List<GoodsWithNumber> goods;

    public static OrderResponse of(Order order, Shop shop, List<GoodsWithNumber> goods) {
        OrderResponse response = new OrderResponse(order);
        response.setShop(shop);
        response.setGoods(goods);
        return response;
    }

    public OrderResponse() {

    }

    public OrderResponse(Order order) {
        this.setId(order.getId());
        this.setUserId(order.getUserId());
        this.setTotalPrice(order.getTotalPrice());
        this.setAddress(order.getAddress());
        this.setExpressCompany(order.getExpressCompany());
        this.setExpressId(order.getExpressId());
        this.setStatus(order.getStatus());
        this.setCreatedAt(order.getCreatedAt());
        this.setUpdatedAt(order.getUpdatedAt());
        this.setShopId(order.getShopId());
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public List<GoodsWithNumber> getGoods() {
        return goods;
    }

    public void setGoods(List<GoodsWithNumber> goods) {
        this.goods = goods;
    }
}
