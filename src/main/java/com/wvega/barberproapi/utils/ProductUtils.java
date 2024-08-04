package com.wvega.barberproapi.utils;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.AggregateQuery;
import com.google.cloud.firestore.AggregateQuerySnapshot;
import com.google.cloud.firestore.CollectionReference;
import com.wvega.barberproapi.repository.FireBaseInitializer;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

import static com.wvega.barberproapi.utils.Constants.*;
import static java.util.Arrays.stream;

@Service
public class ProductUtils {

    private final FireBaseInitializer fireBase;
    private CollectionReference productsCollection;
    private CollectionReference statsCollection;

    public ProductUtils(FireBaseInitializer fireBase) {
        this.fireBase = fireBase;
    }

    public CollectionReference getProductsCollection() {

        if (productsCollection == null) {
            productsCollection = fireBase.getFireStore().collection(PRODUCTS_COLLECTION);
        }

        return productsCollection;
    }

    public CollectionReference getStatsCollection() {

        if (statsCollection == null) {
            statsCollection = fireBase.getFireStore().collection(STATS_COLLECTION);
        }

        return statsCollection;
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

    public long countDocuments(String collectionName) {
        try {
            AggregateQuery countQuery = fireBase.getFireStore().collection(collectionName).count();
            ApiFuture<AggregateQuerySnapshot> queryFuture = countQuery.get();
            return queryFuture.get().getCount();
        } catch (Exception e) {
            Thread.currentThread().interrupt();
            return 0L;
        }
    }

}
