package com.gofocus.wxshop.order.dao;

import com.gofocus.wxshop.api.generate.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: GoFocus
 * @Date: 2020-06-28 15:40
 * @Description:
 */

@Mapper
public interface OrderDao {

    List<Order> getOrders(@Param("offset") int offset,
                          @Param("limit") int limit,
                          @Param("userId") long userId,
                          @Param("status") String status);

}
