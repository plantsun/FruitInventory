package com.appleidentify;

import com.appleidentify.mapper.*;
import com.appleidentify.table.*;
import com.appleidentify.table.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;

import java.sql.Timestamp;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@SpringBootApplication
public class Business {

    private User user;//登录的用户
    @Autowired
    private FruitMapper fruitMapper;
    @Autowired
    private RecordMapper recordMapper;
    @Autowired
    private StockMapper stockMapper;
    @Autowired
    private SupplierMapper supplierMapper;
    @Autowired
    private SupplyMapper supplyMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private WarehouseMapper warehouseMapper;
    @Autowired
    private DataSourceTransactionManager dataSourceTransactionManager;

    @Autowired
    private TransactionDefinition transactionDefinition;

    private int lowLimit;//库存下限
    private int highLimit;//库存上限

//    TransactionStatus transactionStatus = dataSourceTransactionManager.getTransaction(transactionDefinition);
//        try {
//
//        dataSourceTransactionManager.commit(transactionStatus); // 手动提交
//    }catch (Exception e){
//        dataSourceTransactionManager.rollback(transactionStatus); // 事务回滚
//        e.printStackTrace();
//        return null;
//    }

    /**
     * 注册
     * @param password
     * @param username
     * @return
     */
    public String register(String username, String password){
        TransactionStatus transactionStatus = dataSourceTransactionManager.getTransaction(transactionDefinition);
        try {
            user = new User(username, password);
            user.checkUser();//检查用户名和密码格式
            //检查结果
        /* ..return "用户名格式错误"
             return "密码格式错误".. */
            User existUser = userMapper.existUser(user);
            dataSourceTransactionManager.commit(transactionStatus); // 手动提交
            if(existUser == null){
                int flag = userMapper.register(user);
                user = userMapper.existUser(user);
                if(flag == 1){
                    dataSourceTransactionManager.commit(transactionStatus); // 手动提交
                    return "注册成功";
                }else {
                    dataSourceTransactionManager.rollback(transactionStatus); // 事务回滚
                    return "注册失败";
                }
            }else {
                dataSourceTransactionManager.rollback(transactionStatus); // 事务回滚
                return "该用户名已存在";
            }

        }catch (Exception e){
            dataSourceTransactionManager.rollback(transactionStatus); // 事务回滚
            e.printStackTrace();
            return ("异常——————————————————");
        }

    }

    /**
     * 登录
     * @param username
     * @param password
     * @return
     */
    public String login(String username, String password){
        TransactionStatus transactionStatus = dataSourceTransactionManager.getTransaction(transactionDefinition);
        try {
            user = new User(username, password);
            int flag = userMapper.logCheck(user);
            User exist = userMapper.existUser(user);
            dataSourceTransactionManager.commit(transactionStatus); // 手动提交
            if(exist == null){
                dataSourceTransactionManager.rollback(transactionStatus); // 事务回滚
                return "用户不存在";
            }else if(flag != 1){
                dataSourceTransactionManager.rollback(transactionStatus); // 事务回滚
                return "密码错误";
            }else {
                user = exist;
                dataSourceTransactionManager.commit(transactionStatus); // 手动提交
                return "登录成功";
            }
        }catch (Exception e){
            dataSourceTransactionManager.rollback(transactionStatus); // 事务回滚
            e.printStackTrace();
            return ("异常——————————————————");
        }
    }


    public String checkStock(String fruitName, String warehouseName, int lowLimit, int highLimit){
        Fruit fruit = fruitMapper.existFruitByName(fruitName);
        int fruitID = fruit.getFruitID();
        Warehouse warehouse = warehouseMapper.existWarehouseByName(warehouseName);
        int warehouseID = warehouse.getWarehouseID();
        List<Stock> stocks = stockMapper.existStockByFruitIDWarehouseID(fruitID,warehouseID);
        int sum = 0;
        for (Stock stock:stocks) {
            sum = sum + stock.getNumber();
        }
        if(sum <= lowLimit){
            return fruitName+"库存数量不足";
        }else if(sum < highLimit){
            return fruitName+"库存数量合适";
        }else {
            return fruitName+"库存数量过多";
        }

    }

