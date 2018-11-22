package com.zjumic.jmToutiao.service;

import com.zjumic.jmToutiao.dao.LoginTicketDAO;
import com.zjumic.jmToutiao.dao.UserDAO;
import com.zjumic.jmToutiao.model.LoginTicket;
import com.zjumic.jmToutiao.model.User;
import com.zjumic.jmToutiao.util.JiemeiUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private LoginTicketDAO loginTicketDAO;

    public Map<String, Object> register(String username, String password) {
       Map<String, Object> map = new HashMap<String, Object>();
        if (StringUtils.isBlank(username)){
            map.put("msgname", "用户名不能为空");
            return map;
        }
        if (StringUtils.isBlank(password)){
            map.put("msgpwd", "密码不能为空");
            return map;
        }
        User user = userDAO.selectByName(username);

        if (user != null) {
            map.put("msgname", "用户名已经被注册");
            return map;
        }

        //密码强度？

        user = new User();
        user.setName(username);
        user.setSalt(UUID.randomUUID().toString().substring(0,5)); //加强密码，取随机UUID前五位做salt
        user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dm.png", new Random().nextInt(1000)));
        user.setPassword(JiemeiUtil.MD5(password+user.getSalt()));
        userDAO.addUser(user);

        //登录
        String ticket = addLoginTicket(user.getId());
        map.put("ticket",ticket);

        return map;
    }

    public Map<String, Object> login(String username, String password) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (StringUtils.isBlank(username)){
            map.put("msgname", "用户名不能为空");
            return map;
        }
        if (StringUtils.isBlank(password)){
            map.put("msgpwd", "密码不能为空");
            return map;
        }
        User user = userDAO.selectByName(username);

        if (user == null) {
            map.put("msgname", "用户名不存在");
            return map;
        }

        if (JiemeiUtil.MD5(password+user.getSalt()).equals(user.getPassword())) {
            map.put("msgpwd", "密码不正确");
            return map;
        }
        //ticket下发
        //查找用户ticket

        String ticket = addLoginTicket(user.getId());
        map.put("ticket",ticket);
        return map;
    }

    private String addLoginTicket(int userId){
        LoginTicket ticket = new LoginTicket();
        ticket.setUserId(userId);
        Date date = new Date();//当前时间，毫秒为单位
        date.setTime(date.getTime() + 1000*3600*24);
        ticket.setExpired(date);
        ticket.setStatus(0);
        ticket.setTicket(UUID.randomUUID().toString().replaceAll("-",""));
        loginTicketDAO.addTicket(ticket);
        return ticket.getTicket();
    }

    public User getUser(int id) {
        return userDAO.selectById(id);
    }

    public void logout(String ticket) {
        loginTicketDAO.updateStatus(ticket, 1);
    }
}
