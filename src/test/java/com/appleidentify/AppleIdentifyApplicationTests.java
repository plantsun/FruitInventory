package com.appleidentify;

import com.appleidentify.mapper.UserMapper;
import com.appleidentify.mapper.WarehouseMapper;
import com.appleidentify.table.User;
import com.appleidentify.table.Warehouse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AppleIdentifyApplicationTests {

    @Autowired
    private UserMapper userMapper;


    @Test
    void contextLoads() {
    }

    @Test
    public void testRegister(){
        User user = new User("fzy" , "123456" );
        Integer flag1 = user.checkUser();
        if (flag1 == 0) System.out.println("用户名或密码格式有误");
        User user1 = userMapper.existUser(user);
        if (user1 != null) {System.out.println("该用户名已存在");user = user1;}
        int flag3 = userMapper.register(user);
        if (flag3 == 1){
            System.out.println("注册成功") ;
        }else {
            System.out.println("注册失败");
        }

    }



}
