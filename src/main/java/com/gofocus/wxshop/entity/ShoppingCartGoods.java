package com.gofocus.wxshop.entity;

/**
 * @Author: GoFocus
 * @Date: 2020-06-22 19:05
 * @Description:
 */
public class ShoppingCartGoods extends Goods {

    private Integer number;

    public ShoppingCartGoods() {
    }

    public ShoppingCartGoods(Goods goods) {
        this.setId(goods.getId());
        this.setShopId(goods.getShopId());
        this.setName(goods.getName());
        this.setDescription(goods.getDescription());
        this.setImgUrl(goods.getImgUrl());
        this.setPrice(goods.getPrice());
        this.setStock(goods.getStock());
        this.setStatus(goods.getStatus());
        this.setCreatedAt(goods.getCreatedAt());
        this.setUpdatedAt(goods.getUpdatedAt());
        this.setDetails(goods.getDetails());
    }


    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }
}
