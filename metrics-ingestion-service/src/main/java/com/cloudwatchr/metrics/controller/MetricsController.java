package com.cloudwatchr.metrics.controller;

import com.cloudwatchr.metrics.dto.MetricsEventDto;
import com.cloudwatchr.metrics.model.Metric;
import com.cloudwatchr.metrics.service.MetricsService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * REST controller for metrics ingestion endpoints.
 * 
 * <p>This controller provides HTTP endpoints for submitting and retrieving metrics.
 * All endpoints are prefixed with {@code /api/metrics}.</p>
 * 
 * <h2>Validation:</h2>
 * <p>All POST endpoints use {@code @Valid} annotation to automatically validate
 * incoming {@link MetricsEventDto} objects using JSR-303 validation annotations.</p>
 * 
 * <h2>Available Endpoints:</h2>
 * <ul>
 *   <li>POST /api/metrics - Submit a single metric (with validation)</li>
 *   <li>POST /api/metrics/batch - Submit multiple metrics (with validation)</li>
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
     * Submit a single metric with automatic validation.
     * 
     * <p>The {@code @Valid} annotation triggers automatic validation of the
     * {@link MetricsEventDto} using JSR-303 annotations. If validation fails,
     * Spring automatically returns a 400 Bad Request with validation error details.</p>
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
     * <h3>Success Response (201 Created):</h3>
     * <pre>
     * {
     *   "message": "Metric ingested successfully",
     *   "metric": { ... }
     * }
     * </pre>
     * 
     * <h3>Validation Error Response (400 Bad Request):</h3>
     * <pre>
     * {
     *   "error": "Validation failed",
     *   "validationErrors": {
     *     "serviceName": "serviceName must not be blank",
     *     "latencyMs": "latencyMs must be positive"
     *   }
     * }
     * </pre>
     * 
     * @param eventDto The metric event to ingest (automatically validated)
     * @return Response with ingested metric
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> ingestMetric(@Valid @RequestBody MetricsEventDto eventDto) {
        logger.info("Received metric submission: service={}, endpoint={}", 
                   eventDto.getServiceName(), eventDto.getEndpoint());
        
        // Convert DTO to domain model and ingest
        Metric metric = metricsService.ingestMetricEvent(eventDto);
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Metric ingested successfully");
        response.put("metric", metric);
        
        logger.info("Metric ingested - Service: {}, Endpoint: {}, Status: {}, Latency: {}ms",
                   metric.getServiceName(), metric.getEndpoint(), 
                   metric.getStatusCode(), metric.getLatencyMs());
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    /**
     * Submit multiple metrics in a batch with automatic validation.
     * 
     * <p>Each metric in the batch is validated individually. If any metric fails
     * validation, the entire batch is rejected.</p>
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
     * @param eventDtos List of metric events (each validated)
     * @return Response with ingested metrics
     */
    @PostMapping("/batch")
    public ResponseEntity<Map<String, Object>> ingestMetricsBatch(
            @Valid @RequestBody List<@Valid MetricsEventDto> eventDtos) {
        
        logger.info("Received batch metric submission: {} metrics", eventDtos.size());
        
        List<Metric> metrics = metricsService.ingestMetricEventsBatch(eventDtos);
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Batch ingestion successful");
        response.put("count", metrics.size());
        response.put("metrics", metrics);
        
        logger.info("Batch ingestion complete - {} metrics processed", metrics.size());
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
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
    
    /**
     * Global exception handler for validation errors.
     * 
     * <p>This method handles {@link MethodArgumentNotValidException} thrown when
     * validation fails for {@code @Valid} annotated parameters. It extracts all
     * validation errors and returns them in a structured format.</p>
     * 
     * <h3>Error Response Format:</h3>
     * <pre>
     * 400 Bad Request
     * {
     *   "error": "Validation failed",
     *   "validationErrors": {
     *     "fieldName1": "error message 1",
     *     "fieldName2": "error message 2"
     *   }
     * }
     * </pre>
     * 
     * @param ex The validation exception
     * @return Structured error response with validation details
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        
        Map<String, String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        error -> error.getDefaultMessage() != null ? error.getDefaultMessage() : "Invalid value",
                        (existing, replacement) -> existing
                ));
        
        logger.warn("Validation failed: {}", errors);
        
        Map<String, Object> response = new HashMap<>();
        response.put("error", "Validation failed");
        response.put("validationErrors", errors);
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}
