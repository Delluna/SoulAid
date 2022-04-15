package com.example.soulaid.dao;

import com.example.soulaid.entity.ChatMessage;
import com.example.soulaid.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ChatMessageDao {

    public ChatMessageDao(){}

    //找到某一位教师与某一位学生的所有聊天记录
    public List<ChatMessage> getAllChats(String tname,String uname){
        List<ChatMessage> contents=new ArrayList<>();
        String sql = "select * from chat_messages where tname = ? and uname = ?";


        DBUtil dbUtil = new DBUtil();
        Connection connection = dbUtil.getCon();

        ChatMessage content = null;

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1,tname);
            preparedStatement.setString(2,uname);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                String message = resultSet.getString(2);
                String teacher = resultSet.getString(3);
                String user = resultSet.getString(4);
                int type = resultSet.getInt(5);
                content = new ChatMessage(id,message,teacher,user,type);
                contents.add(content);
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

    public  void addChat(ChatMessage message){
        String sql = "insert into chat_messages(message,tname,uname,type) values(?,?,?,?)";

        DBUtil dbUtil = new DBUtil();
        Connection connection = dbUtil.getCon();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1,message.getMessage());
            preparedStatement.setString(2,message.getTname());
            preparedStatement.setString(3,message.getUname());
            preparedStatement.setInt(4,message.getType());
            int res = preparedStatement.executeUpdate();
            if (res==1) {
                System.out.println("addChat执行成功！");
            }
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbUtil.closeCon();
        }
    }
}