    /**
     * 水果入库
     * @param fruitName
     * @param warehouseName
     * @param time
     * @param number
     * @param quality
     * @return
     */
    public String[] storage(String fruitName, String warehouseName, Timestamp time, int number, String quality){
        TransactionStatus transactionStatus = dataSourceTransactionManager.getTransaction(transactionDefinition);
        String[] results = new String[2];
        try {
            Fruit fruit = fruitMapper.existFruitByName(fruitName);
            int fruitID = fruit.getFruitID();
            Warehouse warehouse = warehouseMapper.existWarehouseByName(warehouseName);
            int warehouseID = warehouse.getWarehouseID();
            int userID = user.getUserId();
            Short isIn = 1;
            Record newRecord = new Record(fruitID, warehouseID, userID, time, number, isIn, quality);
            int flag1 = recordMapper.register(newRecord);
            Stock stock = stockMapper.existStockByFruitIDWarehouseIDQuality(fruitID,warehouseID,quality);
            int flag2;
            if(stock == null){
                stock = new Stock(fruitID, warehouseID, time, number, quality);
                flag2 = stockMapper.register(stock);
            }else {
                stock = new Stock(fruitID, warehouseID, time, stock.getNumber()+number, quality);
                flag2 = stockMapper.update(stock);
            }

            if (flag1==1 && flag2==1){
                dataSourceTransactionManager.commit(transactionStatus); // 手动提交
                results[0] = "入库成功";
                results[1] = checkStock(fruitName,warehouseName,lowLimit,highLimit);
                return results;
            }else {
                dataSourceTransactionManager.rollback(transactionStatus); // 事务回滚
                results[0] = "入库失败";
                results[1] = checkStock(fruitName,warehouseName,lowLimit,highLimit);
                return results;
            }
        }catch (Exception e){
            dataSourceTransactionManager.rollback(transactionStatus); // 事务回滚
            e.printStackTrace();
            results[0] = "异常";
            results[1] = checkStock(fruitName,warehouseName,lowLimit,highLimit);
            return results;
        }

    }

    /**
     * 水果出库
     * @param fruitName
     * @param warehouseName
     * @param time
     * @param number
     * @param quality
     * @return
     */
    public String[] outStorage(String fruitName, String warehouseName, Timestamp time, int number, String quality){
        TransactionStatus transactionStatus = dataSourceTransactionManager.getTransaction(transactionDefinition);
        String[] results = new String[2];
        try {
            Fruit fruit = fruitMapper.existFruitByName(fruitName);
            int fruitID = fruit.getFruitID();
            Warehouse warehouse = warehouseMapper.existWarehouseByName(warehouseName);
            int warehouseID = warehouse.getWarehouseID();
            int userID = user.getUserId();
            Short isIn = 0;
            Record newRecord = new Record(fruitID, warehouseID, userID, time, number, isIn, quality);
            int flag1 = recordMapper.register(newRecord);
            Stock stock = stockMapper.existStockByFruitIDWarehouseIDQuality(fruitID,warehouseID,quality);
            int flag2;
            if(stock ==null){
                dataSourceTransactionManager.rollback(transactionStatus); // 事务回滚
                results[0] = "该水果库存不存在";
                results[1] = checkStock(fruitName,warehouseName,lowLimit,highLimit);
                return results;
            }else if(stock.getNumber() < number){
                dataSourceTransactionManager.rollback(transactionStatus); // 事务回滚
                results[0] = "库存小于出库数量";
                results[1] = checkStock(fruitName,warehouseName,lowLimit,highLimit);
                return results;
            }else {
                stock = new Stock(fruitID, warehouseID, time, stock.getNumber()-number, quality);
                flag2 = stockMapper.update(stock);
            }
            if(flag1==1 && flag2==1){
                dataSourceTransactionManager.commit(transactionStatus); // 手动提交
                results[0] = "出库成功";
                results[1] = checkStock(fruitName,warehouseName,lowLimit,highLimit);
                return results;
            }else {
                dataSourceTransactionManager.rollback(transactionStatus); // 事务回滚
                results[0] = "出库失败";
                results[1] = checkStock(fruitName,warehouseName,lowLimit,highLimit);
                return results;
            }

        }catch (Exception e){
            dataSourceTransactionManager.rollback(transactionStatus); // 事务回滚
            e.printStackTrace();
            results[0] = "异常——————————————————";
            results[1] = checkStock(fruitName,warehouseName,lowLimit,highLimit);
            return results;
        }
    }

