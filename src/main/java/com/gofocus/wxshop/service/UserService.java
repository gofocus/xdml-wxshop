package com.gofocus.wxshop.service;

import com.gofocus.wxshop.dao.UserDao;
import com.gofocus.wxshop.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @Author: GoFocus
 * @Date: 2020-06-14 23:07
 * @Description:
 */

@Service
public class UserService {

    private final UserDao userDao;

    @Autowired
    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }


    public User createUserIfNotExist(String tel) {
        try {
            User user = new User();
            user.setTel(tel);
            user.setCreatedAt(new Date());
            user.setUpdatedAt(new Date());
            userDao.insertUser(user);
        } catch (Exception e) {
            return userDao.getUserByTel(tel);
        }
        return null;
    }


}
