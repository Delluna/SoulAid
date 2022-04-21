package com.example.soulaid.dao;

import com.example.soulaid.entity.Comment;
import com.example.soulaid.entity.MomentDetail;
import com.example.soulaid.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CommentsDao {
    private DBUtil dbUtil;
    private Connection connection;

    public CommentsDao(){
        dbUtil = new DBUtil();
        connection = dbUtil.getCon();
    }

    public List<Comment> getComments(int mid){
        List<Comment> comments=new ArrayList<>();
        Comment comment;

        String sql = "select * from comments where mid = ? order by id desc";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1,mid);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                comment = new Comment();
                comment.setId(resultSet.getInt(1));
                comment.setUname(resultSet.getString(2));
                comment.setMid(resultSet.getInt(3));
                comment.setContent(resultSet.getString(4));
                comment.setDate(resultSet.getTimestamp(5));
                comments.add(comment);
            }
            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbUtil.closeCon();
        }
        return comments;
    }
    public boolean addComment(String username,int mid,String content){
        boolean state=false;

        String sql = "insert into comments(uname,mid,content) values(?,?,?)";
        try {
            PreparedStatement preparedStatement=connection.prepareStatement(sql);
            preparedStatement.setString(1,username);
            preparedStatement.setInt(2,mid);
            preparedStatement.setString(3,content);

            int resultNumber =preparedStatement.executeUpdate();
            if(resultNumber==1){
                state=true;
            }
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            dbUtil.closeCon();
        }
        return state;
    }

    public Comment getLastComment(){
        Comment comment=new Comment();

        String sql = "select top 1 * from comments order by id desc";

        try {
            PreparedStatement preparedStatement=connection.prepareStatement(sql);

            ResultSet resultSet =preparedStatement.executeQuery();
            while (resultSet.next()){

                comment.setId(resultSet.getInt(1));
                comment.setUname(resultSet.getString(2));
                comment.setMid(resultSet.getInt(3));
                comment.setContent(resultSet.getString(4));
                comment.setDate(resultSet.getTimestamp(5));
            }
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            dbUtil.closeCon();
        }
        return comment;

    }
}
