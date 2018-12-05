package com.zjumic.jmToutiao.controller;

import com.zjumic.jmToutiao.dao.MessageDAO;
import com.zjumic.jmToutiao.model.*;
import com.zjumic.jmToutiao.service.MessageService;
import com.zjumic.jmToutiao.service.UserService;
import com.zjumic.jmToutiao.util.JiemeiUtil;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class MessageController {
    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);

    @Autowired
    MessageService messageService;
    @Autowired
    UserService userService;
    @Autowired
    HostHolder hostHolder;

    @RequestMapping(path = {"/msg/list"}, method = {RequestMethod.GET})
    public String conversationList(Model model) {
        try{
            int localUserId = hostHolder.getUser().getId();
            List<ViewObject> conversations = new ArrayList<>();
            List<Message> conversationList = messageService.getConversationList(localUserId, 0, 10);
            for (Message msg : conversationList) {
                ViewObject vo = new ViewObject();
                vo.set("conversation", msg);
                vo.set("unreadCount", messageService.getConversationUnreadCount(localUserId,msg.getConversationId()));
                //targetId 可能是from也可能是to
                int targetId = (msg.getFromId() == localUserId) ? msg.getToId(): msg.getFromId();
                User targetUser = userService.getUser(targetId);
                vo.set("target", targetUser);

                conversations.add(vo);
            }
            model.addAttribute("conversations", conversations);
        }catch (Exception e) {
            logger.error("获取站内信列表失败" + e.getMessage());
        }
        return "letter";
    }

    @RequestMapping(path = {"/msg/detail"}, method = {RequestMethod.GET})
    public String conversationDetail(Model model, @Param("conversationId") String conversationId) {
        try {
            List<Message> conversationList = messageService.getConversationDetail(conversationId, 0, 10);
            List<ViewObject>  messages = new ArrayList<ViewObject>();
            for (Message msg : conversationList){
                ViewObject vo = new ViewObject();
                if (msg.getHasRead() == 0) {
                    messageService.readUnread(msg.getToId(), conversationId);
                }
                vo.set("message", msg);
                User user = userService.getUser(msg.getFromId());
                if (user == null) {
                    continue;
                }
                vo.set("headUrl",user.getHeadUrl());
                vo.set("userId",user.getId());
                messages.add(vo);
            }
            model.addAttribute("messages", messages);
            return "letterDetail";
        } catch (Exception e) {
           logger.error("获取详情消息失败" + e.getMessage());
        }
        return "letterDetail";
    }

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