    /**
     * 根据质量等级筛选水果
     * @param quality
     * @return
     */
    public List selectFruit(String quality){
        TransactionStatus transactionStatus = dataSourceTransactionManager.getTransaction(transactionDefinition);
        try {
            List<Stock> stockList = stockMapper.existStockByQuality(quality);
            if(stockList != null){
                dataSourceTransactionManager.commit(transactionStatus); // 手动提交
                return stockList;
            }else {
                dataSourceTransactionManager.rollback(transactionStatus); // 事务回滚
                return null;
            }
        }catch (Exception e){
            dataSourceTransactionManager.rollback(transactionStatus); // 事务回滚
            e.printStackTrace();
            return null;
        }
    }

    /*========================================================================================================*/
    /*以下是管理员所有的业务功能*/

    /**
     * 检查权限
     * @return
     */
    public boolean checkAdmin(){
        Short priority = user.getPriority();
        if (priority == 1){
            return true;
        }else {
            return false;
        }
    }


    /**
     * 获取所有供应商信息
     * @return
     */
    public List<Supplier> getAllSupplier(){
        TransactionStatus transactionStatus = dataSourceTransactionManager.getTransaction(transactionDefinition);
        try {
            List<Supplier> supplierList = supplierMapper.listAllSupplier();
            dataSourceTransactionManager.commit(transactionStatus); // 手动提交
            return supplierList;
        }catch (Exception e){
            dataSourceTransactionManager.rollback(transactionStatus); // 事务回滚
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 添加供应商信息
     * @param name
     * @param telephone
     * @param location
     * @return
     */
    public String addSupplier(String name, String telephone, String location){
        TransactionStatus transactionStatus = dataSourceTransactionManager.getTransaction(transactionDefinition);
        try {
            Supplier supplier = new Supplier(name,telephone,location);
            int flag = supplierMapper.register(supplier);
            if(flag != 1){
                dataSourceTransactionManager.commit(transactionStatus); // 手动提交
                return "添加供应商成功";
            }else {
                dataSourceTransactionManager.rollback(transactionStatus);
                return "添加供应商失败";
            }
        }catch (Exception e){
            dataSourceTransactionManager.rollback(transactionStatus); // 事务回滚
            e.printStackTrace();
            return "添加供应商失败";
        }
    }

    /**
     * 删除供应商的信息
     * @param supplierID
     * @return
     */
    public String deleteSupplier(int supplierID){
        TransactionStatus transactionStatus = dataSourceTransactionManager.getTransaction(transactionDefinition);
        try {
            int flag = supplierMapper.delete(supplierID);
            if(flag != 1){
                dataSourceTransactionManager.commit(transactionStatus); // 手动提交
                return "删除供应商成功";
            }else {
                dataSourceTransactionManager.rollback(transactionStatus);
                return "删除供应商失败";
            }
        }catch (Exception e){
            dataSourceTransactionManager.rollback(transactionStatus); // 事务回滚
            e.printStackTrace();
            return "删除供应商失败";
        }
    }

    /**
     * 更新供应商信息
     * @param name
     * @param telephone
     * @param location
     * @return
     */
    public String updateSupplier(int supplierID, String name, String telephone, String location){
        TransactionStatus transactionStatus = dataSourceTransactionManager.getTransaction(transactionDefinition);
        try {
            Supplier supplier = supplierMapper.existSupplierByID(supplierID);
            supplier.setName(name);
            supplier.setTelephone(telephone);
            supplier.setLocation(location);
            int flag = supplierMapper.update(supplier);
            if(flag != 1){
                dataSourceTransactionManager.commit(transactionStatus); // 手动提交
                return "更新供应商成功";
            }else {
                dataSourceTransactionManager.rollback(transactionStatus);
                return "更新供应商失败";
            }
        }catch (Exception e){
            dataSourceTransactionManager.rollback(transactionStatus); // 事务回滚
            e.printStackTrace();
            return "更新供应商失败";
        }
    }

    /**
     * 获取所有仓库信息
     * @return
     */
    public List<Warehouse> getAllWarehouse(){
        TransactionStatus transactionStatus = dataSourceTransactionManager.getTransaction(transactionDefinition);
        try {
            List<Warehouse> warehouseList = warehouseMapper.listAllWarehouse();
            dataSourceTransactionManager.commit(transactionStatus); // 手动提交
            return warehouseList;
        }catch (Exception e){
            dataSourceTransactionManager.rollback(transactionStatus); // 事务回滚
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 添加仓库信息
     * @param name
     * @param telephone
     * @param location
     * @return
     */
    public String addWarehouse(String name, String telephone, String location){
        TransactionStatus transactionStatus = dataSourceTransactionManager.getTransaction(transactionDefinition);
        try {
            Warehouse warehouse = new Warehouse(name,telephone,location);
            int flag = warehouseMapper.register(warehouse);
            if(flag != 1){
                dataSourceTransactionManager.commit(transactionStatus); // 手动提交
                return "添加仓库成功";
            }else {
                dataSourceTransactionManager.rollback(transactionStatus);
                return "添加仓库失败";
            }
        }catch (Exception e){
            dataSourceTransactionManager.rollback(transactionStatus); // 事务回滚
            e.printStackTrace();
            return "添加仓库失败";
        }
    }

    /**
     * 删除仓库信息
     * @param warehouseID
     * @return
     */
    public String deleteWarehouse(int warehouseID){
        TransactionStatus transactionStatus = dataSourceTransactionManager.getTransaction(transactionDefinition);
        try {
            int flag = warehouseMapper.delete(warehouseID);
            if(flag != 1){
                dataSourceTransactionManager.commit(transactionStatus); // 手动提交
                return "删除仓库成功";
            }else {
                dataSourceTransactionManager.rollback(transactionStatus);
                return "删除仓库失败";
            }
        }catch (Exception e){
            dataSourceTransactionManager.rollback(transactionStatus); // 事务回滚
            e.printStackTrace();
            return "删除仓库失败";
        }
    }

    /**
     * 更新仓库信息
     * @param name
     * @param telephone
     * @param location
     * @return
     */
    public String updateWarehouse(int warehouseID, String name, String telephone, String location){
        TransactionStatus transactionStatus = dataSourceTransactionManager.getTransaction(transactionDefinition);
        try {
            Warehouse warehouse = warehouseMapper.existWarehouseByID(warehouseID);
            warehouse.setName(name);
            warehouse.setTelephone(telephone);
            warehouse.setLocation(location);
            int flag = warehouseMapper.update(warehouse);
            if(flag != 1){
                dataSourceTransactionManager.commit(transactionStatus); // 手动提交
                return "更新仓库成功";
            }else {
                dataSourceTransactionManager.rollback(transactionStatus);
                return "更新仓库失败";
            }
        }catch (Exception e){
            dataSourceTransactionManager.rollback(transactionStatus); // 事务回滚
            e.printStackTrace();
            return "更新仓库失败";
        }
    }

    /**
     * 获取所有用户信息
     * @return
     */
    public List<User> getAllUser(){
        TransactionStatus transactionStatus = dataSourceTransactionManager.getTransaction(transactionDefinition);
        try {
            List<User> userList = userMapper.listAllUser();
            dataSourceTransactionManager.commit(transactionStatus); // 手动提交
            return userList;
        }catch (Exception e){
            dataSourceTransactionManager.rollback(transactionStatus); // 事务回滚
            e.printStackTrace();
            return null;
        }

    }

    /**
     * 删除用户信息
     * @param userID
     * @return
     */
    public String deleteUser(int userID){
        TransactionStatus transactionStatus = dataSourceTransactionManager.getTransaction(transactionDefinition);
        try {
            int flag = userMapper.delete(userID);
            if(flag != 1){
                dataSourceTransactionManager.commit(transactionStatus); // 手动提交
                return "删除用户成功";
            }else {
                dataSourceTransactionManager.rollback(transactionStatus);
                return "删除用户失败";
            }
        }catch (Exception e){
            dataSourceTransactionManager.rollback(transactionStatus); // 事务回滚
            e.printStackTrace();
            return "删除用户失败";
        }
    }


    /**
     * * 更新用户信息，（包括用户权限，所属仓库）
     * @param username
     * @param priority
     * @param warehouseID
     * @return
     */
    public String updateUser(int userID, String username, Short priority, int warehouseID){
        TransactionStatus transactionStatus = dataSourceTransactionManager.getTransaction(transactionDefinition);
        try {
            User user0 = userMapper.existUserByID(userID);
            user0.setPriority(priority);
            user0.setWarehouseID(warehouseID);
            int flag = userMapper.update(user0);
            if(flag != 1){
                dataSourceTransactionManager.commit(transactionStatus); // 手动提交
                return "更新用户信息成功";
            }else {
                dataSourceTransactionManager.rollback(transactionStatus);
                return "更新用户信息失败";
            }
        }catch (Exception e){
            dataSourceTransactionManager.rollback(transactionStatus); // 事务回滚
            e.printStackTrace();
            return "更新用户信息失败";
        }
    }

    /**
     * 获取所有水果信息
     * @return
     */
    public List<Fruit> getAllFruit(){
        TransactionStatus transactionStatus = dataSourceTransactionManager.getTransaction(transactionDefinition);
        try {
            List<Fruit> fruitList = fruitMapper.listAllFruit();
            dataSourceTransactionManager.commit(transactionStatus); // 手动提交
            return fruitList;
        }catch (Exception e){
            dataSourceTransactionManager.rollback(transactionStatus); // 事务回滚
            e.printStackTrace();
            return null;
        }

    }

    /**
     * 添加水果信息
     * @param name
     * @param description
     * @param type
     * @return
     */
    public String addFruit(String name, String description, String type){
        TransactionStatus transactionStatus = dataSourceTransactionManager.getTransaction(transactionDefinition);
        try {
            Fruit fruit = new Fruit(name,description,type);
            int flag = fruitMapper.register(fruit);
            if(flag != 1){
                dataSourceTransactionManager.commit(transactionStatus); // 手动提交
                return "添加仓库成功";
            }else {
                dataSourceTransactionManager.rollback(transactionStatus);
                return "添加仓库失败";
            }
        }catch (Exception e){
            dataSourceTransactionManager.rollback(transactionStatus); // 事务回滚
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 删除水果信息
     * @param fruitID
     * @return
     */
    public String deleteFruit(int fruitID){
        TransactionStatus transactionStatus = dataSourceTransactionManager.getTransaction(transactionDefinition);
        try {
            int flag = fruitMapper.delete(fruitID);
            if(flag != 1){
                dataSourceTransactionManager.commit(transactionStatus); // 手动提交
                return "删除水果信息成功";
            }else {
                dataSourceTransactionManager.rollback(transactionStatus);
                return "删除水果信息失败";
            }
        }catch (Exception e){
            dataSourceTransactionManager.rollback(transactionStatus); // 事务回滚
            e.printStackTrace();
            return "删除水果信息失败";
        }
    }


    /**
     * 更新水果信息
     * @param fruitID
     * @param name
     * @param description
     * @param type
     * @return
     */
    public String updateFruit(int fruitID, String name, String description, String type){
        TransactionStatus transactionStatus = dataSourceTransactionManager.getTransaction(transactionDefinition);
        try {
            Fruit fruit = fruitMapper.existFruitByID(fruitID);
            fruit.setName(name);
            fruit.setDescription(description);
            fruit.setType(type);
            int flag = fruitMapper.update(fruit);
            if(flag != 1){
                dataSourceTransactionManager.commit(transactionStatus); // 手动提交
                return "更新水果信息成功";
            }else {
                dataSourceTransactionManager.rollback(transactionStatus);
                return "更新水果信息失败";
            }
        }catch (Exception e){
            dataSourceTransactionManager.rollback(transactionStatus); // 事务回滚
            e.printStackTrace();
            return "更新水果信息失败";
        }
    }


    /**
     * 获取所有供应商供应的水果信息
     * @return
     */
    public List<Supply> getAllSupply(){
        TransactionStatus transactionStatus = dataSourceTransactionManager.getTransaction(transactionDefinition);
        try {
            List<Supply> supplyList = supplyMapper.listAllSupply();
            dataSourceTransactionManager.commit(transactionStatus); // 手动提交
            return supplyList;
        }catch (Exception e){
            dataSourceTransactionManager.rollback(transactionStatus); // 事务回滚
            e.printStackTrace();
            return null;
        }

    }


    /**
     * 添加供应商供应水果信息
     * @param fruitID
     * @param supplierID
     * @param price
     * @param number
     * @return
     */
    public String addSupply(int fruitID, int supplierID, float price, int number){
        TransactionStatus transactionStatus = dataSourceTransactionManager.getTransaction(transactionDefinition);
        try {
            Supply supply = new Supply(fruitID, supplierID, price,number);
            int flag = supplyMapper.register(supply);
            if(flag != 1){
                dataSourceTransactionManager.commit(transactionStatus); // 手动提交
                return "添加供应信息成功";
            }else {
                dataSourceTransactionManager.rollback(transactionStatus);
                return "添加供应信息失败";
            }
        }catch (Exception e){
            dataSourceTransactionManager.rollback(transactionStatus); // 事务回滚
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 删除供应信息
     * @param supplyID
     * @return
     */
    public String deleteSupply(int supplyID){
        TransactionStatus transactionStatus = dataSourceTransactionManager.getTransaction(transactionDefinition);
        try {
            int flag = supplyMapper.delete(supplyID);
            if(flag != 1){
                dataSourceTransactionManager.commit(transactionStatus); // 手动提交
                return "删除供应信息成功";
            }else {
                dataSourceTransactionManager.rollback(transactionStatus);
                return "删除供应信息失败";
            }
        }catch (Exception e){
            dataSourceTransactionManager.rollback(transactionStatus); // 事务回滚
            e.printStackTrace();
            return "删除供应信息失败";
        }
    }

    /**
     * 更新供应信息
     * @param supplyID
     * @param fruitID
     * @param supplierID
     * @param price
     * @param number
     * @return
     */
    public String updateSupply(int supplyID, int fruitID, int supplierID, float price, int number){
        TransactionStatus transactionStatus = dataSourceTransactionManager.getTransaction(transactionDefinition);
        try {
            Supply supply = supplyMapper.existSupplyByID(supplyID);
            supply.setFruitID(fruitID);
            supply.setSupplierID(supplierID);
            supply.setPrice(price);
            supply.setNumber(number);
            int flag = supplyMapper.update(supply);
            if(flag != 1){
                dataSourceTransactionManager.commit(transactionStatus); // 手动提交
                return "更新供应信息成功";
            }else {
                dataSourceTransactionManager.rollback(transactionStatus);
                return "更新供应信息失败";
            }
        }catch (Exception e){
            dataSourceTransactionManager.rollback(transactionStatus); // 事务回滚
            e.printStackTrace();
            return "更新供应信息失败";
        }
    }
}
