package com.zjumic.jmToutiao.controller;

import com.zjumic.jmToutiao.model.HostHolder;
import com.zjumic.jmToutiao.model.News;
import com.zjumic.jmToutiao.service.NewsService;
import com.zjumic.jmToutiao.service.QiniuService;
import com.zjumic.jmToutiao.util.JiemeiUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.util.Date;

@Controller
public class NewsController {
    private static final Logger logger = LoggerFactory.getLogger(NewsController.class);

    @Autowired
    NewsService newsService;
    @Autowired
    QiniuService qiniuService;
    @Autowired
    HostHolder hostHolder;

    @RequestMapping(path = {"/image"}, method = {RequestMethod.GET})
    @ResponseBody
    public void getImage(@RequestParam("name") String imageName,
                         HttpServletResponse response) {
        try {
            response.setContentType("image/jpeg");
            StreamUtils.copy(new FileInputStream(new
                    File(JiemeiUtil.IMAGE_DIR + imageName)),
                    response.getOutputStream());
        } catch (Exception e) {
            logger.error("读取图片错误" + e.getMessage());
        }
    }

    @RequestMapping(path = {"/user/addNews/"}, method = {RequestMethod.POST})
    @ResponseBody
    public String addNews(@RequestParam ("image") String image,
                          @RequestParam ("title") String title,
                          @RequestParam ("link") String link) {
        try {
            News news = new News();
            if (hostHolder.getUser() != null){
                news.setUserId(hostHolder.getUser().getId());
            } else {
                //匿名用户？？？
                news.setUserId(1);
            }
            news.setImage(image);
            news.setCreatedDate(new Date());
            news.setTitle(title);
            news.setLink(link);
            newsService.addNews(news);
            return JiemeiUtil.getJSONString(0);
        } catch (Exception e) {
            logger.error("添加资讯错误：" + e.getMessage());
            return JiemeiUtil.getJSONString(1, "资讯发布失败");
        }
    }

    @RequestMapping(path = {"/uploadImage/"}, method = {RequestMethod.POST})
    @ResponseBody
    public String uploadImage(@RequestParam("file")MultipartFile file) {
        try {
            //String fileUrl = newsService.saveImage(file);
            String fileUrl = qiniuService.saveImage(file);
            if (fileUrl == null) {
                return JiemeiUtil.getJSONString(1, "上传图片失败");
            }
            return JiemeiUtil.getJSONString(0, fileUrl);
        } catch (Exception e) {
            logger.error("上传图片失败" + e.getMessage());
            return JiemeiUtil.getJSONString(1, "上传失败");
        }
    }
}
