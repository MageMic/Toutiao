package com.zjumic.jmToutiao;

import com.zjumic.jmToutiao.dao.UserDAO;
import com.zjumic.jmToutiao.model.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Random;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = JmToutiaoApplication.class)
@Sql("/init-schema.sql")
public class InitDatabaseTests {
	@Autowired
	UserDAO userDAO;
	@Test
	public void contextLoads() {
		Random random = new Random();
		for (int i = 0; i< 11; i++) {
			User user = new User();
			user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", random.nextInt(1000)));
			user.setName(String.format("Jiemei%d",i));
			user.setPassword("");
			user.setSalt("");
			userDAO.addUser(user);

			user.setPassword("newpassword");
			userDAO.updatePassword(user);
		}
		Assert.assertEquals("newpassword", userDAO.selectById(1).getPassword());
		userDAO.deleteById(1);
		Assert.assertNull(userDAO.selectById(1));
	}
}
