package com.gofocus.wxshop.main.controller;

import com.gofocus.wxshop.main.entity.Response;
import com.gofocus.wxshop.main.exception.HttpException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

/**
 * @Author: GoFocus
 * @Date: 2020-06-27 22:50
 * @Description:
 */

@ControllerAdvice
public class ExceptionHandlerController {

    @ResponseBody
    @ExceptionHandler(HttpException.class)
    public Response<?> onError(HttpServletResponse response, HttpException e) {
        response.setStatus(e.getStatusCode());
        return Response.failure(e.getMessage());
    }
}
