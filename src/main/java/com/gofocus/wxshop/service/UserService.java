package com.gofocus.wxshop.service;

import com.gofocus.wxshop.entity.User;
import com.gofocus.wxshop.entity.UserExample;
import com.gofocus.wxshop.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @Author: GoFocus
 * @Date: 2020-06-14 23:07
 * @Description:
 */

@Service
public class UserService {

    private final UserMapper userMapper;

    @Autowired
    public UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }


    public void createUserIfNotExist(String tel) {
        try {
            User user = new User();
            user.setTel(tel);
            user.setCreatedAt(new Date());
            user.setUpdatedAt(new Date());
            userMapper.insert(user);
        } catch (Exception e) {
            getUserByTel(tel);
        }
    }

    public User getUserByTel(String tel) {
        try {
            UserExample userExample = new UserExample();
            userExample.createCriteria().andTelEqualTo(tel);
            List<User> users = userMapper.selectByExample(userExample);
            if (users.size() != 0) {
                return users.get(0);
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

}
