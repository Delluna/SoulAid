package com.example.soulaid.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.sql.Timestamp;

public class Comment implements Serializable, Parcelable {
    private int id;
    private String uname;
    private int mid;
    private String content;
    private Timestamp date;

    public Comment(){}

    public Comment(int id,String uname,int mid, String content,Timestamp date){
        this.id=id;
        this.uname = uname;
        this.mid=mid;
        this.content=content;
        this.date=date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public int getMid() {
        return mid;
    }

    public void setMid(int mid) {
        this.mid = mid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(uname);
        parcel.writeInt(mid);
        parcel.writeString(content);
        parcel.writeString(date.toString());
    }


    public static final Parcelable.Creator<Comment> CREATOR = new Creator<Comment>() {
        @Override
        public Comment createFromParcel(Parcel parcel) {
            Comment comment=new Comment();
            comment.setId(parcel.readInt());
            comment.setUname(parcel.readString());
            comment.setMid(parcel.readInt());
            comment.setContent(parcel.readString());
            comment.setDate(Timestamp.valueOf(parcel.readString()));
            return comment;
        }

        @Override
        public Comment[] newArray(int i) {
            return new Comment[i];
        }
    };
}
