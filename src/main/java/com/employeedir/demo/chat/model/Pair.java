package com.employeedir.demo.chat.model;

import java.util.Objects;

public class Pair {
    private String sender;
    private String recipient;

    public Pair(String sender, String recipient) {
        this.sender = sender;
        this.recipient = recipient;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair pair = (Pair) o;
        return Objects.equals(sender, pair.sender) && Objects.equals(recipient, pair.recipient);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sender, recipient);
    }

    @Override
    public String toString() {
        return "Pair{" +
                "sender='" + sender + '\'' +
                ", recipient='" + recipient + '\'' +
                '}';
    }
}