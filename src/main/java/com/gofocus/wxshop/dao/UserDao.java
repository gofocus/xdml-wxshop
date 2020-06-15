package com.gofocus.wxshop.dao;

import com.gofocus.wxshop.entity.User;
import com.gofocus.wxshop.entity.UserExample;
import com.gofocus.wxshop.mapper.UserMapper;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * @Author: GoFocus
 * @Date: 2020-06-14 23:20
 * @Description:
 */

@Repository
public class UserDao {
    private final SqlSession sqlSession;
    private final UserMapper userMapper;

    @Autowired

    public UserDao(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
        this.userMapper = sqlSession.getMapper(UserMapper.class);
    }

    public void insertUser(User user) {
        userMapper.insert(user);
    }

    public User getUserByTel(String tel) {
        UserExample userExample = new UserExample();
        userExample.createCriteria().andTelEqualTo(tel);
        return userMapper.selectByExample(userExample).get(0);
    }

}
