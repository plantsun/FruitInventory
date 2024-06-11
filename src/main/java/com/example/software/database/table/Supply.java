package com.example.software.database.table;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Supply {
    private Integer ID;//供应ID
    private Integer fruitID;//水果ID
    private Integer supplierID;//供应商ID
    private float price;//单价
    private Integer number;//数量

    public Supply(Integer fruitID, Integer supplierID, float price, Integer number) {
        this.fruitID = fruitID;
        this.supplierID = supplierID;
        this.price = price;
        this.number = number;
    }
}
