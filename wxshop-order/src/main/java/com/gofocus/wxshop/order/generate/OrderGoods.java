package com.gofocus.wxshop.order.generate;

import java.math.BigDecimal;

public class OrderGoods {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ORDER_GOODS.ID
     *
     * @mbg.generated Fri Jun 26 17:22:08 CST 2020
     */
    private Long id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ORDER_GOODS.GOODS_ID
     *
     * @mbg.generated Fri Jun 26 17:22:08 CST 2020
     */
    private Long goodsId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ORDER_GOODS.NUMBER
     *
     * @mbg.generated Fri Jun 26 17:22:08 CST 2020
     */
    private BigDecimal number;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ORDER_GOODS.ID
     *
     * @return the value of ORDER_GOODS.ID
     *
     * @mbg.generated Fri Jun 26 17:22:08 CST 2020
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ORDER_GOODS.ID
     *
     * @param id the value for ORDER_GOODS.ID
     *
     * @mbg.generated Fri Jun 26 17:22:08 CST 2020
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ORDER_GOODS.GOODS_ID
     *
     * @return the value of ORDER_GOODS.GOODS_ID
     *
     * @mbg.generated Fri Jun 26 17:22:08 CST 2020
     */
    public Long getGoodsId() {
        return goodsId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ORDER_GOODS.GOODS_ID
     *
     * @param goodsId the value for ORDER_GOODS.GOODS_ID
     *
     * @mbg.generated Fri Jun 26 17:22:08 CST 2020
     */
    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ORDER_GOODS.NUMBER
     *
     * @return the value of ORDER_GOODS.NUMBER
     *
     * @mbg.generated Fri Jun 26 17:22:08 CST 2020
     */
    public BigDecimal getNumber() {
        return number;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ORDER_GOODS.NUMBER
     *
     * @param number the value for ORDER_GOODS.NUMBER
     *
     * @mbg.generated Fri Jun 26 17:22:08 CST 2020
     */
    public void setNumber(BigDecimal number) {
        this.number = number;
    }
}