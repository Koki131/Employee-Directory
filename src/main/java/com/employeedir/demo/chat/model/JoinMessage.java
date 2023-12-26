package com.employeedir.demo.chat.model;


import com.employeedir.demo.chat.model.ChatMessage;
import com.employeedir.demo.model.User;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class JoinMessage {

    private Set<ChatUser> users;

    private List<ChatMessage> globalMessages;

    private Map<String, Map<String, Integer>> messageCount;

    private Map<String, PrivateMessage> allConvos;


    public JoinMessage() {

    }

    public JoinMessage(Set<ChatUser> users, List<ChatMessage> globalMessages, Map<String, Map<String, Integer>> messageCount, Map<String, PrivateMessage> allConvos) {
        this.users = users;
        this.globalMessages = globalMessages;
        this.messageCount = messageCount;
        this.allConvos = allConvos;
    }


    public List<ChatMessage> getGlobalMessages() {
        return globalMessages;
    }

    public void setGlobalMessages(List<ChatMessage> globalMessages) {
        this.globalMessages = globalMessages;
    }

    public void add(ChatUser user) {
        this.users.add(user);
    }
    public Set<ChatUser> getUsers() {
        return users;
    }

    public void setUsers(Set<ChatUser> users) {
        this.users = users;
    }

    public Map<String, Map<String, Integer>> getMessageCount() {
        return messageCount;
    }

    public void setMessageCount(Map<String, Map<String, Integer>> messageCount) {
        this.messageCount = messageCount;
    }

    public Map<String, PrivateMessage> getAllConvos() {
        return allConvos;
    }

    public void setAllConvos(Map<String, PrivateMessage> allConvos) {
        this.allConvos = allConvos;
    }
}
