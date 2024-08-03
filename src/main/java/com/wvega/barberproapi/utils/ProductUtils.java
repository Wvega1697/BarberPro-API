package com.wvega.barberproapi.utils;

import com.google.cloud.firestore.CollectionReference;
import com.wvega.barberproapi.database.FireBaseInitializer;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

import static java.util.Arrays.stream;

@Service
public class ProductUtils {

    private final FireBaseInitializer fireBase;
    private CollectionReference products;

    public ProductUtils(FireBaseInitializer fireBase) {
        this.fireBase = fireBase;
    }

    public CollectionReference getProductsCollection() {

        if (products == null) {
            products = fireBase.getFireStore().collection("Products");
        }

        return products;
    }

    public static boolean invalidObject(Map<String, Object> objectHashMap) {

        if (objectHashMap.isEmpty()) {
            return true;
        }

        String[] requiredFields = {"name", "price", "quantity", "category", "description"};

        boolean hasEmptyFields = stream(requiredFields).anyMatch(field -> !objectHashMap.containsKey(field) || objectHashMap.get(field).toString().isEmpty());

        if (hasEmptyFields) {
            return true;
        }

        try {
            new BigDecimal(objectHashMap.get("price").toString());
            Integer.parseInt(objectHashMap.get("quantity").toString());

        } catch (NumberFormatException e) {
            return true;

        }

        return false;
    }
}
