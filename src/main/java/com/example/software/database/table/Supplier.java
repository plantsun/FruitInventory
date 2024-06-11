package com.example.software.database.table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Supplier {

    private Integer supplierID;//供应商ID
    private String name;//供应商名
    private String telephone;//联系电话
    private String location;//位置

    public Supplier(String name, String telephone, String location) {
        this.name = name;
        this.telephone = telephone;
        this.location = location;
    }
}
