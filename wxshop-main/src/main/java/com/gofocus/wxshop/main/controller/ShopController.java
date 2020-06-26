package com.gofocus.wxshop.main.controller;

import com.gofocus.wxshop.main.entity.PaginationResponse;
import com.gofocus.wxshop.main.entity.Response;
import com.gofocus.wxshop.main.exception.HttpException;
import com.gofocus.wxshop.main.generate.Shop;
import com.gofocus.wxshop.main.service.ShopService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

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
        try {
            shopsWithPagination = shopService.getShopsWithPagination(pageNum, pageSize);
            return Response.success(shopsWithPagination);
        } catch (HttpException e) {
            return Response.failure(e.getMessage());
        }
    }

    @PostMapping("/shop")
    public Response<Shop> createShop(@RequestBody Shop shop) {
        try {
            Shop shopCreated = shopService.createShop(shop);
            return Response.success(shopCreated);
        } catch (HttpException e) {
            return Response.failure(e.getMessage());
        }
    }

    @PatchMapping("/shop/{id}")
    public Response<Shop> updateShop(@PathVariable("id") Long shopId, @RequestBody Shop shop, HttpServletResponse response) {
        try {
            shop.setId(shopId);
            Shop updatedShop = shopService.updateShop(shop);
            return Response.success(updatedShop);
        } catch (HttpException e) {
            response.setStatus(e.getStatusCode());
            return Response.failure(e.getMessage());
        }
    }

    @DeleteMapping("/shop/{id}")
    public Response<Shop> deleteShop(@PathVariable("id") Long shopId, @RequestBody Shop shop, HttpServletResponse response) {
        try {
            shop.setId(shopId);
            Shop shopDeleted = shopService.deleteShop(shop);
            return Response.success(shopDeleted);
        } catch (HttpException e) {
            response.setStatus(e.getStatusCode());
            return Response.failure(e.getMessage());
        }
    }
}
