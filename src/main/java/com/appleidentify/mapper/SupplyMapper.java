package com.appleidentify.mapper;

import com.appleidentify.table.*;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface SupplyMapper {
    /**
     * 创建新供应信息
     */
    @Insert("insert into supply(fruitID, supplierID, price, number) value(#{fruitID},#{supplierID},#{price},#{number})")
    public int register(Supply supply);

    /**
     * 查找所有数据
     */
    @Select("select * from supply")
    public List<Supply> listAllSupply();

    /**
     * 查找是否已有供应信息,通过供应商ID
     */
    @Select("select * from supply where supplierID=#{supplierID}")
    public List<Supply> existSupplyBySupplierID(Integer supplierID);

    /**
     * 查找是否已有供应信息,通过水果ID
     */
    @Select("select * from supply where fruitID=#{fruitID}")
    public List<Supply> existSupplyByFruitID(Integer fruitID);


    /**
     * 查找是否已有供应信息，通过供应信息ID
     */
    @Select("select * from supply where ID=#{ID}")
    public Supply existSupplyByID(Integer ID);


    /**
     * 删除供应信息
     */
    @Delete("delete from supply where ID = #{ID}")
    public Integer delete(Integer ID);

    /**
     * 修改
     * @param supply
     * @return
     */
    @Update("update supply set fruitID=#{fruitID}, supplierID=#{supplierID}, price=#{price}, number=#{number} where ID=#{ID}")
    public Integer update(Supply supply);
}
