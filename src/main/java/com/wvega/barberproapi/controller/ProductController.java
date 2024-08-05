package com.wvega.barberproapi.controller;

import com.wvega.barberproapi.service.ProductService;
import com.wvega.barberproapi.utils.ResponseWS;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    @Operation(summary = "Crear un nuevo producto",
            description = "Crea un nuevo producto en la barbería con los detalles proporcionados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos del producto inválidos"),
            @ApiResponse(responseCode = "500", description = "Error al agregar el producto")
    })
    @PostMapping
    public ResponseWS createProduct(@RequestBody Map<String, Object> product) {
        log.info("createProduct");
        return productService.add(product);
    }

    @Operation(summary = "Actualizar un producto",
            description = "Actualiza los detalles de un producto existente por ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto actualizado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Producto no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error al modificar el producto")
    })
    @PutMapping("/{id}")
    public ResponseWS updateProduct(@PathVariable String id, @RequestBody Map<String, Object> product) {
        log.info("updateProduct");
        return productService.modify(id, product);
    }

    @Operation(summary = "Eliminar un producto",
            description = "Elimina un producto existente por ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto eliminado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Producto no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error al eliminar un producto")
    })
    @DeleteMapping("/{id}")
    public ResponseWS deleteProduct(@PathVariable String id) {
        log.info("deleteProduct");
        return productService.delete(id);
    }

    @Operation(summary = "Obtener todos los productos",
            description = "Obtiene una lista de todos los productos, con opciones de paginación y ordenamiento.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de productos obtenida exitosamente"),
            @ApiResponse(responseCode = "400", description = "Sin Productos encontrados"),
            @ApiResponse(responseCode = "500", description = "Error al buscar los productos")
    })
    @GetMapping
    public ResponseWS getAllProducts(@RequestParam(required = false) Integer page,
                                     @RequestParam(required = false) Integer size,
                                     @RequestParam(required = false) String orderBy) {
        log.info("getAllProducts");
        return productService.searchAll(page, size, orderBy);
    }

    @Operation(summary = "Obtener un producto por ID",
            description = "Obtiene los detalles de un producto específico dado su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto obtenido exitosamente"),
            @ApiResponse(responseCode = "400", description = "Producto no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error al obtener el producto")
    })
    @GetMapping("/{id}")
    public ResponseWS getProductById(@PathVariable String id) {
        log.info("getProductById");
        return productService.search(id);
    }

    @Operation(summary = "Buscar productos por campos",
            description = "Busca productos basados en un conjunto de criterios especificados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto obtenido exitosamente"),
            @ApiResponse(responseCode = "400", description = "Criterios de búsqueda inválidos"),
            @ApiResponse(responseCode = "500", description = "Error al obtener el producto")
    })
    @PostMapping("/search")
    public ResponseWS getProductsByFields(@RequestBody Map<String, Object> fields) {
        log.info("searchProducts");
        return productService.searchByFields(fields);
    }

}
