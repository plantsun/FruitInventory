package com.example.software.database.mapper;

import com.example.software.database.table.Fruit;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FruitMapper {
    /**
     * 创建新水果
     */
    @Insert("insert into fruit(name, description, type) value(#{name},#{description},#{type})")
    public int register(Fruit newFruit);

    /**
     * 查找所有数据
     */
    @Select("select * from Fruit")
    public List<Fruit> listAllFruit();

    /**
     * 查找是否已有水果,通过水果名
     */
    @Select("select * from fruit where name=#{name}")
    public Fruit existFruitByName(String name);

    /**
     * 查找是否已有水果，通过ID
     */
    @Select("select * from fruit where fruitID=#{fruitID}")
    public Fruit existFruitByID(Integer fruitID);


    /**
     * 删除水果
     */
    @Delete("delete from fruit where fruitID = #{fruitID}")
    public Integer delete(Integer fruitID);

    /**
     * 修改
     * @param fruit
     * @return
     */
    @Update("update fruit set name=#{name}, description=#{description}, type=#{type} where fruitID=#{fruitID}")
    public Integer update(Fruit fruit);

}
