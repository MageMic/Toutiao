package com.zjumic.jmToutiao.service;

import com.zjumic.jmToutiao.dao.UserDAO;
import com.zjumic.jmToutiao.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserDAO userDAO;

    public User getUser(int id) {
        return userDAO.selectById(id);
    }
}
