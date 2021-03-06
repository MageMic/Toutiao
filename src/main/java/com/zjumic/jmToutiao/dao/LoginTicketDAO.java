package com.zjumic.jmToutiao.dao;

import com.zjumic.jmToutiao.model.LoginTicket;
import com.zjumic.jmToutiao.model.User;
import org.apache.ibatis.annotations.*;

/**
 * Created by rainday on 16/6/30.
 */
@Mapper
public interface LoginTicketDAO {

    String TABLE_NAME = "login_ticket";

    String INSERT_FIELDS = " user_id, expired, status, ticket ";

    String SELECT_FIELDS = " id, " +  INSERT_FIELDS;

    @Insert({
            "insert into ", TABLE_NAME, "(", INSERT_FIELDS,
            ") Values (#{userId}, #{expired}, #{status}, #{ticket})"
    })
    int addTicket(LoginTicket ticket);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where ticket=#{ticket}"})
    LoginTicket selectByTicket(String ticket);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where user_id=#{userId}"})
    LoginTicket selectByUserId(int userId);

    @Update({"update ", TABLE_NAME, " set status=#{status} where ticket=#{ticket}"})
    void updateStatus(@Param("ticket") String ticket, @Param("status") int status);
}
