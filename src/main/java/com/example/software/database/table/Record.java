package com.example.software.database.table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Record {
    private Integer recordID;//记录ID
    private Integer fruitID;//水果ID
    private Integer warehouseID;//仓库ID
    private Integer userID;//用户ID
    private Timestamp time;//记录时间
    private Integer number;//变更数量
    private Short isIn;//是否为入库
    private String quality;

    public Record(Integer fruitID, Integer warehouseID, Integer userID,
                  Timestamp time, Integer number, short isIn, String quality) {
        this.fruitID = fruitID;
        this.warehouseID = warehouseID;
        this.userID = userID;
        this.time = time;
        this.number = number;
        this.isIn = isIn;
        this.quality = quality;
    }
}
