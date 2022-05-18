package com.example.soulaid.dao;

import com.example.soulaid.entity.MomentDetail;
import com.example.soulaid.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MomentsDao {

    private int number=5 ;//每次取出moment的个数

    private DBUtil dbUtil;
    private Connection connection;

    public MomentsDao(){
    }

    //每次获取number个moment，position表示最后一次取出的moment的位置
    public List<MomentDetail> getMoments() {
        List<MomentDetail> moments = new ArrayList<>();
        dbUtil = new DBUtil();
        connection = dbUtil.getCon();

        MomentDetail momentDetail;

//        String sql = "select * from moments order by date desc offset "+position+" rows fetch next "+number+" rows only";
        String sql = "select * from moments";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                momentDetail = new MomentDetail();
                momentDetail.setId(resultSet.getInt(1));
                momentDetail.setUname(resultSet.getString(2));
                momentDetail.setTitle(resultSet.getString(3));
                momentDetail.setContent(resultSet.getString(4));
                momentDetail.setDatetime(resultSet.getTimestamp(5));
                momentDetail.setLiked(resultSet.getInt(6));
                moments.add(momentDetail);
            }
            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbUtil.closeCon();
        }
        return moments;
    }

    //点赞数加一
    public boolean addLikedCount(int id) {
        boolean state = false;

        dbUtil = new DBUtil();
        connection=dbUtil.getCon();

        String sql = "update moments set liked = liked +1 where id = ?";

        try {
            PreparedStatement preparedStatement=connection.prepareStatement(sql);
            preparedStatement.setInt(1,id);

            int resultNumber =preparedStatement.executeUpdate();
            if(resultNumber!=0){
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

    //点赞数减一
    public boolean subtractionLikedCount(int id) {
        boolean state = false;

        dbUtil = new DBUtil();
        connection=dbUtil.getCon();

        String sql = "update moments set liked = liked -1 where id = ? and liked > 0";

        try {
            PreparedStatement preparedStatement=connection.prepareStatement(sql);
            preparedStatement.setInt(1,id);

            int resultNumber =preparedStatement.executeUpdate();
            if(resultNumber!=0){
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

    public boolean addMoment(String username,String title,String content){
        boolean state=false;
        dbUtil = new DBUtil();
        connection=dbUtil.getCon();

        String sql = "insert into moments(username,title,content) values(?,?,?)";

        try {
            PreparedStatement preparedStatement=connection.prepareStatement(sql);
            preparedStatement.setString(1,username);
            preparedStatement.setString(2,title);
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

    public boolean deleteMoment(int id){
        boolean state=false;
        dbUtil = new DBUtil();
        connection=dbUtil.getCon();

        String sql = "delete from moments where id = ?";

        try {
            PreparedStatement preparedStatement=connection.prepareStatement(sql);
            preparedStatement.setInt(1,id);

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

    public MomentDetail getLastMoment(){
        dbUtil = new DBUtil();
        connection=dbUtil.getCon();
        MomentDetail momentDetail=new MomentDetail();

        String sql = "select top 1 * from moments order by id desc";

        try {
            PreparedStatement preparedStatement=connection.prepareStatement(sql);

            ResultSet resultSet =preparedStatement.executeQuery();
            while (resultSet.next()){

                momentDetail.setId(resultSet.getInt(1));
                momentDetail.setUname(resultSet.getString(2));
                momentDetail.setTitle(resultSet.getString(3));
                momentDetail.setContent(resultSet.getString(4));
                momentDetail.setDatetime(resultSet.getTimestamp(5));
                momentDetail.setLiked(resultSet.getInt(6));
            }
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            dbUtil.closeCon();
        }
        return momentDetail;

    }
}
