package com.example.soulaid.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.soulaid.util.DBUtil;

import java.io.Serializable;
import java.sql.Timestamp;

public class Video implements Serializable, Parcelable {
    private int id;
    private String name;
    private String title;
    private String url;
    //数据库中数据类型datetime对应java中Timestamp
    private Timestamp datetime;
    private int type;

    public Video(){
    }

    public Video(int id, String name, String title, String url , Timestamp datetime,int type){
        this.id = id;
        this.name = name;
        this.title = title;
        this.url = url;
        this.datetime = datetime;
        this.type=type;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    //将localhost改为本机ip地址
    public String getUrl() {
        String r_url = url.replace("localhost", DBUtil.ip);
        return r_url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Timestamp getDatetime() {
        return datetime;
    }

    public void setDatetime(Timestamp datetime) {
        this.datetime = datetime;
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
        parcel.writeString(name);
        parcel.writeString(title);
        parcel.writeString(url);
        parcel.writeString(datetime.toString());
        parcel.writeInt(type);
    }


    public static final Parcelable.Creator<Video> CREATOR = new Creator<Video>() {
        @Override
        public Video createFromParcel(Parcel parcel) {
            Video video =new Video();
            video.setId(parcel.readInt());
            video.setName(parcel.readString());
            video.setTitle(parcel.readString());
            video.setUrl(parcel.readString());
            video.setDatetime(Timestamp.valueOf(parcel.readString()));
            video.setType(parcel.readInt());
            return video;
        }

        @Override
        public Video[] newArray(int i) {
            return new Video[i];
        }
    };
}
