package com.gofocus.wxshop.dao;

import com.gofocus.wxshop.entity.ShoppingCartData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: GoFocus
 * @Date: 2020-06-22 21:40
 * @Description:
 */

@Mapper
public interface ShoppingCartDao {

    List<ShoppingCartData> selectShoppingCartDataByUserId(
            @Param("userId") Long userId,
            @Param("status") String status,
            @Param("offSet") Integer offSet,
            @Param("limit") Integer limit
    );

    Integer countHowManyShopInUserShoppingCart(Long userId);


}
