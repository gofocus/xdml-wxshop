package com.gofocus.wxshop.controller;

import com.gofocus.wxshop.entity.AddToShoppingCartItem;
import com.gofocus.wxshop.entity.PaginationResponse;
import com.gofocus.wxshop.entity.Response;
import com.gofocus.wxshop.entity.ShoppingCartData;
import com.gofocus.wxshop.exception.HttpException;
import com.gofocus.wxshop.service.ShoppingCartService;
import com.gofocus.wxshop.shiro.UserContext;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: GoFocus
 * @Date: 2020-06-22 16:57
 * @Description:
 */

@RestController
@RequestMapping("/api/v1/shoppingCart")
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;

    public ShoppingCartController(ShoppingCartService shoppingCartService) {
        this.shoppingCartService = shoppingCartService;
    }

    @PostMapping
    public Response<ShoppingCartData> addToShoppingCart(@RequestBody AddToShoppingCartRequest addToShoppingCartRequest) {
        try {
            Long userId = UserContext.getCurrentUser().getId();
            ShoppingCartData shoppingCartData = shoppingCartService.addToShoppingCart(addToShoppingCartRequest, userId);
            return Response.success(shoppingCartData);
        } catch (HttpException e) {
            return Response.failure(e.getMessage());
        }
    }

    @GetMapping
    public PaginationResponse<ShoppingCartData> getGoods(
            @RequestParam("pageNum") Integer pageNum,
            @RequestParam("pageSize") Integer pageSize
    ) {
        return shoppingCartService.getShoppingCartDataByUserId(
                UserContext.getCurrentUser().getId(),
                pageNum,
                pageSize);
    }

    @DeleteMapping("/{goodsId}")
    public Response<ShoppingCartData> deleteGoodsInShoppingCart(@PathVariable("goodsId") Long goodsId) {
        try {
            Long userId = UserContext.getCurrentUser().getId();
            ShoppingCartData shoppingCartData = shoppingCartService.deleteGoods(goodsId, userId);
            return Response.success(shoppingCartData);
        } catch (HttpException e) {
            return Response.failure(e.getMessage());
        }
    }

    public static class AddToShoppingCartRequest {
        List<AddToShoppingCartItem> goods;

        public List<AddToShoppingCartItem> getGoods() {
            return goods;
        }

        public void setGoods(List<AddToShoppingCartItem> goods) {
            this.goods = goods;
        }
    }


}
