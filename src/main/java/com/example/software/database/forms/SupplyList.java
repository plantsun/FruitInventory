package com.example.software.database.forms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SupplyList {
    private Integer ID;//供应ID
    private String fruitName;//水果名
    private String supplier名;//供应商名
    private float price;//单价
    private Integer number;//数量
}
