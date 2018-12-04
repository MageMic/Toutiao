package com.zjumic.jmToutiao.controller;

import com.zjumic.jmToutiao.dao.MessageDAO;
import com.zjumic.jmToutiao.model.Comment;
import com.zjumic.jmToutiao.model.EntityType;
import com.zjumic.jmToutiao.model.Message;
import com.zjumic.jmToutiao.service.MessageService;
import com.zjumic.jmToutiao.util.JiemeiUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

@Controller
public class MessageController {
    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);

    @Autowired
    MessageService messageService;

    @RequestMapping(path = {"/msg/addMessage"}, method = {RequestMethod.POST})
    @ResponseBody
    public String addMessage(@RequestParam("fromId") int fromId,
                             @RequestParam("toId") int toId,
                             @RequestParam("content") String content) {
        try {
           Message msg = new Message();
           msg.setContent(content);
           msg.setFromId(fromId);
           msg.setToId(toId);
           msg.setCreatedDate(new Date());
           msg.setConversationId(fromId < toId ? String.format("%d_%d", fromId, toId) : String.format("%d_%d", toId, fromId));
           messageService.addMessage(msg);
           return JiemeiUtil.getJSONString(msg.getId());
        } catch (Exception e) {
            logger.error("添加消息异常" + e.getMessage());
            return JiemeiUtil.getJSONString(1, "插入消息失败");
        }
    }
}
