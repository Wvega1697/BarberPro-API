package com.wvega.barberproapi.utils;

import java.time.ZoneOffset;

public class Constants {

    private Constants() {
    }

    //Product strings
    public static final String NAME = "name";
    public static final String PRICE = "price";
    public static final String DURATION_MINUTES = "durationMinutes";
    public static final String CATEGORY = "category";
    public static final String DESCRIPTION = "description";

    //Stat strings
    public static final String METHOD_NAME = "methodName";
    public static final String EXECUTION_TIME = "executionTime";
    public static final String PROCESSID = "processId";
    public static final String DATEMILLIS = "dateMillis";
    public static final String STATUS = "status";
    public static final String DEFAULT_BULK_ID = "SEARCH_ALL";
    public static final String DEFAULT_PROCESSID = "ERROR";

    //Firebase
    public static final String PRODUCTS_COLLECTION = "Products";
    public static final String STATS_COLLECTION = "Stats";
    public static final int DEFAULT_SIZE = 20;

    //Dates
    public static final ZoneOffset UTC_MINUS_4 = ZoneOffset.of("-04:00");

    //Log
    public static final String STATS_SUCCESS = "Stats retrieved successfully";
    public static final String STATS_FAIL = "No stats retrieved";
    public static final String STATS_ERROR = "Error retrieving stats: ";

    //Methods Name
    public static final String METHOD_ADD = "createProduct";
    public static final String METHOD_UPDATE = "updateProduct";
    public static final String METHOD_DELETE = "deleteProduct";
    public static final String METHOD_SEARCH_ALL = "getAllProducts";
    public static final String METHOD_SEARCH_ID = "getProductById";
    public static final String METHOD_SEARCH_FIELDS = "getProductsByFields";

    //Status
    public static final String STATUS_SUCCESS = "SUCCESS";
    public static final String STATUS_FAIL = "FAIL";
    public static final String STATUS_ERROR = "ERROR";

}
