package com.gofocus.wxshop.utils;

/**
 * @Author: GoFocus
 * @Date: 2020-06-22 9:34
 * @Description:
 */
public class PageUtil {

    public static int getTotalPage(Integer shopCount, Integer pageSize) {
        return (shopCount + pageSize - 1) / pageSize;
    }
}
