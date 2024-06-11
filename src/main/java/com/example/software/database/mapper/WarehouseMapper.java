package com.example.software.database.mapper;

import com.example.software.database.table.Warehouse;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface WarehouseMapper {
    /**
     * 创建新仓库
     */
    @Insert("insert into warehouse(name,telephone,location) value(#{name},#{telephone},#{location})")
    public int register(Warehouse newWarehouse);

    /**
     * 查找所有数据
     */
    @Select("select * from warehouse")
    public List<Warehouse> listAllWarehouse();

    /**
     * 查找是否已有仓库,通过仓库名
     */
    @Select("select * from warehouse where name=#{name}")
    public Warehouse existWarehouseByName(String name);

    /**
     * 查找是否已有仓库，通过仓库ID
     */
    @Select("select * from warehouse where warehouseID=#{warehouseID}")
    public Warehouse existWarehouseByID(Integer warehouseID);


    /**
     * 删除仓库
     */
    @Delete("delete from warehouse where warehouseID = #{warehouseID}")
    public Integer delete(Integer warehouseID);

    /**
     * 修改
     * @param warehouse
     * @return
     */
    @Update("update warehouse set name=#{name}, telephone=#{telephone}, location=#{location} where warehouseID=#{warehouseID}")
    public Integer update(Warehouse warehouse);
}
