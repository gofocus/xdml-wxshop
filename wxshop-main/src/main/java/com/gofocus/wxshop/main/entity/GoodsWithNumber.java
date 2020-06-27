package com.gofocus.wxshop.main.entity;

import com.gofocus.wxshop.main.generate.Goods;

/**
 * @Author: GoFocus
 * @Date: 2020-06-22 19:05
 * @Description:
 */
public class GoodsWithNumber extends Goods {

    private Integer number;

    public GoodsWithNumber() {
    }

    public GoodsWithNumber(Goods goods, Integer number) {
        this.setNumber(number);
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
