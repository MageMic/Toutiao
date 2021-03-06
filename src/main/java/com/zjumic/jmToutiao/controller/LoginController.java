package com.zjumic.jmToutiao.controller;

import com.zjumic.jmToutiao.model.News;
import com.zjumic.jmToutiao.model.ViewObject;
import com.zjumic.jmToutiao.service.NewsService;
import com.zjumic.jmToutiao.service.UserService;
import com.zjumic.jmToutiao.util.JiemeiUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class LoginController {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    NewsService newsService;

    @Autowired
    UserService userService;

    @RequestMapping(path = {"/reg/"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String reg(@RequestParam("username")String username,
                      @RequestParam("password")String password,
                      @RequestParam(value="rember", defaultValue = "0") int rememberme,Model model,
                      HttpServletResponse response) {
       try {
           Map<String, Object> map = userService.register(username, password);
    //{"code":0, "msg":"xxxx"} , <b>}
           if (map.containsKey("ticket")) {
               Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
               cookie.setPath("/");
               if (rememberme > 0) {
                   cookie.setMaxAge(3600*24*5);
               }
               response.addCookie(cookie);
               return JiemeiUtil.getJSONString(0, "注册成功");
           } else {
               return JiemeiUtil.getJSONString(1, map);
           }
       } catch (Exception e) {
           logger.error("注册异常" + e.getMessage());
           return JiemeiUtil.getJSONString(1, "注册异常");
       }
    }

    @RequestMapping(path = {"/login/"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String login(@RequestParam("username")String username,
                        @RequestParam("password")String password,
                        @RequestParam(value="rember", defaultValue = "0"
                      ) int rememberme, Model model, HttpServletResponse response) {
        try {
            Map<String, Object> map = userService.login(username, password);
            //{"code":0, "msg":"xxxx"} , <b>}
            if (map.containsKey("ticket")) {
                Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
                cookie.setPath("/");
                if (rememberme > 0) {
                    cookie.setMaxAge(3600*24*5);
                }
                response.addCookie(cookie);
                return JiemeiUtil.getJSONString(0, "登录成功");
            } else {
                return JiemeiUtil.getJSONString(1, map);
            }
        } catch (Exception e) {
            logger.error("登录异常" + e.getMessage());
            return JiemeiUtil.getJSONString(1, "登录异常");
        }
    }

    @RequestMapping(path = {"/logout/"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String logout(@CookieValue("ticket") String ticket) {
        userService.logout(ticket);
        return "redirect:/";
    }
}


