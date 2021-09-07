package com.project.chatz.source.source.Model;

public class User {

    private String username, email, password, lastMessage, Id, imageurl;
    private long lastmessageTime = 0;

    public User(String username, String email, String password, String lastMessage, String id, String imageurl) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.lastMessage = lastMessage;
        Id = id;
        this.imageurl = imageurl;
    }

    public User(String userName, String email, String password){
        this.username = userName;
        this.email = email;
        this.password = password;
    }

    public User(){}

    public String getUsername() {
        return username;
    }

    public void setUsername(String userName) {
        this.username = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLastmessage() {
        return lastMessage;
    }

    public void setLastmessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageURl) {
        this.imageurl = imageURl;
    }

    public long getLastmessagetime() {
        return lastmessageTime;
    }

    public void setLastmessagetime(long lastmessageTime) {
        this.lastmessageTime = lastmessageTime;
    }
}
