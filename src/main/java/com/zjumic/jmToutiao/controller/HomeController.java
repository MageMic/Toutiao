package com.zjumic.jmToutiao.controller;

import com.zjumic.jmToutiao.model.News;
import com.zjumic.jmToutiao.model.ViewObject;
import com.zjumic.jmToutiao.service.NewsService;
import com.zjumic.jmToutiao.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {
    @Autowired
    NewsService newsService;

    @Autowired
    UserService userService;

    @RequestMapping(path = {"/", "/index"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String index(Model model) {
        List<News> newsList = newsService.getLatestNews(0,0,10);

        List<ViewObject> vos = new ArrayList<>();
        for(News news: newsList){
            ViewObject vo = new ViewObject();
            vo.set("news",news);
            news.getCreatedDate();
            vo.set("user", userService.getUser(news.getUserId()));
            vos.add(vo);
        }

        model.addAttribute("vos", vos);
        return "home";
    }

}
