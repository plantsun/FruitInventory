package com.example.software.database.forms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RecordList {
    private Integer recordID;//记录ID
    private String fruitName;//水果名
    private Integer warehouseID;//仓库ID
    private String userName;//用户名
    private Timestamp time;//记录时间
    private Integer number;//变更数量
    private Short isIn;//是否为入库
    private String quality;
}
