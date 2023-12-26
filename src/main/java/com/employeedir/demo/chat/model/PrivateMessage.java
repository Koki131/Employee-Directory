package com.employeedir.demo.chat.model;


import com.employeedir.demo.model.User;


import java.util.Date;
import java.util.List;

public class PrivateMessage {

    private ChatUser sender;
    private String recipient;
    private String content;

    private Date timestamp;

    private List<PrivateMessage> messages;


    public PrivateMessage() {

    }

    public PrivateMessage(ChatUser sender, String recipient, String content, List<PrivateMessage> messages) {
        this.sender = sender;
        this.recipient = recipient;
        this.content = content;
        this.messages = messages;
        this.timestamp = new Date();
    }


    public ChatUser getSender() {
        return sender;
    }

    public void setSender(ChatUser sender) {
        this.sender = sender;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    // Getters and setters


    public List<PrivateMessage> getMessages() {
        return messages;
    }

    public void setMessages(List<PrivateMessage> messages) {
        this.messages = messages;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "PrivateMessage{" +
                "sender=" + sender +
                ", recipient='" + recipient + '\'' +
                ", content='" + content + '\'' +
                ", timestamp=" + timestamp +
                ", messages=" + messages +
                '}';
    }
}