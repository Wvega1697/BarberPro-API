package com.wvega.barberproapi.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.wvega.barberproapi.model.ListData;
import com.wvega.barberproapi.model.ProductDto;
import com.wvega.barberproapi.utils.ProductUtils;
import com.wvega.barberproapi.utils.ResponseWS;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.wvega.barberproapi.utils.Constants.*;
import static com.wvega.barberproapi.utils.ProductUtils.invalidObject;
import static org.springframework.util.ObjectUtils.isEmpty;

@Slf4j
@Service
@AllArgsConstructor
public class ProductService {

    private ProductUtils productUtils;

    public ResponseWS add(Map<String, Object> product) {
        ResponseWS response = new ResponseWS();

        if (invalidObject(product)) {
            return response.failResponse("Incomplete data", product);
        }

        ProductDto productDto = ProductDto.fromMap(product);
        try {
            DocumentReference documentReference = productUtils.getProductsCollection().document();
            ApiFuture<WriteResult> writeResultApiFuture = documentReference.create(productDto);
            WriteResult result = writeResultApiFuture.get();

            return isEmpty(result)
                    ? response.failResponse("Product not created", productDto)
                    : response.successResponse("Product created successfully", documentReference.getId());

        } catch (Exception e) {
            Thread.currentThread().interrupt();
            return response.errorResponse("Error adding product: " + e.getMessage());
        }
    }

    public ResponseWS modify(String id, Map<String, Object> product) {
        ResponseWS response = new ResponseWS();

        try {
            DocumentReference documentReference = productUtils.getProductsCollection().document(id);
            Map<String, Object> updates = new HashMap<>();

            if (product.containsKey(NAME)) {
                updates.put(NAME, product.get(NAME));
            }
            if (product.containsKey(PRICE)) {
                updates.put(PRICE, new BigDecimal(product.get(PRICE).toString()));
            }
            if (product.containsKey(DURATION_MINUTES)) {
                updates.put(DURATION_MINUTES, Integer.parseInt(product.get(DURATION_MINUTES).toString()));
            }
            if (product.containsKey(CATEGORY)) {
                updates.put(CATEGORY, product.get(CATEGORY));
            }
            if (product.containsKey(DESCRIPTION)) {
                updates.put(DESCRIPTION, product.get(DESCRIPTION));
            }

            ApiFuture<WriteResult> writeResultApiFuture = documentReference.update(updates);
            WriteResult result = writeResultApiFuture.get();

            return isEmpty(result)
                    ? response.failResponse("Product not updated", id)
                    : response.successResponse("Product updated successfully", id);

        } catch (Exception e) {
            Thread.currentThread().interrupt();
            return response.errorResponse("Error updating product: " + e.getMessage());
        }
    }

    public ResponseWS delete(String id) {
        ResponseWS response = new ResponseWS();

        try {
            DocumentReference documentReference = productUtils.getProductsCollection().document(id);
            ApiFuture<WriteResult> writeResultApiFuture = documentReference.delete();
            WriteResult result = writeResultApiFuture.get();

            return isEmpty(result)
                    ? response.failResponse("Product not deleted", id)
                    : response.successResponse("Product deleted successfully", id);

        } catch (Exception e) {
            Thread.currentThread().interrupt();
            return response.errorResponse("Error deleting product: " + e.getMessage());
        }
    }

    public ResponseWS getAllProducts(Integer page, Integer size, String orderBy) {
        ResponseWS response = new ResponseWS();
        ListData listData = new ListData();

        page = isEmpty(page) ? 0 : page;
        size = isEmpty(size) ? DEFAULT_SIZE : size;
        orderBy = isEmpty(orderBy) ? NAME : orderBy;

        try {
            Firestore firestore = productUtils.getFireStore();
            ApiFuture<QuerySnapshot> queryFuture = firestore.collection(PRODUCTS_COLLECTION)
                    .orderBy(orderBy)
                    .offset((page) * size)
                    .limit(size)
                    .get();

            QuerySnapshot querySnapshot = queryFuture.get();
            List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();

            List<Object> products = new ArrayList<>();
            for (QueryDocumentSnapshot document : documents) {
                products.add(ProductDto.fromMap(document.getData()));
            }

            listData.setPage(page);
            listData.setSize(products.size());
            listData.setTotal(countDocuments(PRODUCTS_COLLECTION));
            listData.setData(products);

            return products.isEmpty()
                    ? response.failResponse("No products retrieved", listData)
                    : response.successResponse("Products retrieved successfully", listData);


        } catch (Exception e) {
            Thread.currentThread().interrupt();
            return response.errorResponse("Error retrieving products: " + e.getMessage());
        }
    }

    private long countDocuments(String collectionName) {
        try {
            AggregateQuery countQuery = productUtils.getFireStore().collection(collectionName).count();
            ApiFuture<AggregateQuerySnapshot> queryFuture = countQuery.get();
            return queryFuture.get().getCount();
        } catch (Exception e) {
            log.error(e.getMessage());
            Thread.currentThread().interrupt();
            return 0L;
        }
    }

}
