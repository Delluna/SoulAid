package com.example.soulaid.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.Objects;

public class UserMessage implements Serializable, Parcelable {
    private int id;
    private String username;
    private String password;

    public UserMessage(){}

    public UserMessage(int id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeInt(id);
        parcel.writeString(username);
        parcel.writeString(password);
    }
    public static final Parcelable.Creator<UserMessage> CREATOR = new Creator<UserMessage>() {
        @Override
        public UserMessage createFromParcel(Parcel parcel) {
            UserMessage userMessage = new UserMessage();
            userMessage.setId(parcel.readInt());
            userMessage.setUsername(parcel.readString());
            userMessage.setPassword(parcel.readString());
            return userMessage;
        }


        @Override
        public UserMessage[] newArray(int i) {
            return new UserMessage[i];
        }
    };

    @Override
    public boolean equals(Object o){
        if(this==o) return true;
        if(o==null||getClass()!=o.getClass()) return false;
        UserMessage userMessage = (UserMessage) o;
        return Objects.equals(id,userMessage.id) &&
                Objects.equals(username,userMessage.username) &&
                Objects.equals(password,userMessage.password);
    }
}
