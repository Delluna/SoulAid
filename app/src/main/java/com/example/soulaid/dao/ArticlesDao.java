package com.example.soulaid.dao;

import com.example.soulaid.entity.ArticleDetail;
import com.example.soulaid.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class ArticlesDao {
    private DBUtil dbUtil;
    private Connection connection;

    //添加文章
    public boolean addContent(String name, String title, String content) {
        if (title == null || content == null) return false;

        dbUtil = new DBUtil();
        connection = dbUtil.getCon();
        PreparedStatement pS = null;

        try {
            if(name==null){
                String sql1 = "insert into home_content (title,content) values (?,?)";
                pS = connection.prepareStatement(sql1);
                pS.setString(1,title);
                pS.setString(2,content);
            }else {
                String sql2 = "insert into home_content (name,title,content) values (?,?,?)";
                pS = connection.prepareStatement(sql2);
                pS.setString(1,name);
                pS.setString(2,title);
                pS.setString(3,content);
            }

            int value = pS.executeUpdate();
            if (value > 0) {
                pS.close();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbUtil.closeCon();
        }
        return false;
    }

    //获取文章数量
    public int getContentsNumber(){
        dbUtil = new DBUtil();
        connection = dbUtil.getCon();
        int count=0;
        String sql = "select * from home_content";
        try {
            Statement statement=connection.createStatement();
            ResultSet resultSet=statement.executeQuery(sql);
            while (resultSet.next()){
                count++;
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            dbUtil.closeCon();
        }
        return count;
    }

    //通过id查询文章，文章id从1开始
    public ArticleDetail findContentById(int id) {

        String sql = "select * from home_content where contentId = ?";

        DBUtil dbUtil = new DBUtil();
        Connection connection = dbUtil.getCon();

        ArticleDetail articleDetail = null;

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1,id);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int contentId = resultSet.getInt(1);
                String name = resultSet.getString(2);
                String title = resultSet.getString(3);
                String content = resultSet.getString(4);
                Timestamp datetime = resultSet.getTimestamp(5);
                articleDetail = new ArticleDetail(contentId, name, title, content, datetime);
            }else {

            }
            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbUtil.closeCon();
        }
        return articleDetail;
    }

    //获取num个文章
    public List<ArticleDetail> getContents(int num){
        List<ArticleDetail> contents=new ArrayList<>();
        String sql = "select top "+num+" * from home_content order by issueDate desc";

        DBUtil dbUtil = new DBUtil();
        Connection connection = dbUtil.getCon();

        ArticleDetail articleDetail = null;

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int contentId = resultSet.getInt(1);
                String name = resultSet.getString(2);
                String title = resultSet.getString(3);
                String content = resultSet.getString(4);
                Timestamp datetime = resultSet.getTimestamp(5);
                articleDetail = new ArticleDetail(contentId, name, title, content, datetime);
                contents.add(articleDetail);
            }
            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbUtil.closeCon();
        }
        return contents;
    }
}
