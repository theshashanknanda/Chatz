package com.project.chatz.source.source.Model;

public class Message {

    public String message;
    public String id;
    public long time;

    public Message(String message, String id, long time) {
        this.message = message;
        this.id = id;
        this.time = time;
    }

    public Message(){}

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
