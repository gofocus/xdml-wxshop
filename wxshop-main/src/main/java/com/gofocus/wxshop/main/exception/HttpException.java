package com.gofocus.wxshop.main.exception;

import static javax.servlet.http.HttpServletResponse.*;

/**
 * @Author: GoFocus
 * @Date: 2020-06-22 9:57
 * @Description:
 */
public class HttpException extends RuntimeException {

    private int statusCode;
    private String message;

    public static HttpException forbidden(String message) {
        return new HttpException(SC_FORBIDDEN, message);
    }

    public static HttpException notFound(String message) {
        return new HttpException(SC_NOT_FOUND, message);
    }

    private HttpException(int statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }

    public static HttpException badRequest(String message) {
        return new HttpException(SC_BAD_REQUEST, message);
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
