package com.wvega.barberproapi.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.wvega.barberproapi.model.ListData;
import com.wvega.barberproapi.model.StatDto;
import com.wvega.barberproapi.utils.ProductUtils;
import com.wvega.barberproapi.utils.ResponseWS;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import static com.wvega.barberproapi.utils.Constants.*;
import static org.springframework.util.ObjectUtils.isEmpty;

@Slf4j
@Service
@AllArgsConstructor
public class StatsService {

    private final ProductUtils productUtils;

    @Async
    public void saveStats(String methodName, long executionTime, String processId, String status) {
        long dateMillis = ZonedDateTime.now(UTC_MINUS_4).toInstant().toEpochMilli();

        Map<String, Object> stats = new HashMap<>();
        stats.put(METHOD_NAME, methodName);
        stats.put(EXECUTION_TIME, executionTime);
        stats.put(PROCESSID, processId);
        stats.put(DATEMILLIS, dateMillis);
        stats.put(STATUS, status);

        try {
            productUtils.getStatsCollection().add(StatDto.fromMap(stats));
        } catch (Exception e) {
            log.error("Failed to save stats for method: {}", methodName, e);
        }
    }

    @Async
    public void saveStatsBulk(String methodName, long executionTime, List<String> processIds, String status) {
        long dateMillis = ZonedDateTime.now(UTC_MINUS_4).toInstant().toEpochMilli();

        Map<String, Object> stats = new HashMap<>();
        stats.put(METHOD_NAME, methodName);
        stats.put(EXECUTION_TIME, executionTime);
        stats.put(DATEMILLIS, dateMillis);
        stats.put(STATUS, status);

        try {
            for (String processId : processIds) {
                stats.put(PROCESSID, processId);
                productUtils.getStatsCollection().add(StatDto.fromMap(stats));
            }
        } catch (Exception e) {
            log.error("Failed to save stats for method: {}", methodName, e);
        }
    }

    public ResponseWS getStats(Integer page, Integer size, String orderBy) {
        ResponseWS response = new ResponseWS();
        ListData listData = new ListData();

        page = isEmpty(page) ? 0 : page;
        size = isEmpty(size) ? DEFAULT_SIZE : size;
        orderBy = isEmpty(orderBy) ? DATEMILLIS : orderBy;

        try {
            ApiFuture<QuerySnapshot> queryFuture = productUtils.getStatsCollection()
                    .orderBy(orderBy)
                    .offset((page) * size)
                    .limit(size)
                    .get();

            QuerySnapshot querySnapshot = queryFuture.get();
            List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();

            List<Object> statsList = new ArrayList<>();
            for (QueryDocumentSnapshot document : documents) {
                statsList.add(StatDto.fromMap(document.getData()));
            }

            listData.setPage(page);
            listData.setSize(statsList.size());
            listData.setTotal(productUtils.countDocuments(STATS_COLLECTION));
            listData.setData(statsList);

            return statsList.isEmpty()
                    ? response.failResponse(STATS_FAIL, listData)
                    : response.successResponse(STATS_SUCCESS, listData);


        } catch (Exception e) {
            Thread.currentThread().interrupt();
            return response.errorResponse(STATS_ERROR + e.getMessage());
        }
    }

    public ResponseWS getStatsByField(String field, String data) {
        ResponseWS response = new ResponseWS();
        ListData listData = new ListData();

        try {
            Query query = productUtils.getStatsCollection().whereEqualTo(field, data);
            ApiFuture<QuerySnapshot> queryFuture = query.get();
            QuerySnapshot querySnapshot = queryFuture.get();
            List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();

            List<Object> statsList = new ArrayList<>();
            for (QueryDocumentSnapshot document : documents) {
                statsList.add(StatDto.fromMap(document.getData()));
            }

            listData.setPage(0);
            listData.setSize(statsList.size());
            listData.setTotal(statsList.size());
            listData.setData(statsList);

            return statsList.isEmpty()
                    ? response.failResponse(STATS_FAIL, listData)
                    : response.successResponse(STATS_SUCCESS, listData);

        } catch (Exception e) {
            Thread.currentThread().interrupt();
            return response.errorResponse(STATS_ERROR + e.getMessage());
        }
    }


    public ResponseWS getStatsGroupedByStatus() {
        ResponseWS response = new ResponseWS();

        try {
            ApiFuture<QuerySnapshot> queryFuture = productUtils.getStatsCollection().get();
            List<QueryDocumentSnapshot> documents = queryFuture.get().getDocuments();

            Map<String, Long> statusCounts = documents.stream()
                    .collect(Collectors.groupingBy(doc -> doc.getString(STATUS), Collectors.counting()));

            return statusCounts.isEmpty()
                    ? response.failResponse(STATS_FAIL, statusCounts)
                    : response.successResponse(STATS_SUCCESS, statusCounts);

        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
            return response.errorResponse(STATS_ERROR + e.getMessage());
        }
    }

    public ResponseWS getProductCountsByCategory() {
        ResponseWS response = new ResponseWS();

        try {
            ApiFuture<QuerySnapshot> queryFuture = productUtils.getProductsCollection().get();
            List<QueryDocumentSnapshot> documents = queryFuture.get().getDocuments();

            Map<String, Long> categoryCounts = documents.stream()
                    .collect(Collectors.groupingBy(doc -> doc.getString(CATEGORY), Collectors.counting()));

            return categoryCounts.isEmpty()
                    ? response.failResponse("No products found", categoryCounts)
                    : response.successResponse("Products grouped by category", categoryCounts);

        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
            return response.errorResponse("Error retrieving product counts: " + e.getMessage());
        }
    }

    public ResponseWS getAverageExecutionTimeByMethod() {
        ResponseWS response = new ResponseWS();

        try {
            ApiFuture<QuerySnapshot> queryFuture = productUtils.getStatsCollection().get();
            List<QueryDocumentSnapshot> documents = queryFuture.get().getDocuments();

            Map<String, String> avgExecutionTime = documents.stream()
                    .collect(Collectors.groupingBy(
                            doc -> doc.getString(METHOD_NAME),
                            Collectors.collectingAndThen(
                                    Collectors.averagingDouble(doc -> ((Number) doc.get(EXECUTION_TIME)).doubleValue()),
                                    avgMillis -> String.format("%.2f Seg", avgMillis / 1000)
                            )
                    ));

            return avgExecutionTime.isEmpty()
                    ? response.failResponse("No execution time data found", avgExecutionTime)
                    : response.successResponse("Average execution time by method", avgExecutionTime);

        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
            return response.errorResponse("Error calculating average execution time: " + e.getMessage());
        }
    }

}
