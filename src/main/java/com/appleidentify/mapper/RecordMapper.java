package com.appleidentify.mapper;

import com.appleidentify.table.Record;
import com.appleidentify.table.Stock;
import com.appleidentify.table.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface RecordMapper {
    /**
     * 创建新库存变动信息
     */
    @Insert("insert into record(fruitID, warehouseID, userID, time, number, isIn, quality) " +
            "value(#{fruitID},#{warehouseID},#{userID},#{time},#{number},#{isIn},#{quality})")
    public int register(Record record);

    /**
     * 查找所有数据
     */
    @Select("select * from record")
    public List<Record> listAllRecord();

    /**
     * 查找是否已有库存变动信息,通过fruitID
     */
    @Select("select * from record where fruitID=#{fruitID}")
    public List<Record> existRecordByFruitID(Integer fruitID);

    /**
     * 查找是否已有库存变动信息,通过warehouseID
     */
    @Select("select * from record where warehouseID=#{warehouseID}")
    public List<Record> existRecordByWarehouseID(Integer warehouseID);

    /**
     * 查找是否已有库存变动信息,通过userID
     */
    @Select("select * from record where userID=#{userID}")
    public List<Record> existRecordByUserID(Integer userID);

    /**
     * 查找是否已有库存变动信息，通过储存信息ID
     */
    @Select("select * from record where recordID=#{recordID}")
    public Record existRecordByID(Integer recordID);


    /**
     * 删除库存变动信息
     */
    @Delete("delete from record where recordID = #{recordID}")
    public Integer delete(Integer recordID);

    /**
     * 修改
     * @param record
     * @return
     */
    @Update("update record set fruitID=#{fruitID}, warehouseID=#{warehouseID}, userID=#{userID}, time=#{time}," +
            " number=#{number}, isIn=#{isIn}, quality=#{quality} where recordID=#{recordID}")
    public Integer update(Record record);
}
