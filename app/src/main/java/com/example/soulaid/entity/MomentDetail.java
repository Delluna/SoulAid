package com.example.soulaid.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.sql.Timestamp;

public class MomentDetail implements Parcelable, Serializable {
    private int id;
    private String uname;
    private String title;
    private String content;
    //数据库中数据类型datetime对应java中Timestamp
    private Timestamp datetime;
    private int liked;

    public MomentDetail(){}
    public MomentDetail(int id, String uname, String title, String content, Timestamp datetime, int liked) {
        this.id = id;
        this.uname=uname;
        this.title = title;
        this.content = content;
        this.datetime = datetime;
        this.liked=liked;
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
        this.uname=uname;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getDatetime() {
        return datetime;
    }

    public void setDatetime(Timestamp datetime) {
        this.datetime = datetime;
    }

    public int getLiked() {
        return liked;
    }

    public void setLiked(int liked) {
        this.liked = liked;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(uname);
        parcel.writeString(title);
        parcel.writeString(content);
        parcel.writeString(datetime.toString());
        parcel.writeInt(liked);
    }
    public static final Parcelable.Creator<MomentDetail> CREATOR = new Creator<MomentDetail>() {
        @Override
        public MomentDetail createFromParcel(Parcel parcel) {
            MomentDetail momentDetail=new MomentDetail();
            momentDetail.setId(parcel.readInt());
            momentDetail.setUname(parcel.readString());
            momentDetail.setTitle(parcel.readString());
            momentDetail.setContent(parcel.readString());
            momentDetail.setDatetime(Timestamp.valueOf(parcel.readString()));
            momentDetail.setLiked(parcel.readInt());
            return momentDetail;
        }

        @Override
        public MomentDetail[] newArray(int i) {
            return new MomentDetail[i];
        }
    };
}
