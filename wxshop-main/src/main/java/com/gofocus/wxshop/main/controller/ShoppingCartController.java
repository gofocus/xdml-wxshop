package com.gofocus.wxshop.main.controller;

import com.gofocus.wxshop.main.entity.AddToShoppingCartItem;
import com.gofocus.wxshop.main.entity.PaginationResponse;
import com.gofocus.wxshop.main.entity.Response;
import com.gofocus.wxshop.main.entity.ShoppingCartData;
import com.gofocus.wxshop.main.service.ShoppingCartService;
import com.gofocus.wxshop.main.shiro.UserContext;
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
        Long userId = UserContext.getCurrentUser().getId();
        ShoppingCartData shoppingCartData = shoppingCartService.addToShoppingCart(addToShoppingCartRequest, userId);
        return Response.success(shoppingCartData);
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
        Long userId = UserContext.getCurrentUser().getId();
        ShoppingCartData shoppingCartData = shoppingCartService.deleteGoods(goodsId, userId);
        return Response.success(shoppingCartData);
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
