package com.example.soulaid.dao;

import com.example.soulaid.entity.Question;
import com.example.soulaid.entity.Scale;
import com.example.soulaid.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class QuestionsDao {
    private String tablename;

    private DBUtil dbUtil;
    private Connection connection;

    public QuestionsDao(){}

    public List<Question> getQuestions(String name) {

        switch (name){
            case "大五人格问卷简式版(NEO-FFI)":
                tablename="NEO_FFI";
                break;
            case "症状自评量表SCL-90":
                tablename="SCL_90";
                break;
            case "人际关系综合诊断量表":
                tablename="IRAS";
                break;
            case "亲密关系体验量表":
                tablename="ECR";
                break;
        }

        List<Question> questions=new ArrayList<>();
        dbUtil = new DBUtil();
        connection = dbUtil.getCon();

        Question question;
        System.out.println(tablename);
        String sql="select * from "+tablename;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int positioin = resultSet.getInt(1);
                String q = resultSet.getString(2);

                question = new Question(positioin,q);
                questions.add(question);
            }
            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbUtil.closeCon();
        }
        return questions;
    }
}
