package com.gofocus.wxshop.main.dao;

import com.gofocus.wxshop.api.data.GoodsInfo;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author: GoFocus
 * @Date: 2020-06-27 9:44
 * @Description:
 */

@Mapper
public interface GoodsDao {

    int deductGoods(GoodsInfo goodsInfo);

}
