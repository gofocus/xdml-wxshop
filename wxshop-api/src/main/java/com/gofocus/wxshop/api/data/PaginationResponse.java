package com.gofocus.wxshop.api.data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: GoFocus
 * @Date: 2020-06-20 15:48
 * @Description:
 */
public class PaginationResponse<T> implements Serializable {

    private Integer pageNum;
    private Integer pageSize;
    private Integer totalPage;
    private List<T> data;

    public static <T> PaginationResponse<T> pageData(Integer pageNum, Integer pageSize, Integer totalPage, List<T> data) {
        return new PaginationResponse<>(pageNum, pageSize, totalPage, data);
    }

    public PaginationResponse() {
    }

    public PaginationResponse(Integer pageNum, Integer pageSize, Integer totalPage, List<T> data) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.totalPage = totalPage;
        this.data = data;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public Integer getTotalPage() {
        return totalPage;
    }

    public List<T> getData() {
        return data;
    }
}
