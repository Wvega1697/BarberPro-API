package com.wvega.barberproapi.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.wvega.barberproapi.model.ListData;
import com.wvega.barberproapi.model.StatDto;
import com.wvega.barberproapi.utils.ProductUtils;
import com.wvega.barberproapi.utils.ResponseWS;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.ZonedDateTime;
import java.util.*;
import java.util.concurrent.ExecutionException;

import static com.wvega.barberproapi.utils.Constants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class StatsServiceTest {

    @Mock
    private ProductUtils productUtils;

    @Mock
    private CollectionReference collectionReference;

    @Mock
    private ApiFuture<QuerySnapshot> querySnapshotApiFuture;

    @Mock
    private QuerySnapshot querySnapshot;

    @InjectMocks
    private StatsService statsService;

    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    @Test
    void saveStats_ShouldAddStatsToCollection() {
        // Arrange
        when(productUtils.getStatsCollection()).thenReturn(collectionReference);
        when(collectionReference.add(any(StatDto.class))).thenReturn(null);

        // Act
        statsService.saveStats("testMethod", 100L, "testId", "SUCCESS");

        // Assert
        verify(productUtils.getStatsCollection(), times(1)).add(any(StatDto.class));
    }

    @Test
    void saveStatsBulk_ShouldAddMultipleStatsToCollection() {
        // Arrange
        when(productUtils.getStatsCollection()).thenReturn(collectionReference);
        when(collectionReference.add(any(StatDto.class))).thenReturn(null);

        // Act
        statsService.saveStatsBulk("testMethod", 100L, Arrays.asList("id1", "id2"), "SUCCESS");

        // Assert
        verify(productUtils.getStatsCollection(), times(2)).add(any(StatDto.class));
    }

    @Test
    void getStats_ShouldReturnListOfStats() throws ExecutionException, InterruptedException {
        // Arrange
        Map<String, Object> statsMap = new HashMap<>();
        statsMap.put("methodName", "testMethod");
        statsMap.put("executionTime", 100L);
        statsMap.put("processId", "testId");
        statsMap.put("dateMillis", ZonedDateTime.now(UTC_MINUS_4).toInstant().toEpochMilli());
        statsMap.put("status", "SUCCESS");

        when(productUtils.getStatsCollection()).thenReturn(collectionReference);
        when(collectionReference.orderBy(anyString())).thenReturn(collectionReference);
        when(collectionReference.offset(anyInt())).thenReturn(collectionReference);
        when(collectionReference.limit(anyInt())).thenReturn(collectionReference);
        when(collectionReference.get()).thenReturn(querySnapshotApiFuture);
        when(querySnapshotApiFuture.get()).thenReturn(querySnapshot);

        QueryDocumentSnapshot document = mock(QueryDocumentSnapshot.class);
        when(querySnapshot.getDocuments()).thenReturn(Collections.singletonList(document));
        when(document.getData()).thenReturn(statsMap);

        when(productUtils.countDocuments(anyString())).thenReturn(1L);

        // Act
        ResponseWS response = statsService.getStats(0, 10, "dateMillis");

        // Assert
        assertTrue(response.isSuccess());
        assertTrue(response.getData() instanceof ListData);
    }

    @Test
    void getStatsByField_ShouldReturnFilteredStats() throws ExecutionException, InterruptedException {
        // Arrange
        Map<String, Object> statsMap = new HashMap<>();
        statsMap.put("methodName", "testMethod");
        statsMap.put("executionTime", 100L);
        statsMap.put("processId", "testId");
        statsMap.put("dateMillis", ZonedDateTime.now(UTC_MINUS_4).toInstant().toEpochMilli());
        statsMap.put("status", "SUCCESS");

        when(productUtils.getStatsCollection()).thenReturn(collectionReference);
        when(collectionReference.whereEqualTo(anyString(), any())).thenReturn(collectionReference);
        when(collectionReference.get()).thenReturn(querySnapshotApiFuture);
        when(querySnapshotApiFuture.get()).thenReturn(querySnapshot);

        QueryDocumentSnapshot document = mock(QueryDocumentSnapshot.class);
        when(querySnapshot.getDocuments()).thenReturn(Collections.singletonList(document));
        when(document.getData()).thenReturn(statsMap);

        // Act
        ResponseWS response = statsService.getStatsByField("methodName", "testMethod");

        // Assert
        assertTrue(response.isSuccess());
        assertTrue(response.getData() instanceof ListData);
    }

    @Test
    void getStatsGroupedByStatus_ShouldReturnGroupedStats() throws ExecutionException, InterruptedException {
        // Arrange
        when(productUtils.getStatsCollection()).thenReturn(collectionReference);
        when(collectionReference.get()).thenReturn(querySnapshotApiFuture);
        when(querySnapshotApiFuture.get()).thenReturn(querySnapshot);

        QueryDocumentSnapshot document = mock(QueryDocumentSnapshot.class);
        when(querySnapshot.getDocuments()).thenReturn(Collections.singletonList(document));
        when(document.getString(STATUS)).thenReturn("SUCCESS");

        // Act
        ResponseWS response = statsService.getStatsGroupedByStatus();

        // Assert
        assertTrue(response.isSuccess());
        assertTrue(response.getData() instanceof Map);
        Map<String, Long> result = (Map<String, Long>) response.getData();
        assertEquals(1L, result.get("SUCCESS"));
    }

    @Test
    void getProductCountsByCategory_ShouldReturnCategoryCounts() throws ExecutionException, InterruptedException {
        // Arrange
        when(productUtils.getProductsCollection()).thenReturn(collectionReference);
        when(collectionReference.get()).thenReturn(querySnapshotApiFuture);
        when(querySnapshotApiFuture.get()).thenReturn(querySnapshot);

        QueryDocumentSnapshot document = mock(QueryDocumentSnapshot.class);
        when(querySnapshot.getDocuments()).thenReturn(Collections.singletonList(document));
        when(document.getString(CATEGORY)).thenReturn("TestCategory");

        // Act
        ResponseWS response = statsService.getProductCountsByCategory();

        // Assert
        assertTrue(response.isSuccess());
        assertTrue(response.getData() instanceof Map);
        Map<String, Long> result = (Map<String, Long>) response.getData();
        assertEquals(1L, result.get("TestCategory"));
    }

    @Test
    void getAverageExecutionTimeByMethod_ShouldReturnAverageTime() throws ExecutionException, InterruptedException {
        // Arrange
        when(productUtils.getStatsCollection()).thenReturn(collectionReference);
        when(collectionReference.get()).thenReturn(querySnapshotApiFuture);
        when(querySnapshotApiFuture.get()).thenReturn(querySnapshot);

        QueryDocumentSnapshot document = mock(QueryDocumentSnapshot.class);
        when(querySnapshot.getDocuments()).thenReturn(Collections.singletonList(document));
        when(document.getString(METHOD_NAME)).thenReturn("testMethod");
        when(document.get(EXECUTION_TIME)).thenReturn(1000L);

        // Act
        ResponseWS response = statsService.getAverageExecutionTimeByMethod();

        // Assert
        assertTrue(response.isSuccess());
        assertTrue(response.getData() instanceof Map);
        Map<String, String> result = (Map<String, String>) response.getData();
        assertEquals("1.00 Seg", result.get("testMethod"));
    }
}