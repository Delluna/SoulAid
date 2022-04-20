package com.example.soulaid.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.sql.Timestamp;

public class Scale implements Parcelable, Serializable {
    private int id;
    private String name;
    private int type;
    private String description;
    private int answerNumber;
    private String answerDeatail;
    private String answerAnalysis;

    public Scale(){}

    public Scale(int id,String name,int type,String description,int answerNumber,String answerDeatail,String answerAnalysis){
        this.id=id;
        this.name=name;
        this.type=type;
        this.description=description;
        this.answerNumber=answerNumber;
        this.answerDeatail=answerDeatail;
        this.answerAnalysis=answerAnalysis;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAnswerNumber() {
        return answerNumber;
    }

    public void setAnswerNumber(int answerNumber) {
        this.answerNumber = answerNumber;
    }

    public String getAnswerDeatail() {
        return answerDeatail;
    }

    public void setAnswerDeatail(String answerDeatail) {
        this.answerDeatail = answerDeatail;
    }

    public String getAnswerAnalysis() {
        return answerAnalysis;
    }

    public void setAnswerAnalysis(String answerAnalysis) {
        this.answerAnalysis = answerAnalysis;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeInt(type);
        parcel.writeString(description);
        parcel.writeInt(answerNumber);
        parcel.writeString(answerDeatail);
        parcel.writeString(answerAnalysis);
    }
    public static final Parcelable.Creator<Scale> CREATOR = new Creator<Scale>() {
        @Override
        public Scale createFromParcel(Parcel parcel) {
            Scale scale =new Scale();
            scale.setId(parcel.readInt());
            scale.setName(parcel.readString());
            scale.setType(parcel.readInt());
            scale.setDescription(parcel.readString());
            scale.setAnswerNumber(parcel.readInt());
            scale.setAnswerDeatail(parcel.readString());
            scale.setAnswerAnalysis(parcel.readString());
            return scale;
        }

        @Override
        public Scale[] newArray(int i) {
            return new Scale[i];
        }
    };

}
