package com.gofocus.wxshop.dao;

import com.gofocus.wxshop.entity.User;
import com.gofocus.wxshop.entity.UserExample;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author: GoFocus
 * @Date: 2020-06-14 23:20
 * @Description:
 */

@Mapper
public interface UserDao extends MybatisBaseDao<User, Long, UserExample> {


}
