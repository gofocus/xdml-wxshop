package com.gofocus.wxshop.exception;

/**
 * @Author: GoFocus
 * @Date: 2020-06-19 16:08
 * @Description:
 */
public class GoodsNotFoundException extends RuntimeException {
    private String message;

    public GoodsNotFoundException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
