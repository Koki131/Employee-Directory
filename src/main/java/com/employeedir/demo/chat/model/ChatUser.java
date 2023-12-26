package com.employeedir.demo.chat.model;

import org.apache.tomcat.util.codec.binary.Base64;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public class ChatUser {

    private String userName;

    private String image;

    private byte[] imageData;


    public ChatUser() {

    }

    public ChatUser(String userName) {
        this.userName = userName;
    }

    public ChatUser(String userName, String image) {
        this.userName = userName;
        this.image = image;
    }

    public ChatUser(String userName, String image, byte[] imageData) {
        this.userName = userName;
        this.image = image;
        this.imageData = imageData;
    }

    public String generateBase64Image() {
        return Base64.encodeBase64String(this.imageData);
    }

    public byte[] getImageData() {
        return imageData;
    }

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImagePath() {

        if (image == null) return null;

        return "/profile-images/" + userName + "/" + image;

    }

    public boolean isDir() {

        String dir = "./profile-images/" + userName;

        Path uploadPath = Paths.get(dir);

        return Files.isDirectory(uploadPath);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ChatUser chatUser = (ChatUser) o;

        return Objects.equals(userName, chatUser.userName);
    }

    @Override
    public int hashCode() {
        return userName != null ? userName.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "ChatUser{" +
                "userName='" + userName + '\'' +
                '}';
    }
}
