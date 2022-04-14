package com.example.soulaid.dao;

import com.example.soulaid.entity.ArticleDetail;
import com.example.soulaid.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class UserFavoriteDao {
    private DBUtil dbUtil;
    private Connection connection;

    //获取某一用户收藏文章的个数
    public int getContentsNumber(String username) {
        int number = 0;
        dbUtil = new DBUtil();
        connection = dbUtil.getCon();
        PreparedStatement pS = null;
        try {
            String check = "select * from user_favorite where uid = (select id from user_message where username = ?)";
            pS = connection.prepareStatement(check);
            pS.setString(1, username);
            //ResultSet获取记录数
            ResultSet resultSet = pS.executeQuery();
            while (resultSet.next()) {
                number++;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbUtil.closeCon();
        }
        return number;
    }

    //获取收藏列表
    public List<ArticleDetail> getFavoriteContents(String username) {
        List<ArticleDetail> contents = new ArrayList<>();
        String sql = "select * from home_content where contentId in (select cid from user_favorite where uid in (select id from user_message where username= ? ))";

        DBUtil dbUtil = new DBUtil();
        Connection connection = dbUtil.getCon();

        ArticleDetail articleDetail = null;

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);
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

    //添加文章至用户收藏(这里第二个参数选择title,因此title不可重复)
    public int addToFavorite(String username, String contentTitle) {
        int state = -1;
        dbUtil = new DBUtil();
        connection = dbUtil.getCon();
        PreparedStatement pS = null;
        try {
            String check = "select * from user_favorite where uid = (select id from user_message where username = ?) and cid = (select contentId from home_content where title = ?)";
            pS = connection.prepareStatement(check);
            pS.setString(1, username);
            pS.setString(2, contentTitle);
            ResultSet rS = pS.executeQuery();
            //如果同一用户已收藏过，则不在添加
            if (rS.next()) {
                rS.close();
                pS.close();
                state = 0;
            } else {
                //添加数据
                String sql = "insert into user_favorite (uid,cid) values ((select id from user_message where username = ?),(select contentId from home_content where title = ?))";
                pS = connection.prepareStatement(sql);
                pS.setString(1, username);
                pS.setString(2, contentTitle);

                int value = pS.executeUpdate();
                if (value > 0) {
                    pS.close();
                    state = 1;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbUtil.closeCon();
        }
        return state;
    }

    //删除文章from用户收藏(这里第二个参数选择title,因此title不可重复)
    public int deleteFromFavorite(String username, String contentTitle) {
        int state = 2;
        dbUtil = new DBUtil();
        connection = dbUtil.getCon();
        PreparedStatement pS = null;
        try {
            //删除数据
            String sql = "delete from user_favorite where uid = (select id from user_message where username = ?) and cid = (select contentId from home_content where title = ? )";
            pS = connection.prepareStatement(sql);
            pS.setString(1, username);
            pS.setString(2, contentTitle);

            int value = pS.executeUpdate();
            if (value > 0) {
                pS.close();
                state = 3;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbUtil.closeCon();
        }
        return state;
    }
}