package com.example.software.database.mapper;

import com.example.software.database.table.Stock;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface StockMapper {
    /**
     * 创建新储存信息
     */
    @Insert("insert into stock(fruitID, warehouseID, time, number, quality) value(#{fruitID},#{warehouseID},#{time},#{number},#{quality})")
    public int register(Stock stock);

    /**
     * 查找所有数据
     */
    @Select("select * from stock")
    public List<Stock> listAllStock();

    /**
     * 查找是否已有储存信息,通过fruitID
     */
    @Select("select * from stock where fruitID=#{fruitID}")
    public List<Stock> existStockByFruitID(Integer fruitID);

    /**
     * 查找是否已有储存信息,通过quality筛选水果
     */
    @Select("select * from stock where quality=#{quality}")
    public List<Stock> existStockByQuality(String quality);

    /**
     * 查找是否已有储存信息,通过fruitID和warehouseID和quality
     */
    @Select("select * from stock where fruitID=#{fruitID} and warehouseID=#{warehouseID} and quality=#{quality}")
    public Stock existStockByFruitIDWarehouseIDQuality(@Param("fruitID") Integer fruitID, @Param("warehouseID") Integer warehouseID,@Param("quality") String quality);

    /**
     * 查找是否已有储存信息,通过fruitID和warehouseID
     */
    @Select("select * from stock where fruitID=#{fruitID} and warehouseID=#{warehouseID}")
    public List<Stock> existStockByFruitIDWarehouseID(@Param("fruitID") int fruitID, @Param("warehouseID") int warehouseID);

    /**
     * 查找是否已有储存信息,通过warehouseID
     */
    @Select("select * from stock where warehouseID=#{warehouseID}")
    public List<Stock> existStockByWarehouseID(Integer warehouseID);


    /**
     * 查找是否已有储存信息，通过储存信息ID
     */
    @Select("select * from stock where ID=#{ID}")
    public Stock existStockByID(Integer ID);


    /**
     * 删除储存信息
     */
    @Delete("delete from stock where ID = #{ID}")
    public Integer delete(Integer ID);

    /**
     * 修改
     * @param stock
     * @return
     */
    @Update("update stock set fruitID=#{fruitID}, warehouseID=#{warehouseID}, time=#{time}," +
            " number=#{number}, quality=#{quality} where ID=#{ID}")
    public Integer update(Stock stock);

    /**
     * 查找低于阈值的库存ID列表
     * @param lowLimit
     * @return
     */
    @Select("select ID from stock,(select warehouseID as t1, fruitID as t2 from stock  group by warehouseID,fruitID having SUM(number)<#{lowLimit}) as t where warehouseID = t.t1 and fruitID = t.t2 ")
    public List<Integer> checkStockLow(@Param("lowLimit") Integer lowLimit);

    /**
     * 查找高于阈值的库存ID列表
     * @param highLimit
     * @return
     */
    @Select("select ID from stock,(select warehouseID as t1, fruitID as t2 from stock  group by warehouseID,fruitID having SUM(number)>#{highLimit}) as t where warehouseID = t.t1 and fruitID = t.t2 ")
    public List<Integer> checkStockHigh(@Param("highLimit") Integer highLimit);
}
