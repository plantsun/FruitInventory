package com.appleidentify.mapper;

import com.appleidentify.table.User;
import org.apache.ibatis.annotations.*;

import java.util.List;


@Mapper
public interface UserMapper {
    /**
     * 创建新用户
     */
    @Insert("insert into user(username,password) " +
            "value(#{userName},#{password})")
    public int register(User newUser);

    /**
     * 查找所有数据
     */
    @Select("select * from user")
    public List<User> listAllUser();

    /**
     * 查找是否已有该用户
     */
    @Select("select * from user where username=#{userName}")
    public User existUser(User user);

    /**
     * 查找是否已有该用户
     */
    @Select("select * from user where username=#{username}")
    public User existUserByName(String username);

    /**
     * 查找是否已有该用户
     */
    @Select("select * from user where userID=#{userID}")
    public User existUserByID(int userID);

    /**
     * 登录验证
     */
    @Select("select count(*) from user where username=#{userName} and password=#{password}")
    public Integer logCheck(User user);

    /**
     * 删除用户
     */
    @Delete("delete from user where userID = #{userId}")
    public Integer delete(Integer userId);

    /**
     * 修改
     * @param user
     * @return
     */
    @Update("update user set username=#{userName}, password=#{password}, " +
            "priority=#{priority}, warehouseID=#{warehouseID} where userID=#{userId}")
    public Integer update(User user);

}
