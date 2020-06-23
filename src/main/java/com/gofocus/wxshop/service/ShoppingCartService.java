package com.gofocus.wxshop.service;

import com.gofocus.wxshop.dao.ShoppingCartDao;
import com.gofocus.wxshop.entity.DataStatus;
import com.gofocus.wxshop.entity.PaginationResponse;
import com.gofocus.wxshop.entity.ShoppingCartData;
import com.gofocus.wxshop.entity.ShoppingCartGoods;
import com.gofocus.wxshop.utils.PageUtil;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * @Author: GoFocus
 * @Date: 2020-06-22 19:07
 * @Description:
 */

@Service
public class ShoppingCartService {

    private final ShoppingCartDao shoppingCartDao;

    public ShoppingCartService(ShoppingCartDao shoppingCartDao) {
        this.shoppingCartDao = shoppingCartDao;
    }

    public PaginationResponse<ShoppingCartData> selectShoppingCartDataByUserId(Long userId, Integer pageNum, Integer pageSize) {
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
        List<ShoppingCartGoods> goods = goodsOfSameShop.stream()
                .map(ShoppingCartData::getGoods)
                .flatMap(Collection::stream)
                .collect(toList());
        shoppingCartData.setGoods(goods);
        return shoppingCartData;
    }


}
