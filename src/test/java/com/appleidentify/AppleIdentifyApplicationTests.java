package com.appleidentify;


import com.appleidentify.Forms.RecordList;
import com.appleidentify.Forms.StockList;
import com.appleidentify.Forms.SupplyList;
import com.appleidentify.mapper.StockMapper;
import com.appleidentify.mapper.UserMapper;
import com.appleidentify.serves.Business;
import com.appleidentify.table.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;


@SpringBootTest

class AppleIdentifyApplicationTests {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    StockMapper stockMapper;
    @Autowired
    private Business business;

    @Test
    void contextLoads() {
    }

    @Test
    public void testRegister(){
        String  result = business.register("user12", "pass12");;
        System.out.println(result);
    }

    @Test
    public void testLogin(){
        String  result = business.login("user12", "pass12");;
        System.out.println(result);
    }

    @Test
    public void testCheckStock(){
        String  result = business.checkStock("苹果","1",10, 100);
        System.out.println(result);
    }

    @Test
    public void testStorage(){
        business.login("user12", "pass12");
        String result[] = business.storage("苹果", "1", new Timestamp((new Date()).getTime()), 10,"合格");
        System.out.println(result[0]);
        System.out.println(result[1]);
    }

    @Test
    public void testOutStorage(){
        business.login("user12", "pass12");
        String result[] = business.outStorage("橙子", "1", new Timestamp((new Date()).getTime()), 10,"合格");
        System.out.println(result[0]);
        System.out.println(result[1]);
    }

    @Test
    public void testSelectFruit(){
        business.login("user12", "pass12");
        List<StockList>  result = business.selectFruit("合格");
        System.out.println(result);
    }

    @Test
    public void testGetAllRecord(){
        business.login("user12", "pass12");
        List<RecordList> result = business.getAllRecord();
        System.out.println(result);
    }

    @Test
    public void testGetAllStock(){
        business.login("user12", "pass12");
        List<StockList> result = business.getAllStock();
        System.out.println(result);
    }

    @Test
    public void testCheckAdmin(){
        business.login("user9", "pass9");
        boolean result = business.checkAdmin();
        System.out.println(result);
    }

    @Test
    public void testGetAllSupplier(){
        business.login("user9", "pass9");
        List<Supplier> result = business.getAllSupplier();
        System.out.println(result);
    }

    @Test
    public void testAddSupplier(){
        business.login("user9", "pass9");
        String result = business.addSupplier("供应商9", "12312312312", "xian");
        System.out.println(result);
    }

    @Test
    public void testGetSupplierByFruit(){
        business.login("user9", "pass9");
        List<Supplier> result = business.getSupplierByFruit("苹果");
        System.out.println(result);
    }

    @Test
    public void testDeleteSupplier(){
        business.login("user9", "pass9");
        String result = business.deleteSupplier(41);
        System.out.println(result);
    }

    @Test
    public void testUpdateSupplier(){
        business.login("user9", "pass9");
        String result = business.updateSupplier(41,"供应商9", "12312312312", "xian");
        System.out.println(result);
    }

    @Test
    public void testGetAllWarehouse(){
        business.login("user9", "pass9");
        List<Warehouse> result = business.getAllWarehouse();
        System.out.println(result);
    }

    @Test
    public void testAddWarehouse(){
        business.login("user9", "pass9");
        String result = business.addWarehouse("test3","2193719283", "xian");
        System.out.println(result);
    }

    @Test
    public void testDeleteWarehouse(){
        business.login("user9", "pass9");
        String result = business.deleteWarehouse(45);
        System.out.println(result);
    }

    @Test
    public void testUpdateWarehouse(){
        business.login("user9", "pass9");
        String result = business.updateWarehouse(44,"test1","2193719283", "xian");
        System.out.println(result);
    }

    @Test
    public void testGetAllUser(){
        business.login("user9", "pass9");
        List<User> result = business.getAllUser();
        System.out.println(result);
    }

    @Test
    public void testDeleteUser(){
        business.login("user9", "pass9");
        String result = business.deleteUser(29);
        System.out.println(result);
    }

    @Test
    public void testUpdateUser(){
        business.login("user9", "pass9");
        String result = business.updateUser(31,"user12","pass12",(short)0,31);
        System.out.println(result);
    }

    @Test
    public void testGetAllFruit(){
        business.login("user9", "pass9");
        List<Fruit> result = business.getAllFruit();
        System.out.println(result);
    }

    @Test
    public void testAddFruit(){
        business.login("user9", "pass9");
        String result = business.addFruit("荔枝", "请你荔枝一点","荔枝");
        System.out.println(result);
    }

    @Test
    public void testDeleteFruit(){
        business.login("user9", "pass9");
        String result = business.deleteFruit(28);
        System.out.println(result);
    }

    @Test
    public void testUpdateFruit(){
        business.login("user9", "pass9");
        String result = business.updateFruit(29,"荔枝", "请你荔枝一点","荔枝");
        System.out.println(result);
    }

    @Test
    public void testGetAllSupply(){
        business.login("user9", "pass9");
        List<SupplyList> result = business.getAllSupply();
        System.out.println(result);
    }

    @Test
    public void testAddSupply(){
        business.login("user9", "pass9");
        String result = business.addSupply("荔枝", "供应商1", (float) 100.1, 100);
        System.out.println(result);
    }

    @Test
    public void testDeleteSupply(){
        business.login("user9", "pass9");
        String result = business.deleteSupply(12);
        System.out.println(result);
    }

    @Test
    public void testUpdateSupply(){
        business.login("user9", "pass9");
        String result = business.updateSupply(13,"荔枝", "供应商1", (float) 100.1, 100);
        System.out.println(result);
    }




}
