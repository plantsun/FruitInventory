package com.example.software.database.forms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StockList {
    private Integer ID;//储存ID
    private String fruitName;//水果名
    private String warehouseName;//仓库Name
    private Timestamp time;//最后一次修改时间
    private Integer number;//存储数量
    private String quality;//质量评估
}