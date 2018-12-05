package com.zjumic.jmToutiao.service;

import com.zjumic.jmToutiao.dao.MessageDAO;
import com.zjumic.jmToutiao.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {
    @Autowired
    MessageDAO messageDAO;

    public int addMessage(Message message) {
        return messageDAO.addMessage(message);
    }

    public List<Message> getConversationDetail(String conversationId, int offset, int limit){
        return messageDAO.getConversationDetail(conversationId, offset, limit);
    }

    public List<Message> getConversationList(int userId, int offset, int limit){
        return messageDAO.getConversationList(userId, offset, limit);
    }

    public int getConversationUnreadCount(int userId, String ConversationId) {
        return messageDAO.getConversationUnread(userId, ConversationId);
    }

    public void readUnread(int userId, String conversationId){
        messageDAO.readUnread(userId, conversationId);
    }
}
