package com.zjumic.jmToutiao.service;

import com.zjumic.jmToutiao.dao.NewsDAO;
import com.zjumic.jmToutiao.model.News;
import com.zjumic.jmToutiao.util.JiemeiUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
public class NewsService {
    @Autowired
    private NewsDAO newsDAO;

    public List<News> getLatestNews(int userId, int offset, int limit){
        return newsDAO.selectByUserIdAndOffset(userId, offset, limit);
    }

    public int addNews(News news) {
        newsDAO.addNews(news);
        return news.getId();
    }

    public News getById(int newsId) {
        return newsDAO.getById(newsId);
    }

    public int updateCommentCount(int id, int count) {
        return newsDAO.updateCommentCount(id, count);
    }

    public String saveImage(MultipartFile file) throws IOException {
        //判断是不是一个图片，通过后缀名判断
        int dotPos = file.getOriginalFilename().lastIndexOf(".");
        if (dotPos < 0) {
            return null;
        }
        String fileExt = file.getOriginalFilename().substring(dotPos + 1).toLowerCase();
        if (!JiemeiUtil.isFileAllowed(fileExt)) {
            return null;
        }

        String filename = UUID.randomUUID().toString().replaceAll("-", "")
                + "." + fileExt;
        Files.copy(file.getInputStream(),new File(JiemeiUtil.IMAGE_DIR + filename).toPath(), StandardCopyOption.REPLACE_EXISTING);
        //xxxx.jpg
        return JiemeiUtil.TOUTIAO_DOMAIN + "image?name=" + filename;
    }
}
