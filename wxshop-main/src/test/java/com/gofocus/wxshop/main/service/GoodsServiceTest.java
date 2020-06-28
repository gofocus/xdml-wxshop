package com.gofocus.wxshop.main.service;

import com.gofocus.wxshop.api.HttpException;
import com.gofocus.wxshop.main.generate.Goods;
import com.gofocus.wxshop.main.generate.GoodsMapper;
import com.gofocus.wxshop.main.generate.Shop;
import com.gofocus.wxshop.main.generate.User;
import com.gofocus.wxshop.main.shiro.UserContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @Author: GoFocus
 * @Date: 2020-06-20 21:18
 * @Description:
 */

@ExtendWith(MockitoExtension.class)
class GoodsServiceTest {

    @Mock
    private GoodsMapper goodsMapper;
    @Mock
    private ShopService shopService;
    @Mock
    private Shop shop;
    @Mock
    private Goods goods;
    @InjectMocks
    private GoodsService goodsService;
    @InjectMocks
    private UserContext userContext;

    @BeforeEach
    void init() {
        User user = new User();
        user.setId(1L);
        UserContext.setCurrentUser(user);
    }

    @Test
    void createGoodsSucceedIfUserIsOwner() {
        when(shopService.getShopById(Mockito.anyLong())).thenReturn(shop);
        when(shop.getOwnerUserId()).thenReturn(1L);
        when(goodsMapper.insertSelective(goods)).thenReturn(1);

        goodsService.createGoods(goods);
        Goods goods = goodsService.createGoods(this.goods);
        goods.setId(1L);
        assertEquals(0L, goods.getId());

    }

    @Test
    void createGoodsFailsIfUserIsNotOwner() {
        when(shopService.getShopById(Mockito.anyLong())).thenReturn(shop);
        HttpException httpException = assertThrows(HttpException.class, () -> goodsService.createGoods(goods));
        assertEquals(HttpStatus.FORBIDDEN.value(), httpException.getStatusCode());
    }

    @Test
    void deleteGoodsFailsIfGoodsNotExists() {
        Long goodsToDelete = 123L;
        when(goodsMapper.selectByPrimaryKey(goodsToDelete)).thenReturn(null);
        HttpException exception = assertThrows(HttpException.class, () -> goodsService.deleteGoodsById(123L));
        assertEquals(HttpStatus.NOT_FOUND.value(), exception.getStatusCode());
    }

    @Test
    void deleteGoodsFailsIfUserIsNotOwner() {
        Long goodsToDelete = 123L;
        when(goodsMapper.selectByPrimaryKey(goodsToDelete)).thenReturn(goods);
        when(shopService.getShopById(anyLong())).thenReturn(shop);
        when(shop.getOwnerUserId()).thenReturn(233L);

        HttpException exception = assertThrows(HttpException.class, () -> goodsService.deleteGoodsById(goodsToDelete));
        assertEquals(HttpStatus.FORBIDDEN.value(), exception.getStatusCode());
    }

    @Test
    void deleteGoodsSucceed() {
        long goodsToDelete = 1L;
        when(goodsMapper.selectByPrimaryKey(goodsToDelete)).thenReturn(goods);
        when(shopService.getShopById(anyLong())).thenReturn(shop);
        when(shop.getOwnerUserId()).thenReturn(1L);

        when(goodsMapper.selectByPrimaryKey(goodsToDelete)).thenReturn(goods);
        goodsService.deleteGoodsById(goodsToDelete);
        verify(goods).setStatus("deleted");
        //        Assertions.assertEquals("deleted", goods.getStatus());

    }

    @Test
    void updateGoodsSucceed() {
/*        Long goods2Update = 1L;
        goods.setId(1L);
        goods.setShopId(1L);
        when(goodsMapper.selectByPrimaryKey(goods2Update)).thenReturn(goods);
        shop.setOwnerUserId(1L);
        when(shopService.getShopById(1L)).thenReturn(shop);
        goodsService.updateGoods(1L, goods);
        Assertions.assertEquals(goods, goodsService.updateGoods(1L,goods));*/
    }

    @Test
    void getGoodsWithPagination() {
    }

    @Test
    void countGoodsByShopId() {
    }

}
