package com.cloudwatchr.alerting.controller;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/alerts")
public class AlertController {

    @GetMapping
    public Map<String, Object> getAlerts() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Alerting service operational");
        response.put("alerts", new ArrayList<>());
        return response;
    }

    @PostMapping
    public Map<String, Object> createAlert(@RequestBody Map<String, Object> alert) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Alert created successfully");
        response.put("alert", alert);
        return response;
    }
}