package com.gofocus.wxshop.main.service;

import com.gofocus.wxshop.api.DataStatus;
import com.gofocus.wxshop.api.data.PaginationResponse;
import com.gofocus.wxshop.api.exception.HttpException;
import com.gofocus.wxshop.api.utils.PageUtil;
import com.gofocus.wxshop.main.controller.ShoppingCartController;
import com.gofocus.wxshop.main.dao.ShoppingCartDao;
import com.gofocus.wxshop.main.entity.AddToShoppingCartItem;
import com.gofocus.wxshop.main.entity.GoodsWithNumber;
import com.gofocus.wxshop.main.entity.ShoppingCartData;
import com.gofocus.wxshop.main.generate.*;
import com.gofocus.wxshop.main.shiro.UserContext;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

/**
 * @Author: GoFocus
 * @Date: 2020-06-22 19:07
 * @Description:
 */

@Service
public class ShoppingCartService {

    private final ShoppingCartDao shoppingCartDao;
    private final GoodsMapper goodsMapper;
    private final SqlSessionFactory sqlSessionFactory;

    public ShoppingCartService(ShoppingCartDao shoppingCartDao, GoodsMapper goodsMapper, SqlSessionFactory sqlSessionFactory) {
        this.shoppingCartDao = shoppingCartDao;
        this.goodsMapper = goodsMapper;
        this.sqlSessionFactory = sqlSessionFactory;
    }

    public PaginationResponse<ShoppingCartData> getShoppingCartDataByUserId(Long userId, Integer pageNum, Integer pageSize) {
        Integer offSet = (pageNum - 1) * pageSize;
        Integer shopCount = shoppingCartDao.countHowManyShopInUserShoppingCart(userId);
        Integer totalPage = PageUtil.getTotalPage(shopCount, pageSize);
        List<ShoppingCartData> list = shoppingCartDao.selectShoppingCartDataByUserId(userId, DataStatus.OK.getName(), offSet, pageSize);
        /*      .stream() .collect(Collectors.groupingBy((data) -> data.getShop().getId())) .values() .stream() .map(this::merge).collect(toList());*/
        return PaginationResponse.pageData(pageNum, pageSize, totalPage, list);
    }

    private ShoppingCartData merge(List<ShoppingCartData> goodsOfSameShop) {
        ShoppingCartData shoppingCartData = new ShoppingCartData();
        shoppingCartData.setShop(goodsOfSameShop.get(0).getShop());
        List<GoodsWithNumber> goods = goodsOfSameShop.stream()
                .map(ShoppingCartData::getGoods)
                .flatMap(Collection::stream)
                .collect(toList());
        shoppingCartData.setGoods(goods);
        return shoppingCartData;
    }

    public ShoppingCartData getShoppingCartDataByUserIdAndShopId(Long shopId, Long userId) {
        return shoppingCartDao.getShoppingCartDataByUserIdAndShopId(userId, shopId).get(0);
    }

    //    @SuppressFBWarnings("")
    public ShoppingCartData addToShoppingCart(ShoppingCartController.AddToShoppingCartRequest addToShoppingCartRequest, Long userId) {
        final List<AddToShoppingCartItem> requestGoods = addToShoppingCartRequest.getGoods();
        List<Long> goodsIdList = requestGoods
                .stream()
                .map(AddToShoppingCartItem::getId)
                .collect(toList());
        if (goodsIdList.isEmpty()) {
            throw HttpException.badRequest("商品ID为空");
        }

        GoodsExample example = new GoodsExample();
        example.createCriteria().andIdIn(goodsIdList);
        List<Goods> goods = goodsMapper.selectByExample(example);
        Long shopId = goods.get(0).getShopId();
        Map<Long, Goods> idToGoodsMap = goods.stream().collect(toMap(Goods::getId, item -> item));

        int shopIdCount = goods.stream().map(Goods::getShopId).collect(Collectors.toSet()).size();
        if (shopIdCount != 1) {
            throw HttpException.badRequest("商品ID非法！（不属于同一个shop）");
        }

        List<ShoppingCart> shoppingCartRows = requestGoods.stream().map(item -> toShoppingCartRow(item, idToGoodsMap)).collect(toList());

        try (SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH)) {
            ShoppingCartMapper mapper = sqlSession.getMapper(ShoppingCartMapper.class);
            shoppingCartRows.forEach(mapper::insertSelective);
            sqlSession.commit();
        }
        return getShoppingCartDataByUserIdAndShopId(shopId, userId);
    }

    private ShoppingCart toShoppingCartRow(AddToShoppingCartItem item, Map<Long, Goods> idToGoodsMap) {
        final ShoppingCart cart = new ShoppingCart();
        final Goods goods = idToGoodsMap.get(item.getId());
        if (goods == null) {
            return null;
        }
        cart.setShopId(goods.getShopId());
        cart.setStatus("ok");
        cart.setUserId(UserContext.getCurrentUser().getId());
        cart.setGoodsId(item.getId());
        cart.setNumber(item.getNumber());
        cart.setCreatedAt(new Date());
        cart.setUpdatedAt(new Date());
        return cart;
    }


    public ShoppingCartData deleteGoods(Long goodsId, Long userId) {
        Goods goods = goodsMapper.selectByPrimaryKey(goodsId);
        if (goods == null) {
            throw HttpException.notFound("商品未找到");
        }
        shoppingCartDao.deleteGoods(goodsId, userId);
        return getShoppingCartDataByUserIdAndShopId(goods.getShopId(), userId);
    }
}
