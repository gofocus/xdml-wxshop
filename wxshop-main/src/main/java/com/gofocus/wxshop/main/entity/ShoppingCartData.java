package com.gofocus.wxshop.main.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gofocus.wxshop.main.generate.Shop;

import java.util.List;

/**
 * @Author: GoFocus
 * @Date: 2020-06-22 19:01
 * @Description:
 */
public class ShoppingCartData {
    @JsonProperty("shop")
    private Shop shop;
    @JsonProperty("goods")
    private List<ShoppingCartGoods> goods;

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public List<ShoppingCartGoods> getGoods() {
        return goods;
    }

    public void setGoods(List<ShoppingCartGoods> goods) {
        this.goods = goods;
    }
}
