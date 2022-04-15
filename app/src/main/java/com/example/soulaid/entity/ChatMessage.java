package com.example.soulaid.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.sql.Timestamp;

public class ChatMessage implements Serializable, Parcelable {
    public static final int TYPE_SEND = 1;
    public static final int TYPE_RECEIVE = 0;

    private int id;
    private String message;
    private String tname;
    private String uname;
    private int type;

    public ChatMessage() {
    }

    public ChatMessage( String message, String tname, String uname, int type) {
        this.message = message;
        this.tname = tname;
        this.uname = uname;
        this.type = type;
    }
    public ChatMessage(int id, String message, String tname, String uname, int type) {
        this.id=id;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(message);
        parcel.writeString(tname);
        parcel.writeString(uname);
        parcel.writeInt(type);
    }
    public static final Parcelable.Creator<ChatMessage> CREATOR = new Creator<ChatMessage>() {
        @Override
        public ChatMessage createFromParcel(Parcel parcel) {
            ChatMessage chatMessage =new ChatMessage();
            chatMessage.setId(parcel.readInt());
            chatMessage.setMessage(parcel.readString());
            chatMessage.setTname(parcel.readString());
            chatMessage.setUname(parcel.readString());
            chatMessage.setType(parcel.readInt());
            return chatMessage;
        }

        @Override
        public ChatMessage[] newArray(int i) {
            return new ChatMessage[i];
        }
    };
}
