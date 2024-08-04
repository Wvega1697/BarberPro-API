package com.wvega.barberproapi.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.wvega.barberproapi.model.ListData;
import com.wvega.barberproapi.model.ProductDto;
import com.wvega.barberproapi.utils.ProductUtils;
import com.wvega.barberproapi.utils.ResponseWS;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class ProductServiceTest {

    @Mock
    private ProductUtils productUtils;

    @Mock
    private StatsService statsService;

    @Mock
    private CollectionReference collectionReference;

    @Mock
    private DocumentReference documentReference;

    @Mock
    private ApiFuture<WriteResult> writeResultApiFuture;

    @Mock
    private ApiFuture<DocumentSnapshot> documentSnapshotApiFuture;

    @Mock
    private DocumentSnapshot documentSnapshot;

    @Mock
    private ApiFuture<QuerySnapshot> querySnapshotApiFuture;

    @Mock
    private QuerySnapshot querySnapshot;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    @Test
    void add_ValidProduct_ReturnsSuccessResponse() throws ExecutionException, InterruptedException {
        // Arrange
        Map<String, Object> product = new HashMap<>();
        product.put("name", "Test Product");
        product.put("price", "10.99");
        product.put("durationMinutes", "30");
        product.put("category", "Test Category");
        product.put("description", "Test Description");

        when(productUtils.getProductsCollection()).thenReturn(collectionReference);
        when(collectionReference.document()).thenReturn(documentReference);
        when(documentReference.create(any(ProductDto.class))).thenReturn(writeResultApiFuture);
        when(writeResultApiFuture.get()).thenReturn(Mockito.mock(WriteResult.class));
        when(documentReference.getId()).thenReturn("testId");

        // Act
        ResponseWS response = productService.add(product);

        // Assert
        assertTrue(response.isSuccess());
        assertEquals("Product created successfully", response.getMessage());
        assertEquals("testId", response.getData());
        verify(statsService).saveStats(anyString(), anyLong(), eq("testId"), eq("SUCCESS"));
    }

    @Test
    void add_InvalidProduct_ReturnsFailResponse() {
        // Arrange
        Map<String, Object> product = new HashMap<>();

        // Act
        ResponseWS response = productService.add(product);

        // Assert
        assertFalse(response.isSuccess());
        assertEquals("Incomplete data", response.getMessage());
        assertEquals(product, response.getData());
    }

    @Test
    void modify_ValidUpdate_ReturnsSuccessResponse() throws ExecutionException, InterruptedException {
        // Arrange
        String id = "testId";
        Map<String, Object> product = new HashMap<>();
        product.put("name", "Updated Product");
        product.put("price", "15.99");

        when(productUtils.getProductsCollection()).thenReturn(collectionReference);
        when(collectionReference.document(id)).thenReturn(documentReference);
        when(documentReference.update(anyMap())).thenReturn(writeResultApiFuture);
        WriteResult mockWriteResult = Mockito.mock(WriteResult.class);
        when(writeResultApiFuture.get()).thenReturn(mockWriteResult);

        // Act
        ResponseWS response = productService.modify(id, product);

        // Assert
        assertTrue(response.isSuccess());
        assertEquals("Product updated successfully", response.getMessage());
        assertEquals(id, response.getData());
        verify(statsService).saveStats(anyString(), anyLong(), eq(id), eq("SUCCESS"));
    }

    @Test
    void modify_EmptyUpdate_ReturnsFailResponse() {
        // Arrange
        String id = "testId";
        Map<String, Object> product = new HashMap<>();

        // Act
        ResponseWS response = productService.modify(id, product);

        // Assert
        assertFalse(response.isSuccess());
        assertEquals("No fields to update", response.getMessage());
        assertEquals(id, response.getData());
    }

    @Test
    void delete_ValidId_ReturnsSuccessResponse() throws ExecutionException, InterruptedException {
        // Arrange
        String id = "testId";

        when(productUtils.getProductsCollection()).thenReturn(collectionReference);
        when(collectionReference.document(id)).thenReturn(documentReference);
        when(documentReference.delete()).thenReturn(writeResultApiFuture);
        when(writeResultApiFuture.get()).thenReturn(Mockito.mock(WriteResult.class));

        // Act
        ResponseWS response = productService.delete(id);

        // Assert
        assertTrue(response.isSuccess());
        assertEquals("Product deleted successfully", response.getMessage());
        assertEquals(id, response.getData());
        verify(statsService).saveStats(anyString(), anyLong(), eq(id), eq("SUCCESS"));
    }

    @Test
    void searchAll_ValidParameters_ReturnsSuccessResponse() throws ExecutionException, InterruptedException {
        // Arrange
        Integer page = 0;
        Integer size = 10;
        String orderBy = "name";

        Map<String, Object> product = new HashMap<>();
        product.put("name", "Test Product");
        product.put("price", "10.99");
        product.put("durationMinutes", "30");
        product.put("category", "Test Category");
        product.put("description", "Test Description");

        QueryDocumentSnapshot doc1 = mock(QueryDocumentSnapshot.class);
        QueryDocumentSnapshot doc2 = mock(QueryDocumentSnapshot.class);

        when(productUtils.getProductsCollection()).thenReturn(collectionReference);
        when(collectionReference.orderBy(orderBy)).thenReturn(collectionReference);
        when(collectionReference.offset(anyInt())).thenReturn(collectionReference);
        when(collectionReference.limit(anyInt())).thenReturn(collectionReference);
        when(collectionReference.get()).thenReturn(querySnapshotApiFuture);
        when(querySnapshotApiFuture.get()).thenReturn(querySnapshot);
        when(querySnapshot.getDocuments()).thenReturn(Arrays.asList(doc1, doc2));
        when(doc1.getData()).thenReturn(product);
        when(doc2.getData()).thenReturn(product);
        when(productUtils.countDocuments(anyString())).thenReturn(2L);

        // Act
        ResponseWS response = productService.searchAll(page, size, orderBy);

        // Assert
        System.out.println(response.getMessage());
        assertTrue(response.isSuccess());
        assertEquals("Products retrieved successfully", response.getMessage());
        assertTrue(response.getData() instanceof ListData);
        ListData listData = (ListData) response.getData();
        assertEquals(2, listData.getSize());
        assertEquals(2L, listData.getTotal());
        verify(statsService).saveStats(anyString(), anyLong(), eq("SEARCH_ALL"), eq("SUCCESS"));
    }

    @Test
    void search_ValidId_ReturnsSuccessResponse() throws ExecutionException, InterruptedException {
        // Arrange
        String id = "testId";
        Map<String, Object> product = new HashMap<>();
        product.put("name", "Test Product");
        product.put("price", "10.99");
        product.put("durationMinutes", "30");
        product.put("category", "Test Category");
        product.put("description", "Test Description");

        when(productUtils.getProductsCollection()).thenReturn(collectionReference);
        when(collectionReference.document(id)).thenReturn(documentReference);
        when(documentReference.get()).thenReturn(documentSnapshotApiFuture);
        when(documentSnapshotApiFuture.get()).thenReturn(documentSnapshot);
        when(documentSnapshot.getData()).thenReturn(product);

        // Act
        ResponseWS response = productService.search(id);

        // Assert
        verify(statsService).saveStats(anyString(), anyLong(), eq(id), eq("SUCCESS"));
        assertTrue(response.isSuccess());
        assertEquals("Product found", response.getMessage());
        assertInstanceOf(ProductDto.class, response.getData());
    }

    @Test
    void searchByFields_ValidFields_ReturnsSuccessResponse() throws ExecutionException, InterruptedException {
        // Arrange
        Map<String, Object> fields = new HashMap<>();
        fields.put("category", "Test Category");

        Map<String, Object> product = new HashMap<>();
        product.put("name", "Test Product");
        product.put("price", "10.99");
        product.put("durationMinutes", "30");
        product.put("category", "Test Category");
        product.put("description", "Test Description");

        QueryDocumentSnapshot doc = mock(QueryDocumentSnapshot.class);

        when(productUtils.getProductsCollection()).thenReturn(collectionReference);
        when(collectionReference.whereEqualTo(anyString(), any())).thenReturn(collectionReference);
        when(collectionReference.get()).thenReturn(querySnapshotApiFuture);
        when(querySnapshotApiFuture.get()).thenReturn(querySnapshot);
        when(querySnapshot.getDocuments()).thenReturn(Arrays.asList(doc));
        when(doc.getId()).thenReturn("testId");
        when(doc.getData()).thenReturn(product);

        // Act
        ResponseWS response = productService.searchByFields(fields);

        // Assert
        assertTrue(response.isSuccess());
        assertEquals("Products retrieved successfully", response.getMessage());
        assertTrue(response.getData() instanceof ListData);
        ListData listData = (ListData) response.getData();
        assertEquals(1, listData.getSize());
        verify(statsService).saveStatsBulk(anyString(), anyLong(), anyList(), eq("SUCCESS"));
    }
}