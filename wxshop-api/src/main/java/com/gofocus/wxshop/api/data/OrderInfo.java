package com.gofocus.wxshop.api.data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: GoFocus
 * @Date: 2020-06-26 17:02
 * @Description:
 */
public class OrderInfo implements Serializable {
    private List<GoodsInfo> goods;

    public List<GoodsInfo> getGoods() {
        return goods;
    }

    public void setGoods(List<GoodsInfo> goods) {
        this.goods = goods;
    }
}
