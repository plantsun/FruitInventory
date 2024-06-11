package com.example.software.database.table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Fruit {
    private Integer fruitID;//水果ID
    private String name;//水果名
    private String description;//水果描述
    private String type;//水果类型

    public Fruit(String name, String description, String type) {
        this.name = name;
        this.description = description;
        this.type = type;
    }
}
