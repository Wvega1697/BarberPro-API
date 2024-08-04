package com.wvega.barberproapi;

import com.wvega.barberproapi.controller.ProductController;
import com.wvega.barberproapi.controller.StatsController;
import com.wvega.barberproapi.service.ProductService;
import com.wvega.barberproapi.service.StatsService;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@AllArgsConstructor
@SpringBootTest
class BarberProApiApplicationTests {

    private ApplicationContext applicationContext;

    @Test
    void contextLoads() {
        assertNotNull(applicationContext, "Application context should not be null");
    }

    @Test
    void mainComponentsArePresent() {
        assertNotNull(applicationContext.getBean(ProductService.class), "ProductService should be present");
        assertNotNull(applicationContext.getBean(ProductController.class), "ProductController should be present");
        assertNotNull(applicationContext.getBean(StatsService.class), "StatsService should be present");
        assertNotNull(applicationContext.getBean(StatsController.class), "StatsController should be present");
    }

}
