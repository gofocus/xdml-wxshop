package com.gofocus.wxshop.main.service;

import com.gofocus.wxshop.api.DataStatus;
import com.gofocus.wxshop.api.data.GoodsInfo;
import com.gofocus.wxshop.api.data.OrderInfo;
import com.gofocus.wxshop.main.dao.GoodsDao;
import com.gofocus.wxshop.main.entity.PaginationResponse;
import com.gofocus.wxshop.main.exception.HttpException;
import com.gofocus.wxshop.main.generate.*;
import com.gofocus.wxshop.main.shiro.UserContext;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final ShopService shopService;
    private final GoodsDao goodsDao;
    private final SqlSessionFactory sqlsessionFactory;

    public GoodsService(GoodsMapper goodsMapper, ShopService shopService, GoodsDao goodsDao, SqlSessionFactory sqlsessionFactory) {
        this.goodsMapper = goodsMapper;
        this.shopService = shopService;
        this.goodsDao = goodsDao;
        this.sqlsessionFactory = sqlsessionFactory;
    }

    public Goods createGoods(Goods goods) {
        Shop shop = shopService.getShopById(goods.getShopId());

        if (Objects.equals(shop.getOwnerUserId(), UserContext.getCurrentUser().getId())) {
            goodsMapper.insertSelective(goods);
            return goods;
        } else {
            throw HttpException.forbidden("没有修改店铺的商品的权限");
        }
    }

    public Goods deleteGoodsById(Long goodsId) {
        Goods goods = goodsMapper.selectByPrimaryKey(goodsId);
        if (goods == null) {
            throw HttpException.notFound("商品不存在");
        } else {
            Shop shop = shopService.getShopById(goods.getShopId());

            if (shop != null && !Objects.equals(shop.getOwnerUserId(), UserContext.getCurrentUser().getId())) {
                throw HttpException.forbidden("没有操作该商品的权限");
            } else {
                goods.setStatus(DataStatus.DELETED.getName());
                goodsMapper.updateByPrimaryKeySelective(goods);
                return goodsMapper.selectByPrimaryKey(goodsId);
            }
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
        criteria.andStatusEqualTo(DataStatus.OK.getName());
        if (shopId != null) {
            Shop shop = shopService.getShopById(shopId);
            if (shop != null) {
                criteria.andShopIdEqualTo(shopId);
            }
        }
        return goodsMapper.countByExample(example);
    }

    public Goods updateGoods(Long goodsId, Goods goods) {
        if (goodsMapper.selectByPrimaryKey(goodsId) == null) {
            throw HttpException.notFound("商品未找到");
        } else if (!currentUserHasGoodsPermission(goodsId)) {
            throw HttpException.forbidden("没有权限");
        } else {
            goods.setId(goodsId);
            goodsMapper.updateByPrimaryKeySelective(goods);
            return goods;
        }
    }

    public boolean currentUserHasGoodsPermission(Long goodsId) {
        User currentUser = UserContext.getCurrentUser();
        Long userId = currentUser.getId();
        Goods goods = goodsMapper.selectByPrimaryKey(goodsId);
        Shop shop = shopService.getShopById(goods.getShopId());
        Long ownerUserId = shop.getOwnerUserId();
        return userId.equals(ownerUserId);
    }

    @Transactional
    public void deductGoods(OrderInfo orderInfo) {
        for (GoodsInfo goodsInfo : orderInfo.getGoods()) {
            if (goodsDao.deductGoods(goodsInfo) <= 0) {
                System.out.println("扣减库存失败");
                throw HttpException.gone("库存不足");
            }
        }
    }

}
