package com.gofocus.wxshop.service;

import com.gofocus.wxshop.entity.*;
import com.gofocus.wxshop.exception.GoodsNotFoundException;
import com.gofocus.wxshop.exception.NotAuthorized4HandlingGoods;
import com.gofocus.wxshop.mapper.GoodsMapper;
import com.gofocus.wxshop.mapper.ShopMapper;
import com.gofocus.wxshop.shiro.UserContext;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @Author: GoFocus
 * @Date: 2020-06-18 13:04
 * @Description:
 */

@Service
public class GoodsService {

    private final GoodsMapper goodsMapper;
    private final ShopMapper shopMapper;
    private final ShopService shopService;

    public GoodsService(GoodsMapper goodsMapper, ShopMapper shopMapper, ShopService shopService) {
        this.goodsMapper = goodsMapper;
        this.shopMapper = shopMapper;
        this.shopService = shopService;
    }

    public Goods createGoods(Goods goods) {
        Shop shop = shopService.getShopById(goods.getShopId());

        if (Objects.equals(shop.getOwnerUserId(), UserContext.getCurrentUser().getId())) {
            goodsMapper.insertSelective(goods);
            return goods;
        } else {
            throw new NotAuthorized4HandlingGoods("没有修改店铺的商品的权限");
        }
    }

    public Goods deleteGoodsById(Long goodsId) {
        Goods goods = goodsMapper.selectByPrimaryKey(goodsId);
        if (goods == null) {
            throw new GoodsNotFoundException("商品不存在");
        } else if (!Objects.equals(goods.getShopId(), UserContext.getCurrentUser().getId())) {
            throw new NotAuthorized4HandlingGoods("没有操作该商品的权限");
        } else {
            Goods goods2Delete = new Goods();
            goods2Delete.setId(goodsId);
            goods2Delete.setStatus(GoodsStatus.DELETED.getName());
            goodsMapper.updateByPrimaryKeySelective(goods2Delete);
            return goodsMapper.selectByPrimaryKey(goodsId);
        }
    }

    public PaginationResponse<Goods> getGoodsWithPagination(Integer pageNum, Integer pageSize, Long shopId) {
        Long goodsCount = countGoodsByShopId(shopId);
        Integer totalPage = Math.toIntExact((goodsCount + pageSize - 1) / pageSize);
        pageNum = pageNum.compareTo(1) < 0 ? Integer.valueOf(1) : pageNum;
        pageNum = pageNum > totalPage ? totalPage : pageNum;

        GoodsExample goodsExample = new GoodsExample();
        goodsExample.setOffset((pageNum - 1) * pageSize);
        goodsExample.setLimit(pageSize);
        if (shopId != null) {
            goodsExample.createCriteria().andShopIdEqualTo(shopId);
        }

        List<Goods> goods = goodsMapper.selectByExample(goodsExample);
        return PaginationResponse.pageData(pageNum, pageSize, totalPage, goods);
    }

    public Long countGoodsByShopId(Long shopId) {
        GoodsExample example = new GoodsExample();
        GoodsExample.Criteria criteria = example.createCriteria();
        criteria.andStatusEqualTo(GoodsStatus.OK.getName());
        if (shopId != null) {
            Shop shop = shopService.getShopById(shopId);
            if (shop != null) {
                criteria.andShopIdEqualTo(shopId);
            }
        }
        return goodsMapper.countByExample(example);
    }
}
