package com.cloudwatchr.metrics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MetricsIngestionServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MetricsIngestionServiceApplication.class, args);
    }
}