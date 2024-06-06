package com.appleidentify.applejdbc;

import com.appleidentify.table.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class userJbdc {
    public Connection connectJdbc() throws Exception {
        //注册驱动
        Class.forName("com.mysql.cj.jdbc.Driver");
        //2.获取连接对象
        String url = "jdbc:mysql://localhost:3306/appleidentify";
        String username = "root";
        String password = "sunny!!8246";
        Connection connection = DriverManager.getConnection(url,username,password);
        return connection;
    }

    public String register(User user) throws Exception {
        //检查用户名和密码是否合法
        user.checkUser();
        //获取链接
        Connection connection = connectJdbc();
        //获取已有用户
        String sql1 = "select username from user";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql1);
        List<String> existUser = new ArrayList<>();
        while (resultSet.next()){
            String username = resultSet.getString("username");
            existUser.add(username);
        }
        statement.close();
        connection.close();
        return "注册成功";
    }
}
