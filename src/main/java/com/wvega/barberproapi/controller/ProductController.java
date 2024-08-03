package com.wvega.barberproapi.controller;

import com.wvega.barberproapi.service.ProductService;
import com.wvega.barberproapi.utils.ResponseWS;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
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
    public ResponseWS updateProduct(@PathVariable String id, @RequestBody Map<String, Object> product) {
        log.info("updateProduct");
        return productService.modify(id, product);
    }

    @DeleteMapping("/{id}")
    public ResponseWS deleteProduct(@PathVariable String id) {
        log.info("deleteProduct");
        return productService.delete(id);
    }

    @GetMapping
    public ResponseWS getAllProducts(@RequestParam(required = false) Integer page,
                                     @RequestParam(required = false) Integer size,
                                     @RequestParam(required = false) String orderBy) {
        log.info("getAllProducts");
        return productService.searchAll(page, size, orderBy);
    }

    @GetMapping("/{id}")
    public ResponseWS getProductById(@PathVariable String id) {
        log.info("getProductById");
        return productService.search(id);
    }

    @GetMapping("/search")
    public List<Object> getProductByName(@RequestParam String name) {
        log.info("searchProducts");
        return Collections.emptyList();
    }

}
