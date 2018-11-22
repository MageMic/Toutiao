package com.zjumic.jmToutiao.controller;

import com.zjumic.jmToutiao.model.HostHolder;
import com.zjumic.jmToutiao.model.News;
import com.zjumic.jmToutiao.model.ViewObject;
import com.zjumic.jmToutiao.service.NewsService;
import com.zjumic.jmToutiao.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {
    @Autowired
    NewsService newsService;

    @Autowired
    UserService userService;

    @Autowired
    HostHolder hostHolder;

    private List<ViewObject> getNews(int userId, int offset, int limit) {
        List<News> newsList = newsService.getLatestNews(userId, offset, limit);

        List<ViewObject> vos = new ArrayList<>();
        for(News news: newsList) {
            ViewObject vo = new ViewObject();
            vo.set("news", news);
            news.getCreatedDate();
            vo.set("user", userService.getUser(news.getUserId()));
            vos.add(vo);
        }
        return vos;
    }

    @RequestMapping(path = {"/", "/index"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String index(@RequestParam(value = "userId", defaultValue = "0")int userId, Model model) {
        model.addAttribute("vos",getNews(0,0,10));
        return "home";
    }

    @RequestMapping(path = {"/user/{userId}"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String userIndex(@PathVariable("userId")int userId, Model model) {
        model.addAttribute("vos",getNews(userId,0,10));
        return "home";
    }


}


