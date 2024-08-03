package com.wvega.barberproapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
        BigDecimal price = new BigDecimal(objectMap.get(PRICE).toString())
                .setScale(2, RoundingMode.HALF_DOWN);

        return new ProductDto(
                objectMap.get(NAME).toString(),
                price,
                Integer.parseInt(objectMap.get(DURATION_MINUTES).toString()),
                objectMap.get(CATEGORY).toString(),
                objectMap.get(DESCRIPTION).toString()
        );
    }

}
