package com.example.software.database.table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Warehouse {
    private Integer warehouseID;//仓库ID
    private String name;//仓库名
    private String telephone;//联系电话
    private String location;//位置


    public Warehouse(String name, String telephone, String location) {
        this.name = name;
        this.telephone = telephone;
        this.location = location;
    }
}
