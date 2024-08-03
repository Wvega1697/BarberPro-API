package com.wvega.barberproapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

@Data
@AllArgsConstructor
public class ProductDto {

    private String name;
    private BigDecimal price;
    private int durationMinutes;
    private String category;
    private String description;

    public static ProductDto fromMap(Map<String, Object> objectMap) {
        return new ProductDto(
                objectMap.get("name").toString(),
                new BigDecimal(objectMap.get("price").toString()),
                Integer.parseInt(objectMap.get("quantity").toString()),
                objectMap.get("category").toString(),
                objectMap.get("description").toString()
        );
    }
}
