package com.gofocus.wxshop.main.service;

import com.gofocus.wxshop.api.DataStatus;
import com.gofocus.wxshop.api.data.PaginationResponse;
import com.gofocus.wxshop.api.exception.HttpException;
import com.gofocus.wxshop.main.generate.Shop;
import com.gofocus.wxshop.main.generate.ShopExample;
import com.gofocus.wxshop.main.generate.ShopMapper;
import com.gofocus.wxshop.main.generate.User;
import com.gofocus.wxshop.main.shiro.UserContext;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import static com.gofocus.wxshop.api.utils.PageUtil.getTotalPage;

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
            throw HttpException.notFound("店铺不存在");
        } else {
            return shop;
        }
    }

    public PaginationResponse<Shop> getShopsWithPagination(Integer pageNum, Integer pageSize) {
        User currentUser = UserContext.getCurrentUser();
        ShopExample example = new ShopExample();
        example.createCriteria()
                .andOwnerUserIdEqualTo(currentUser.getId())
                .andStatusEqualTo(DataStatus.OK.getName());
        Integer shopCount = Math.toIntExact(shopMapper.countByExample(example));
        example.setOffset((pageNum - 1) * pageSize);
        example.setLimit(pageSize);
        List<Shop> shops = shopMapper.selectByExample(example);
        Integer totalPage = getTotalPage(shopCount, pageSize);
        return PaginationResponse.pageData(pageNum, pageSize, totalPage, shops);
    }


    public Shop createShop(Shop shop) {
        Long userId = UserContext.getCurrentUser().getId();
        shop.setOwnerUserId(userId);
        shop.setStatus(DataStatus.OK.getName());
        shopMapper.insertSelective(shop);
        return shopMapper.selectByPrimaryKey(shop.getId());
    }

    public Shop updateShop(Shop shop) {
        assert (currentUserIsShopOwner(shop.getId()));
        shopMapper.updateByPrimaryKey(shop);
        return shop;
    }

    public Shop deleteShop(Shop shop) {
        shop.setStatus(DataStatus.DELETED.getName());
        shop.setUpdatedAt(new Date());
        return shop;
//        shopMapper.updateByPrimaryKeySelective(shop);
//        return getShopById(shop.getId());
    }

    private boolean currentUserIsShopOwner(Long shopId) {
        Shop shopInDb = shopMapper.selectByPrimaryKey(shopId);
        if (shopInDb == null) {
            throw HttpException.notFound("店铺未找到");
        }
        if (!Objects.equals(shopInDb.getOwnerUserId(), UserContext.getCurrentUser().getId())) {
            throw HttpException.forbidden("无权操作指定店铺");
        }
        return true;
    }
}
