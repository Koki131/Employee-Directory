package com.employeedir.demo.chat.model;

import java.util.List;

public class SenderRecipientMessages {

    private List<PrivateMessage> messages;


    public SenderRecipientMessages(List<PrivateMessage> messages) {
        this.messages = messages;
    }

    public List<PrivateMessage> getMessages() {
        return messages;
    }

    public void setMessages(List<PrivateMessage> messages) {
        this.messages = messages;
    }
}
