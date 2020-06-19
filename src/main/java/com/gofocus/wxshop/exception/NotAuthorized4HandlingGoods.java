package com.gofocus.wxshop.exception;

/**
 * @Author: GoFocus
 * @Date: 2020-06-19 15:50
 * @Description:
 */
public class NotAuthorized4HandlingGoods extends RuntimeException {

    private String message;

    public NotAuthorized4HandlingGoods(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
