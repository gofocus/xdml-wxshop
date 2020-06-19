package com.gofocus.wxshop.dao;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

/**
 * @Author: GoFocus
 * @Date: 2020-06-18 13:06
 * @Description:
 */

@Repository
public class GoodsDao {

    private final SqlSession sqlSession;

    public GoodsDao(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }


}
