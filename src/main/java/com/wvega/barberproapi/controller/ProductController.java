package com.wvega.barberproapi.controller;

import com.wvega.barberproapi.service.ProductService;
import com.wvega.barberproapi.utils.ResponseWS;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseWS createProduct(@RequestBody Map<String, Object> product) {
        log.info("createProduct");
        return productService.add(product);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateProduct(@PathVariable Long id, @RequestBody Object product) {
        log.info("updateProduct");
        return null;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        log.info("deleteProduct");
        return null;
    }

    @GetMapping
    public List<Object> getAllProducts() {
        log.info("getAllProducts");
        return Collections.emptyList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getProductById(@PathVariable Long id) {
        log.info("getProductById");
        return null;
    }

    @GetMapping("/search")
    public List<Object> getProductByName(@RequestParam String name) {
        log.info("searchProducts");
        return Collections.emptyList();
    }

}
