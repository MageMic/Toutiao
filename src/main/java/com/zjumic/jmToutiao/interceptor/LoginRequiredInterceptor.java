package com.zjumic.jmToutiao.interceptor;

import com.zjumic.jmToutiao.dao.LoginTicketDAO;
import com.zjumic.jmToutiao.dao.UserDAO;
import com.zjumic.jmToutiao.model.HostHolder;
import com.zjumic.jmToutiao.model.LoginTicket;
import com.zjumic.jmToutiao.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Component
public class LoginRequiredInterceptor implements HandlerInterceptor {


    @Autowired
    private HostHolder hostHolder;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (hostHolder.getUser() == null) {
            response.sendRedirect("/?pop=1");
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
