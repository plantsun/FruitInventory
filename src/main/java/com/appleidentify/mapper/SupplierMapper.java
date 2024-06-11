package com.appleidentify.mapper;

import com.appleidentify.table.Fruit;
import com.appleidentify.table.Supplier;
import com.appleidentify.table.User;
import com.appleidentify.table.Warehouse;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface SupplierMapper {
    /**
     * 创建新供应商
     */
    @Insert("insert into supplier(name,telephone,location) value(#{name},#{telephone},#{location})")
    public int register(Supplier newSupplier);

    /**
     * 查找所有数据
     */
    @Select("select * from supplier")
    public List<Supplier> listAllSupplier();

    /**
     * 查找是否已有供应商,通过供应商名
     */
    @Select("select * from supplier where name=#{name}")
    public Supplier existSupplierByName(String name);

    /**
     * 查找是否已有供应商，通过供应商ID
     */
    @Select("select * from supplier where supplierID=#{supplierID}")
    public Supplier existSupplierByID(Integer supplierID);

    /**
     * 查找是否已有供应商，通过水果ID
     */
    @Select("select * from supplier where supplierID in (select supply.supplierID from supply where supply.fruitID=#{fruitID})")
    public List<Supplier> existSupplierByFruitID(Integer fruitID);


    /**
     * 删除供应商
     */
    @Delete("delete from supplier where supplierID = #{supplierID}")
    public Integer delete(Integer supplierID);

    /**
     * 修改
     * @param supplier
     * @return
     */
    @Update("update supplier set name=#{name}, telephone=#{telephone}, location=#{location} where supplierID=#{supplierID}")
    public Integer update(Supplier supplier);
}
