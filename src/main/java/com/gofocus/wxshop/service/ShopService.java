package com.gofocus.wxshop.service;

import com.gofocus.wxshop.entity.Shop;
import com.gofocus.wxshop.exception.ShopNotFoundException;
import com.gofocus.wxshop.mapper.ShopMapper;
import org.springframework.stereotype.Service;

/**
 * @Author: GoFocus
 * @Date: 2020-06-20 9:31
 * @Description:
 */

@Service
public class ShopService {

    private final ShopMapper shopMapper;

    public ShopService(ShopMapper shopMapper) {
        this.shopMapper = shopMapper;
    }

    public Shop getShopById(Long shopId) {
        Shop shop = shopMapper.selectByPrimaryKey(shopId);
        if (shop == null) {
            throw new ShopNotFoundException("店铺不存在");
        } else {
            return shop;
        }
    }

}
