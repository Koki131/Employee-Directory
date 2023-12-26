package com.employeedir.demo.chat.model;


import java.util.Date;
import java.util.List;

public class ChatMessage {
    private ChatUser sender;
    private String content;
    private Date timestamp;

    private List<ChatMessage> globalMessages;
    public ChatMessage() {
    }

    public ChatMessage(ChatUser sender, String content, List<ChatMessage> globalMessages) {
        this.sender = sender;
        this.content = content;
        this.timestamp = new Date();
        this.globalMessages = globalMessages;
    }

    public ChatUser getSender() {
        return sender;
    }

    public void setSender(ChatUser sender) {
        this.sender = sender;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public List<ChatMessage> getGlobalMessages() {
        return globalMessages;
    }

    public void setGlobalMessages(List<ChatMessage> globalMessages) {
        this.globalMessages = globalMessages;
    }

    @Override
    public String toString() {
        return "ChatMessage{" +
                "sender=" + sender +
                ", content='" + content + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}