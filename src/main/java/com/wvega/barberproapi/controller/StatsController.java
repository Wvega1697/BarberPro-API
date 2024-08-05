package com.wvega.barberproapi.controller;

import com.wvega.barberproapi.service.StatsService;
import com.wvega.barberproapi.utils.ResponseWS;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static com.wvega.barberproapi.utils.Constants.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@Slf4j
@RestController
@PreAuthorize("hasRole('ADMIN')")
@AllArgsConstructor
@RequestMapping("/api/stats")
public class StatsController {

    private final StatsService statsService;

    @Operation(summary = "Obtener estadísticas generales",
            description = "Obtiene una lista paginada de todas las estadísticas registradas en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estadísticas obtenidas exitosamente"),
            @ApiResponse(responseCode = "401", description = "Acceso denegado")
    })
    @GetMapping
    public ResponseWS getStats(@RequestParam(required = false) Integer page,
                               @RequestParam(required = false) Integer size,
                               @RequestParam(required = false) String orderBy) {
        return statsService.getStats(page, size, orderBy);
    }

    @Operation(summary = "Obtener estadísticas por ID de proceso",
            description = "Devuelve las estadísticas filtradas por el ID de proceso proporcionado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estadísticas obtenidas exitosamente"),
            @ApiResponse(responseCode = "400", description = "ID de proceso no encontrado"),
            @ApiResponse(responseCode = "401", description = "Acceso denegado")
    })
    @GetMapping("/process/{id}")
    public ResponseWS getStatsByProcessId(@PathVariable String id) {
        return statsService.getStatsByField(PROCESSID, id);
    }

    @Operation(summary = "Obtener estadísticas por nombre de método",
            description = "Devuelve las estadísticas filtradas por el nombre del método proporcionado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estadísticas obtenidas exitosamente"),
            @ApiResponse(responseCode = "400", description = "Nombre de método no encontrado"),
            @ApiResponse(responseCode = "401", description = "Acceso denegado")
    })
    @GetMapping("/method/{name}")
    public ResponseWS getStatsByMethodName(@PathVariable String name) {
        return statsService.getStatsByField(METHOD_NAME, name);
    }

    @Operation(summary = "Obtener estadísticas por tipo de estado",
            description = "Devuelve las estadísticas filtradas por el tipo de estado (success, fail, error) proporcionado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estadísticas obtenidas exitosamente"),
            @ApiResponse(responseCode = "400", description = "Tipo de estado no encontrado"),
            @ApiResponse(responseCode = "401", description = "Acceso denegado")
    })
    @GetMapping("/status/{type}")
    public ResponseWS getStatsByStatus(@PathVariable String type) {
        return statsService.getStatsByField(STATUS, type);
    }

    @Operation(summary = "Obtener estadísticas agrupadas por estado",
            description = "Devuelve un conteo de las estadísticas agrupadas por tipo de estado (success, fail, error).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Conteo de estadísticas obtenido exitosamente"),
            @ApiResponse(responseCode = "401", description = "Acceso denegado")
    })
    @GetMapping("/status")
    public ResponseWS getStatsGroupedByStatus() {
        return statsService.getStatsGroupedByStatus();
    }

    @Operation(summary = "Obtener conteo de productos por categoría",
            description = "Devuelve un conteo de productos agrupados por categoría.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Conteo de productos obtenido exitosamente"),
            @ApiResponse(responseCode = "401", description = "Acceso denegado")
    })
    @GetMapping("/categories")
    public ResponseWS getProductCountsByCategory() {
        return statsService.getProductCountsByCategory();
    }

    @Operation(summary = "Obtener tiempo de ejecución promedio por método",
            description = "Devuelve el tiempo de ejecución promedio de cada método registrado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Promedio de tiempos de ejecución obtenido exitosamente"),
            @ApiResponse(responseCode = "401", description = "Acceso denegado")
    })
    @GetMapping("/averages")
    public ResponseWS getAverageExecutionTimeByMethod() {
        return statsService.getAverageExecutionTimeByMethod();
    }

}
