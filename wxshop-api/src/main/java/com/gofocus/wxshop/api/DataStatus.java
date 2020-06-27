package com.gofocus.wxshop.api;

/**
 * @Author: GoFocus
 * @Date: 2020-06-19 16:18
 * @Description:
 */
public enum DataStatus {
    DELETED(),
    OK(),

    // only for order
    PENDING(),
    PAID(),
    DELIVERED(),
    RECEIVED();

    public String getName() {
        return name().toLowerCase();
    }

}
