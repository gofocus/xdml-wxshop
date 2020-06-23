package com.gofocus.wxshop.controller;

import com.gofocus.wxshop.entity.Goods;
import com.gofocus.wxshop.entity.PaginationResponse;
import com.gofocus.wxshop.entity.Response;
import com.gofocus.wxshop.entity.ShoppingCartData;
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
    public Response<Goods> addToShoppingCart(@RequestBody AddToShoppingCartRequest addToShoppingCartRequest) {
        return null;
    }

    @GetMapping
    public PaginationResponse<ShoppingCartData> getGoods(
            @RequestParam("pageNum") Integer pageNum,
            @RequestParam("pageSize") Integer pageSize
    ) {

        return shoppingCartService.selectShoppingCartDataByUserId(UserContext.getCurrentUser().getId(), pageNum, pageSize);
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

    public static class AddToShoppingCartItem {
        private Long id;
        private Integer number;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public Integer getNumber() {
            return number;
        }

        public void setNumber(Integer number) {
            this.number = number;
        }
    }


}
