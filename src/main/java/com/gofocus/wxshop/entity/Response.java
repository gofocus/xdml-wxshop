package com.gofocus.wxshop.entity;

/**
 * @Author: GoFocus
 * @Date: 2020-06-18 13:22
 * @Description:
 */
public class Response<T> {
    private T data;
    private String message;

    public static <T> Response<T> success(T data) {
        return new Response<>(data, null);
    }

    public static <T> Response<T> failure(String message) {
        return new Response<T>(null, message);
    }

    public Response() {
    }

    public Response(T data, String message) {
        this.data = data;
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

}
