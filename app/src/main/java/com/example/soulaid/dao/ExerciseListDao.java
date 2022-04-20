package com.example.soulaid.dao;

import com.example.soulaid.entity.ArticleDetail;
import com.example.soulaid.entity.Scale;
import com.example.soulaid.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class ExerciseListDao {
    private DBUtil dbUtil;
    private Connection connection;

    public ExerciseListDao() {
    }

    public List<Scale> getScales(String typename) {
        List<Scale> scales = new ArrayList<>();

        dbUtil = new DBUtil();
        connection = dbUtil.getCon();

        Scale scale;
        String sql = "select * from scale where type = (select id from test_type where typename = ?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, typename);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                String name = resultSet.getString(2);
                int type = resultSet.getInt(3);
                String answerAnalysis = resultSet.getString(4);
                String description = resultSet.getString(5);
                int answerNumber = resultSet.getInt(6);
                String answerDeatail = resultSet.getString(7);
                scale = new Scale(id, name, type, description, answerNumber, answerDeatail, answerAnalysis);
                scales.add(scale);
            }
            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbUtil.closeCon();
        }
        return scales;
    }
}
