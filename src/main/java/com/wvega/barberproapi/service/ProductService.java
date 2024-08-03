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

import java.util.Map;

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

            if (result != null) {
                return response.successResponse(documentReference.getId());
            }

        } catch (Exception e) {
            Thread.currentThread().interrupt();
            return response.errorResponse("Error adding product: " + e.getMessage());
        }

        return response.errorResponse("Unknown error");
    }


}
