package com.wvega.barberproapi.utils;

import com.google.cloud.firestore.CollectionReference;
import com.wvega.barberproapi.database.FireBaseInitializer;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

import static com.wvega.barberproapi.utils.Constants.*;
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
            products = fireBase.getFireStore().collection(PRODUCTS_COLLECTION);
        }

        return products;
    }

    public static boolean invalidObject(Map<String, Object> objectHashMap) {

        if (objectHashMap.isEmpty()) {
            return true;
        }

        String[] requiredFields = {NAME, PRICE, DURATION_MINUTES, CATEGORY, DESCRIPTION};

        boolean hasEmptyFields = stream(requiredFields).anyMatch(field -> !objectHashMap.containsKey(field) || objectHashMap.get(field).toString().isEmpty());

        if (hasEmptyFields) {
            return true;
        }

        try {
            new BigDecimal(objectHashMap.get(PRICE).toString());
            Integer.parseInt(objectHashMap.get(DURATION_MINUTES).toString());

        } catch (NumberFormatException e) {
            return true;

        }

        return false;
    }


}
