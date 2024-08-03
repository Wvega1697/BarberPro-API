package com.wvega.barberproapi.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.WriteResult;
import com.wvega.barberproapi.model.ProductDto;
import com.wvega.barberproapi.utils.ProductUtils;
import com.wvega.barberproapi.utils.ResponseWS;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static com.wvega.barberproapi.utils.Constants.*;
import static com.wvega.barberproapi.utils.ProductUtils.invalidObject;

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

            return ObjectUtils.isEmpty(result)
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

            return ObjectUtils.isEmpty(result)
                    ? response.failResponse("Product not updated", id)
                    : response.successResponse("Product updated successfully", id);

        } catch (Exception e) {
            Thread.currentThread().interrupt();
            return response.errorResponse("Error updating product: " + e.getMessage());
        }
    }

}
