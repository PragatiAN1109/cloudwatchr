package com.cloudwatchr.metrics.controller;

import com.cloudwatchr.metrics.dto.MetricRequest;
import com.cloudwatchr.metrics.model.Metric;
import com.cloudwatchr.metrics.service.MetricsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REST controller for metrics ingestion endpoints.
 * 
 * <p>This controller provides HTTP endpoints for submitting and retrieving metrics.
 * All endpoints are prefixed with {@code /api/metrics}.</p>
 * 
 * <h2>Available Endpoints:</h2>
 * <ul>
 *   <li>POST /api/metrics - Submit a single metric</li>
 *   <li>POST /api/metrics/batch - Submit multiple metrics</li>
 *   <li>GET /api/metrics - Retrieve all metrics</li>
 *   <li>GET /api/metrics/service/{serviceName} - Get metrics by service</li>
 *   <li>GET /api/metrics/stats - Get ingestion statistics</li>
 * </ul>
 * 
 * @version 1.0
 * @since 1.0
 */
@RestController
@RequestMapping("/api/metrics")
public class MetricsController {
    
    private static final Logger logger = LoggerFactory.getLogger(MetricsController.class);
    
    private final MetricsService metricsService;
    
    public MetricsController(MetricsService metricsService) {
        this.metricsService = metricsService;
    }
    
    /**
     * Submit a single metric.
     * 
     * <h3>Request Example:</h3>
     * <pre>
     * POST /api/metrics
     * Content-Type: application/json
     * 
     * {
     *   "serviceName": "user-service",
     *   "endpoint": "/api/users/123",
     *   "timestamp": "2024-01-20T10:30:45.123Z",
     *   "latencyMs": 150,
     *   "statusCode": 200,
     *   "requestId": "req-uuid-1234",
     *   "region": "us-east-1",
     *   "method": "GET"
     * }
     * </pre>
     * 
     * <h3>Success Response:</h3>
     * <pre>
     * 201 Created
     * {
     *   "message": "Metric ingested successfully",
     *   "metric": { ... }
     * }
     * </pre>
     * 
     * <h3>Error Response:</h3>
     * <pre>
     * 400 Bad Request
     * {
     *   "error": "Invalid metric data",
     *   "details": "latencyMs must be non-negative"
     * }
     * </pre>
     * 
     * @param request The metric request to ingest
     * @return Response with ingested metric or error
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> ingestMetric(@RequestBody MetricRequest request) {
        logger.info("Received metric submission: service={}, endpoint={}", 
                   request.getServiceName(), request.getEndpoint());
        
        try {
            Metric metric = metricsService.ingestMetric(request);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Metric ingested successfully");
            response.put("metric", metric);
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
            
        } catch (IllegalArgumentException e) {
            logger.error("Invalid metric submission: {}", e.getMessage());
            
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Invalid metric data");
            error.put("details", e.getMessage());
            
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }
    
    /**
     * Submit multiple metrics in a batch.
     * 
     * <h3>Request Example:</h3>
     * <pre>
     * POST /api/metrics/batch
     * Content-Type: application/json
     * 
     * [
     *   {
     *     "serviceName": "user-service",
     *     "endpoint": "/api/users/123",
     *     "timestamp": "2024-01-20T10:30:45.123Z",
     *     "latencyMs": 150,
     *     "statusCode": 200
     *   },
     *   {
     *     "serviceName": "order-service",
     *     "endpoint": "/api/orders",
     *     "timestamp": "2024-01-20T10:31:00.456Z",
     *     "latencyMs": 200,
     *     "statusCode": 201
     *   }
     * ]
     * </pre>
     * 
     * @param requests List of metric requests
     * @return Response with ingested metrics or error
     */
    @PostMapping("/batch")
    public ResponseEntity<Map<String, Object>> ingestMetricsBatch(
            @RequestBody List<MetricRequest> requests) {
        
        logger.info("Received batch metric submission: {} metrics", requests.size());
        
        try {
            List<Metric> metrics = metricsService.ingestMetricsBatch(requests);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Batch ingestion successful");
            response.put("count", metrics.size());
            response.put("metrics", metrics);
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
            
        } catch (IllegalArgumentException e) {
            logger.error("Invalid batch submission: {}", e.getMessage());
            
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Invalid metric data in batch");
            error.put("details", e.getMessage());
            
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }
    
    /**
     * Retrieve all stored metrics.
     * 
     * <h3>Response Example:</h3>
     * <pre>
     * 200 OK
     * {
     *   "count": 150,
     *   "metrics": [ ... ]
     * }
     * </pre>
     * 
     * @return All stored metrics
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllMetrics() {
        logger.debug("Retrieving all metrics");
        
        List<Metric> metrics = metricsService.getAllMetrics();
        
        Map<String, Object> response = new HashMap<>();
        response.put("count", metrics.size());
        response.put("metrics", metrics);
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Retrieve metrics for a specific service.
     * 
     * <h3>Example:</h3>
     * <pre>
     * GET /api/metrics/service/user-service
     * 
     * 200 OK
     * {
     *   "serviceName": "user-service",
     *   "count": 42,
     *   "metrics": [ ... ]
     * }
     * </pre>
     * 
     * @param serviceName The service name to filter by
     * @return Metrics for the specified service
     */
    @GetMapping("/service/{serviceName}")
    public ResponseEntity<Map<String, Object>> getMetricsByService(
            @PathVariable String serviceName) {
        
        logger.debug("Retrieving metrics for service: {}", serviceName);
        
        List<Metric> metrics = metricsService.getMetricsByService(serviceName);
        
        Map<String, Object> response = new HashMap<>();
        response.put("serviceName", serviceName);
        response.put("count", metrics.size());
        response.put("metrics", metrics);
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Get ingestion statistics.
     * 
     * <h3>Response Example:</h3>
     * <pre>
     * 200 OK
     * {
     *   "totalIngested": 1547,
     *   "currentlyStored": 1547,
     *   "status": "operational"
     * }
     * </pre>
     * 
     * @return Ingestion statistics
     */
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStats() {
        logger.debug("Retrieving ingestion statistics");
        
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalIngested", metricsService.getTotalIngestedCount());
        stats.put("currentlyStored", metricsService.getCurrentStorageCount());
        stats.put("status", "operational");
        
        return ResponseEntity.ok(stats);
    }
}
