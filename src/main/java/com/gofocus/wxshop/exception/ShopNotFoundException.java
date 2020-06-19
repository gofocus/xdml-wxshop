package com.gofocus.wxshop.exception;

/**
 * @Author: GoFocus
 * @Date: 2020-06-20 9:32
 * @Description:
 */
public class ShopNotFoundException extends RuntimeException {

    private String message;


    public ShopNotFoundException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
