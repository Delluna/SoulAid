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
    private int num=8;  //每次取出文章的个数
    private int typenumber=4; //心理健康文章类型数
    private int [] ratio={1,1,1,1};  //每类文章占比
    private int [] e_num={2,2,2,2};  //每类文章应取个数
    private DBUtil dbUtil;
    private Connection connection;

    public ArticlesDao(){
        dbUtil = new DBUtil();
        connection = dbUtil.getCon();
        init_e_num();
    }

    public ArticlesDao(int num,int [] ratio){
        dbUtil = new DBUtil();
        connection = dbUtil.getCon();
        this.num=num;
        this.ratio=ratio;
        init_e_num();
    }

    private void init_e_num(){
        double count =0;   //count不可以为int类型 参考https://blog.csdn.net/weixin_39895486/article/details/114052309
        for(int i=0;i<typenumber;i++){
            count+=ratio[i];
        }

        for(int i=0;i<typenumber;i++){
            e_num[i]= (int)(num*(ratio[i]/count));
        }
    }
    //添加文章
    public boolean addContent(String name, String title, String content) {
        if (title == null || content == null) return false;


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

    //按一定比例随机获取num个文章
    public List<ArticleDetail> getContents(){
        List<ArticleDetail> contents=new ArrayList<>();
        ArticleDetail articleDetail = null;


        try {
            for(int i=1;i<=typenumber;i++){
                System.out.println(e_num[i-1]);
                String sql = "select top "+e_num[i-1]+" * from home_content where type = ? order by NEWID()";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInt(1,i);
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
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbUtil.closeCon();
        }
        return contents;
    }
}
