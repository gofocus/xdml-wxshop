package com.gofocus.wxshop.order.dao;

import com.gofocus.wxshop.api.data.OrderInfo;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author: GoFocus
 * @Date: 2020-06-26 22:05
 * @Description:
 */

@Mapper
public interface OrderGoodsDao {

    void insertOrderGoods(OrderInfo orderInfo);

}
