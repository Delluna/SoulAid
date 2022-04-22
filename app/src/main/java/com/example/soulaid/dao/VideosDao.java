package com.example.soulaid.dao;

import com.example.soulaid.entity.Video;
import com.example.soulaid.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class VideosDao {
    private DBUtil dbUtil;
    private Connection connection;

    public List<Video> getVideos() {
        dbUtil = new DBUtil();
        connection = dbUtil.getCon();

        List<Video> videos = new ArrayList<>();
        Video video;
        try {
            String sql = "select * from videos order by date desc";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                String name = resultSet.getString(2);
                String title = resultSet.getString(3);
                String url = resultSet.getString(4);
                Timestamp datetime = resultSet.getTimestamp(5);
                int type = resultSet.getInt(6);
                video = new Video(id, name, title, url, datetime, type);
                videos.add(video);
            }
            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbUtil.closeCon();
        }
        return videos;
    }

    //获取题目中含有关键字key的所有视频
    public List<Video> getVideosWithKey(String key) {

        dbUtil = new DBUtil();
        connection = dbUtil.getCon();
        List<Video> videos = new ArrayList<>();
        Video video = null;
        try {
            String sql = "select * from videos where title LIKE ? order by id desc";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            key = "%" + key + "%";
            preparedStatement.setString(1, key);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                String name = resultSet.getString(2);
                String title = resultSet.getString(3);
                String url = resultSet.getString(4);
                Timestamp datetime = resultSet.getTimestamp(5);
                int type = resultSet.getInt(6);
                video = new Video(id, name, title, url, datetime,type);
                videos.add(video);
            }
            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbUtil.closeCon();
        }
        return videos;
    }
}
