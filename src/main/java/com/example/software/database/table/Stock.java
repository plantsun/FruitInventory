package com.example.software.database.table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Stock {
    private Integer ID;//储存ID
    private Integer fruitID;//水果ID
    private Integer warehouseID;//仓库ID
    private Timestamp time;//最后一次修改时间
    private Integer number;//存储数量
    private String quality;//质量评估

    public Stock(Integer fruitID, Integer warehouseID, Timestamp time, Integer number, String quality) {
        this.fruitID = fruitID;
        this.warehouseID = warehouseID;
        this.time = time;
        this.number = number;
        this.quality = quality;
    }
}
