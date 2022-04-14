package com.example.soulaid.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class TeacherMessage implements Parcelable, Serializable {
    private int id;
    private String username;
    private String password;
    private int tag; //教师专用: 0表示该教师什么都会，对于某一领域的教师，其tag应从1开始

    public TeacherMessage(){}
    public TeacherMessage(int id, String username, String password,int tag) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.tag=tag;
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

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
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
        parcel.writeInt(tag);
    }

    public static final Parcelable.Creator<TeacherMessage> CREATOR = new Creator<TeacherMessage>() {
        @Override
        public TeacherMessage createFromParcel(Parcel parcel) {
            TeacherMessage teacherMessage = new TeacherMessage();
            teacherMessage.setId(parcel.readInt());
            teacherMessage.setUsername(parcel.readString());
            teacherMessage.setPassword(parcel.readString());
            teacherMessage.setTag(parcel.readInt());
            return teacherMessage;
        }

        @Override
        public TeacherMessage[] newArray(int i) {
            return new TeacherMessage[i];
        }
    };
}
