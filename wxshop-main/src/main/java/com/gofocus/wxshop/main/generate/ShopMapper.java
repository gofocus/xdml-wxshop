package com.gofocus.wxshop.main.generate;

import com.gofocus.wxshop.main.generate.Shop;
import com.gofocus.wxshop.main.generate.ShopExample;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ShopMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table SHOP
     *
     * @mbg.generated Fri Jun 26 16:32:46 CST 2020
     */
    long countByExample(ShopExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table SHOP
     *
     * @mbg.generated Fri Jun 26 16:32:46 CST 2020
     */
    int deleteByExample(ShopExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table SHOP
     *
     * @mbg.generated Fri Jun 26 16:32:46 CST 2020
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table SHOP
     *
     * @mbg.generated Fri Jun 26 16:32:46 CST 2020
     */
    int insert(Shop record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table SHOP
     *
     * @mbg.generated Fri Jun 26 16:32:46 CST 2020
     */
    int insertSelective(Shop record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table SHOP
     *
     * @mbg.generated Fri Jun 26 16:32:46 CST 2020
     */
    List<Shop> selectByExample(ShopExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table SHOP
     *
     * @mbg.generated Fri Jun 26 16:32:46 CST 2020
     */
    Shop selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table SHOP
     *
     * @mbg.generated Fri Jun 26 16:32:46 CST 2020
     */
    int updateByExampleSelective(@Param("record") Shop record, @Param("example") ShopExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table SHOP
     *
     * @mbg.generated Fri Jun 26 16:32:46 CST 2020
     */
    int updateByExample(@Param("record") Shop record, @Param("example") ShopExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table SHOP
     *
     * @mbg.generated Fri Jun 26 16:32:46 CST 2020
     */
    int updateByPrimaryKeySelective(Shop record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table SHOP
     *
     * @mbg.generated Fri Jun 26 16:32:46 CST 2020
     */
    int updateByPrimaryKey(Shop record);
}