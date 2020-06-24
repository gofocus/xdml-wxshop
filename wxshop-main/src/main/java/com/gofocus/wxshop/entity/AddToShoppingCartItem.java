package com.gofocus.wxshop.entity;

/**
 * @Author: GoFocus
 * @Date: 2020-06-23 15:19
 * @Description:
 */
public class AddToShoppingCartItem {
    private Long id;
    private Integer number;

    public AddToShoppingCartItem() {
    }

    public AddToShoppingCartItem(Long id, Integer number) {
        this.id = id;
        this.number = number;
    }

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