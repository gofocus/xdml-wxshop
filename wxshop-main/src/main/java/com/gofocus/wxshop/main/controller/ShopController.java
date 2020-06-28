package com.gofocus.wxshop.main.controller;

import com.gofocus.wxshop.api.data.PaginationResponse;
import com.gofocus.wxshop.main.entity.Response;
import com.gofocus.wxshop.main.generate.Shop;
import com.gofocus.wxshop.main.service.ShopService;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: GoFocus
 * @Date: 2020-06-22 9:08
 * @Description:
 */


@RestController
@RequestMapping("/api/v1")
public class ShopController {

    private final ShopService shopService;

    public ShopController(ShopService shopService) {
        this.shopService = shopService;
    }

    @GetMapping("/shop")
    public Response<PaginationResponse<Shop>> getShops(@RequestParam("pageNum") Integer pageNum,
                                                       @RequestParam("pageSize") Integer pageSize
    ) {
        PaginationResponse<Shop> shopsWithPagination;
        shopsWithPagination = shopService.getShopsWithPagination(pageNum, pageSize);
        return Response.success(shopsWithPagination);
    }

    @PostMapping("/shop")
    public Response<Shop> createShop(@RequestBody Shop shop) {
        Shop shopCreated = shopService.createShop(shop);
        return Response.success(shopCreated);
    }

    @PatchMapping("/shop/{id}")
    public Response<Shop> updateShop(@PathVariable("id") Long shopId, @RequestBody Shop shop) {
        shop.setId(shopId);
        Shop updatedShop = shopService.updateShop(shop);
        return Response.success(updatedShop);
    }

    @DeleteMapping("/shop/{id}")
    public Response<Shop> deleteShop(@PathVariable("id") Long shopId, @RequestBody Shop shop) {
        shop.setId(shopId);
        Shop shopDeleted = shopService.deleteShop(shop);
        return Response.success(shopDeleted);
    }
}
