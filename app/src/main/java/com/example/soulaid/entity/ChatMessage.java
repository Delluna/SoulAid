package com.example.soulaid.entity;

public class ChatMessage {
    public static final int TYPE_SEND = 1;
    public static final int TYPE_RECEIVE = 0;

    private int id;
    private String message;
    private String tname;
    private String uname;
    private int type;

    public ChatMessage() {
    }

    ;

    public ChatMessage(int id, String message, String tname, String uname, int type) {
        this.message = message;
        this.tname = tname;
        this.uname = uname;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTname() {
        return tname;
    }

    public void setTname(String tname) {
        this.tname = tname;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
