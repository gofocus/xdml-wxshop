package com.gofocus.wxshop.order.generate;

import com.gofocus.wxshop.api.generate.Order;
import com.gofocus.wxshop.api.generate.OrderExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OrderMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ORDER_TABLE
     *
     * @mbg.generated Sun Jun 28 13:29:32 CST 2020
     */
    long countByExample(OrderExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ORDER_TABLE
     *
     * @mbg.generated Sun Jun 28 13:29:32 CST 2020
     */
    int deleteByExample(OrderExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ORDER_TABLE
     *
     * @mbg.generated Sun Jun 28 13:29:32 CST 2020
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ORDER_TABLE
     *
     * @mbg.generated Sun Jun 28 13:29:32 CST 2020
     */
    int insert(Order record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ORDER_TABLE
     *
     * @mbg.generated Sun Jun 28 13:29:32 CST 2020
     */
    int insertSelective(Order record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ORDER_TABLE
     *
     * @mbg.generated Sun Jun 28 13:29:32 CST 2020
     */
    List<Order> selectByExample(OrderExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ORDER_TABLE
     *
     * @mbg.generated Sun Jun 28 13:29:32 CST 2020
     */
    Order selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ORDER_TABLE
     *
     * @mbg.generated Sun Jun 28 13:29:32 CST 2020
     */
    int updateByExampleSelective(@Param("record") Order record, @Param("example") OrderExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ORDER_TABLE
     *
     * @mbg.generated Sun Jun 28 13:29:32 CST 2020
     */
    int updateByExample(@Param("record") Order record, @Param("example") OrderExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ORDER_TABLE
     *
     * @mbg.generated Sun Jun 28 13:29:32 CST 2020
     */
    int updateByPrimaryKeySelective(Order record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ORDER_TABLE
     *
     * @mbg.generated Sun Jun 28 13:29:32 CST 2020
     */
    int updateByPrimaryKey(Order record);
}