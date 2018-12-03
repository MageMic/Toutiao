package com.zjumic.jmToutiao;

import com.zjumic.jmToutiao.dao.CommentDAO;
import com.zjumic.jmToutiao.dao.LoginTicketDAO;
import com.zjumic.jmToutiao.dao.NewsDAO;
import com.zjumic.jmToutiao.dao.UserDAO;
import com.zjumic.jmToutiao.model.*;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Date;
import java.util.Random;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = JmToutiaoApplication.class)
@Sql("/init-schema1.sql")
public class InitDatabaseTests {
	@Autowired
	UserDAO userDAO;
	@Autowired
    NewsDAO newsDAO;
    @Autowired
    LoginTicketDAO loginTicketDAO;
    @Autowired
	CommentDAO commentDAO;

	@Test
	public void contextLoads() {
		Random random = new Random();
		for (int i = 0; i< 11; i++) {

			User user = new User();
			user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", random.nextInt(1000)));
			user.setName(String.format("Jiemei%d",i + 11));
			user.setPassword("");
			user.setSalt("");
			userDAO.addUser(user);

			News news = new News();
			news.setCommentCount(i);
			Date date = new Date();
			date.setTime(date.getTime() + 1000*3600*5*i);
			news.setCreatedDate(date);
			news.setImage(String.format("http://images.nowcoder.com/head/%dm.png", random.nextInt(1000)));
			news.setLikeCount(i+1);
			news.setUserId(i+1);
			news.setTitle(String.format("TITLE{%d}", i));
            news.setLink(String.format("http://www.nowcoder.com/%d.html", i));

            newsDAO.addNews(news);

			user.setPassword("newpassword");
			userDAO.updatePassword(user);

			for (int j = 0; j < 3; ++j) {
				Comment comment = new Comment();
				comment.setUserId(i+1);
				comment.setEntityId(news.getId());
				comment.setEntityType(EntityType.ENTITY_NEWS);
				comment.setStatus(0);
				comment.setCreatedDate(new Date());
				comment.setContent("Comment " + String.valueOf(j));
				commentDAO.addComment(comment);
			}
		}


	}
}
