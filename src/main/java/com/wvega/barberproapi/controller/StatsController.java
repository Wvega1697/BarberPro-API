package com.wvega.barberproapi.controller;

import com.wvega.barberproapi.service.StatsService;
import com.wvega.barberproapi.utils.ResponseWS;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static com.wvega.barberproapi.utils.Constants.*;

@Slf4j
@RestController
@PreAuthorize("hasRole('ADMIN')")
@AllArgsConstructor
@RequestMapping("/api/stats")
public class StatsController {

    private final StatsService statsService;

    @GetMapping
    public ResponseWS getStats(@RequestParam(required = false) Integer page,
                               @RequestParam(required = false) Integer size,
                               @RequestParam(required = false) String orderBy) {
        return statsService.getStats(page, size, orderBy);
    }

    @GetMapping("/process/{id}")
    public ResponseWS getStatsByProcessId(@PathVariable String id) {
        return statsService.getStatsByField(PROCESSID, id);
    }

    @GetMapping("/method/{name}")
    public ResponseWS getStatsByMethodName(@PathVariable String name) {
        return statsService.getStatsByField(METHOD_NAME, name);
    }

    @GetMapping("/status/{type}")
    public ResponseWS getStatsByStatus(@PathVariable String type) {
        return statsService.getStatsByField(STATUS, type);
    }

    @GetMapping("/status")
    public ResponseWS getStatsGroupedByStatus() {
        return statsService.getStatsGroupedByStatus();
    }

    @GetMapping("/categories")
    public ResponseWS getProductCountsByCategory() {
        return statsService.getProductCountsByCategory();
    }

    @GetMapping("/averages")
    public ResponseWS getAverageExecutionTimeByMethod() {
        return statsService.getAverageExecutionTimeByMethod();
    }

}
