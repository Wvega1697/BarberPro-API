package com.wvega.barberproapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

import static com.wvega.barberproapi.utils.Constants.*;

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
                objectMap.get(NAME).toString(),
                new BigDecimal(objectMap.get(PRICE).toString()),
                Integer.parseInt(objectMap.get(DURATION_MINUTES).toString()),
                objectMap.get(CATEGORY).toString(),
                objectMap.get(DESCRIPTION).toString()
        );
    }
}
