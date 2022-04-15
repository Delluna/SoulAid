package com.example.soulaid.dao;

import com.example.soulaid.entity.TeacherMessage;
import com.example.soulaid.entity.UserMessage;
import com.example.soulaid.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MessageDao {

    //登录
    public boolean login(String tableName, String name, String pwd) {
        //定义sql语句，连接数据库，执行sql语句，获取返回结果，执行其他操作，finally关闭数据库的连接
        String sql = " select * from " + tableName + " where username = ? and password = ? ";

        DBUtil dbUtil = new DBUtil();
        Connection connection = dbUtil.getCon();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, pwd);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                resultSet.close();
                preparedStatement.close();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbUtil.closeCon();
        }
        return false;
    }


    //注册账户,return 1表示注册成功，return 0表示注册失败, return -1表示用户名已存在，return -2表示两次密码输入不一致
    public int register(String tableName, String name, String pwd, String pwd2) {
        if (name.equals("") || pwd.equals("") || pwd2.equals("")) return 0;
        if (!pwd.equals(pwd2)) return -2;

        String check = "select * from " + tableName + " where username = ?";
        String sql = "insert into " + tableName + " (username,password) values (?,?)";

        DBUtil dbUtil = new DBUtil();
        Connection connection = dbUtil.getCon();

        try {
            //检测是否用户名重复
            PreparedStatement pS = connection.prepareStatement(check);
            pS.setString(1, name);
            ResultSet rS = pS.executeQuery();
            if (rS.next()) {
                rS.close();
                pS.close();
                return -1;
            }

            //插入用户信息
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, pwd);

            int value = preparedStatement.executeUpdate();
            if (value > 0) {
                preparedStatement.close();
                return 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbUtil.closeCon();
        }
        return 0;
    }

    //修改密码
    public boolean changePassword(String tableName, String name, String pwd) {
        boolean state = false;
        String sql = "update " + tableName + " set password = ? where username = ?";

        DBUtil dbUtil = new DBUtil();
        Connection connection = dbUtil.getCon();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, pwd);
            preparedStatement.setString(2, name);
            System.out.println("正在执行changPasswordfangfa");

            int n = preparedStatement.executeUpdate(); //executeUpdate()返回值为受影响的行数
            if (n != 0) {
                state = true;
            }
            System.out.println("获取受影响的行数:" + n);
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbUtil.closeCon();
        }
        return state;
    }

    //查询byid
    public UserMessage findUserById(String tableName, int id) {
        String sql = "select * from " + tableName + " where id = ?";

        DBUtil dbUtil = new DBUtil();
        Connection connection = dbUtil.getCon();

        UserMessage userMessage = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int uid = resultSet.getInt(1);
                String username = resultSet.getString(2);
                String password = resultSet.getString(3);
                userMessage = new UserMessage(uid, username, password);
            }
            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbUtil.closeCon();
        }
        return userMessage;
    }

    //查询byname，用户信息表user_message、teacher_message、admin_message不允许重名
    public UserMessage findUserByName(String tableName,String name) {
        String sql = "select * from "+tableName+" where username = ?";

        DBUtil dbUtil = new DBUtil();
        Connection connection = dbUtil.getCon();

        UserMessage userMessage = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, name);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int id = resultSet.getInt(1);
                String username = resultSet.getString(2);
                String password = resultSet.getString(3);
                userMessage = new UserMessage(id, username, password);
            }
            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbUtil.closeCon();
        }

        return userMessage;
    }

    public List<TeacherMessage> getTeachers(){

        List<TeacherMessage> teachers=new ArrayList<>();

        String sql = "select * from teacher_message";

        DBUtil dbUtil = new DBUtil();
        Connection connection = dbUtil.getCon();
        TeacherMessage teacher;
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                teacher = new TeacherMessage();
                teacher.setId(resultSet.getInt(1));
                teacher.setUsername(resultSet.getString(2));
                teacher.setPassword(resultSet.getString(3));
                teacher.setTag(resultSet.getInt(4));
                teachers.add(teacher);
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbUtil.closeCon();
        }

        return teachers;
    }
    public List<UserMessage> getUsers(String tname){

        List<UserMessage> users=new ArrayList<>();

        String sql = "select * from user_message where username in (select uname from chat_messages where tname = ?)";

        DBUtil dbUtil = new DBUtil();
        Connection connection = dbUtil.getCon();
        UserMessage user;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,tname);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(!resultSet.next())
                return users;
            else {
                user=new UserMessage();
                user.setId(resultSet.getInt(1));
                user.setUsername(resultSet.getString(2));
                user.setPassword(resultSet.getString(3));
                users.add(user);
            }
            while (resultSet.next()) {
                user=new UserMessage();
                user.setId(resultSet.getInt(1));
                user.setUsername(resultSet.getString(2));
                user.setPassword(resultSet.getString(3));
                users.add(user);
            }
            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbUtil.closeCon();
        }

        return users;
    }
}
