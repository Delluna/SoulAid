package com.example.soulaid.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Question implements Serializable, Parcelable {
    private int position;
    private String question;

    public Question(){}
    public Question(int position, String question){
        this.position=position;
        this.question=question;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(position);
        parcel.writeString(question);
    }
    public static final Parcelable.Creator<Question> CREATOR = new Creator<Question>() {
        @Override
        public Question createFromParcel(Parcel parcel) {
            Question questions =new Question();
            questions.setPosition(parcel.readInt());
            questions.setQuestion(parcel.readString());
            return questions;
        }

        @Override
        public Question[] newArray(int i) {
            return new Question[i];
        }
    };
}
