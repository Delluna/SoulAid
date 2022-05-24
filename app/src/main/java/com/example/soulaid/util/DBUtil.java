package com.example.soulaid.util;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {

    public static String ip="192.168.101.28";
    private String driverName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    private String dbURL = "jdbc:jtds:sqlserver://"+ip+":1433;DatabaseName=DB_SOULAID";//数据库连接url
    private String dbName = "SoulAid";//数据库用户名
    private String dbPassword = "SoulAid";//数据库密码
    private Connection con = null;

    public DBUtil() {
//        try {
//            InetAddress ip4 = Inet4Address.getLocalHost();
//            ip=ip4.toString();
//            System.out.println(ip);
//
//        } catch (UnknownHostException e) {
//            e.printStackTrace();
//        }
    }

    //数据库连接
    public Connection getCon() {
        try {
            Class.forName(driverName);//加载数据库驱动
            System.out.println("数据库驱动加载成功！");
            con = DriverManager.getConnection(dbURL, dbName, dbPassword);  //连接数据库
            System.out.println("成功地获取数据库连接！");
        } catch (Exception e) {
            System.out.println("创建数据库连接失败！");
            e.printStackTrace();
        }
        return con;
    }

    //数据库关闭
    public void closeCon(){
        try {
            con.close();
            System.out.println("成功关闭数据库连接");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}