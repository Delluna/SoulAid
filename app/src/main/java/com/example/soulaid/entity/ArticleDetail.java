package com.example.soulaid.entity;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.lifecycle.MutableLiveData;

import java.sql.Timestamp;

public class ArticleDetail implements Parcelable {
    private int id;
    private String name;
    private String title;
    private String content;
    //数据库中数据类型datetime对应java中Timestamp
    private Timestamp datetime;
    private boolean isFavorited=false;

    public ArticleDetail(){}
    public ArticleDetail(int id, String name, String title, String content, Timestamp datetime) {
        this.id = id;
        this.name = name;
        this.title = title;
        this.content = content;
        this.datetime = datetime;
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

    public boolean getIsFavorited(){
        return isFavorited;
    }

    public void setIsFavorited(boolean isFavorited){
        this.isFavorited=isFavorited;
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
        parcel.writeString(content);
        parcel.writeString(datetime.toString());
    }
    public static final Parcelable.Creator<ArticleDetail> CREATOR = new Creator<ArticleDetail>() {
        @Override
        public ArticleDetail createFromParcel(Parcel parcel) {
            ArticleDetail articleDetail =new ArticleDetail();
            articleDetail.setId(parcel.readInt());
            articleDetail.setName(parcel.readString());
            articleDetail.setTitle(parcel.readString());
            articleDetail.setContent(parcel.readString());
            articleDetail.setDatetime(Timestamp.valueOf(parcel.readString()));
            return articleDetail;
        }

        @Override
        public ArticleDetail[] newArray(int i) {
            return new ArticleDetail[i];
        }
    };
}
