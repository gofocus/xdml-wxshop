package com.gofocus.wxshop.entity;

/**
 * @Author: GoFocus
 * @Date: 2020-06-19 16:18
 * @Description:
 */
public enum DataStatus {
    DELETED(),
    OK();

    public String getName() {
        return name().toLowerCase();
    }

}
