package com.gofocus.wxshop.main.dao;

import com.gofocus.wxshop.main.entity.ShoppingCartData;
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


    List<ShoppingCartData> getShoppingCartDataByUserIdAndShopId(Long userId, Long shopId);

    void deleteGoods(@Param("goodsId") Long goodsId,
                     @Param("userId") Long userId);
}
