package com.wvega.barberproapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class BarberProApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(BarberProApiApplication.class, args);
    }

}
