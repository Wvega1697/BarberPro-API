package com.wvega.barberproapi.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/products")
public class ProductController {

    @PostMapping
    public Object createProduct(@RequestBody Object product) {
        log.info("createProduct");
        return null;
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
        return null;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getProductById(@PathVariable Long id) {
        log.info("getProductById");
        return null;
    }

    @GetMapping("/search")
    public List<Object> getProductByName(@RequestParam String name) {
        log.info("searchProducts");
        return null;
    }

}
