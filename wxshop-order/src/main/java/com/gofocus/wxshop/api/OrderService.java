package com.gofocus.wxshop.api;

import org.springframework.stereotype.Service;

/**
 * @Author: GoFocus
 * @Date: 2020-06-24 23:46
 * @Description:
 */
@Service
public interface OrderService {

    void placeOrder(int goodsId, int number);

}
