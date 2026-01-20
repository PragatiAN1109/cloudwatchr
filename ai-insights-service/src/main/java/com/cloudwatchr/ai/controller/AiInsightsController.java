package com.cloudwatchr.ai.controller;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ai")
public class AiInsightsController {

    @GetMapping("/insights")
    public Map<String, Object> getInsights() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "AI Insights service operational");
        
        List<Map<String, String>> insights = new ArrayList<>();
        Map<String, String> insight1 = new HashMap<>();
        insight1.put("type", "anomaly");
        insight1.put("description", "Sample insight - system performance normal");
        insights.add(insight1);
        
        response.put("insights", insights);
        return response;
    }

    @PostMapping("/analyze")
    public Map<String, Object> analyzeMetrics(@RequestBody Map<String, Object> metrics) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Analysis complete");
        response.put("recommendation", "All systems operating normally");
        return response;
    }
}