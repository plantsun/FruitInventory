package com.appleidentify.table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Integer userId;//用户ID
    private String userName;//用户名
    private String password;//密码
    private Short priority;//权限 0为管理员， 1为普通用户
    private Integer warehouseID;//所属仓库ID


    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
        this.priority =0;
        this.warehouseID = null;
    }
    /**
    * 检查用户名和密码是否合法
    * */
    public Integer checkUser(){
        return 1;
    }

}
