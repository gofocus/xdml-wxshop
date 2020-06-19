package com.gofocus.wxshop.controller;

import com.gofocus.wxshop.entity.Goods;
import com.gofocus.wxshop.entity.GoodsStatus;
import com.gofocus.wxshop.entity.PaginationResponse;
import com.gofocus.wxshop.entity.Response;
import com.gofocus.wxshop.exception.GoodsNotFoundException;
import com.gofocus.wxshop.exception.NotAuthorized4HandlingGoods;
import com.gofocus.wxshop.service.GoodsService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;

import static javax.servlet.http.HttpServletResponse.SC_CREATED;
import static javax.servlet.http.HttpServletResponse.SC_FORBIDDEN;

/**
 * @Author: GoFocus
 * @Date: 2020-06-17 22:15
 * @Description:
 */

@RestController
@RequestMapping("/api/v1")
public class GoodsController {

    private final GoodsService goodsService;

    public GoodsController(GoodsService goodsService) {
        this.goodsService = goodsService;
    }

    @PostMapping("/goods")
    public Response<Goods> createGoods(@RequestBody Goods goods, HttpServletResponse response) {
        cleanGoodsParam(goods);
        try {
            response.setStatus(SC_CREATED);
            return Response.success(goodsService.createGoods(goods));
        } catch (Exception e) {
            response.setStatus(SC_FORBIDDEN);
            return Response.failure(e.getMessage());
        }
    }

    private void cleanGoodsParam(Goods goods) {
        goods.setId(null);
        goods.setStatus(GoodsStatus.OK.getName());
        goods.setCreatedAt(new Date());
        goods.setUpdatedAt(new Date());
    }

    @DeleteMapping("/goods/{id}")
    public Response<Goods> deleteGoods(@PathVariable("id") Long goodsId, HttpServletResponse response) {
        try {
            return Response.success(goodsService.deleteGoodsById(goodsId));
        } catch (NotAuthorized4HandlingGoods e) {
            response.setStatus(SC_FORBIDDEN);
            return Response.failure(e.getMessage());
        } catch (GoodsNotFoundException e) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return Response.failure(e.getMessage());
        }
    }

    @GetMapping("/goods")
    public PaginationResponse<Goods> getGoods(@RequestParam("pageNum") Integer pageNum,
                                              @RequestParam("pageSize") Integer pageSize,
                                              @RequestParam(name = "shopId", required = false) Long shopId) {

        PaginationResponse<Goods> goods = goodsService.getGoodsWithPagination(pageNum, pageSize, shopId);

        return goods;
    }
}
