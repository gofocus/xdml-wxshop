package com.gofocus.wxshop.api.data;

import java.io.Serializable;

/**
 * @Author: GoFocus
 * @Date: 2020-06-26 17:02
 * @Description:
 */
public class GoodsInfo implements Serializable {
    private long id;
    private int number;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
