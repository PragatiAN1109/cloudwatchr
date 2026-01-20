package com.cloudwatchr.metrics.service;

import com.cloudwatchr.metrics.dto.MetricRequest;
import com.cloudwatchr.metrics.model.Metric;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Service for processing and managing metric data.
 * 
 * <p>This service handles the ingestion, validation, and storage of metrics.
 * In this initial implementation, metrics are stored in-memory for demonstration
 * purposes. Future versions will integrate with a persistent data store.</p>
 * 
 * <h2>Responsibilities:</h2>
 * <ul>
 *   <li>Validate incoming metric requests</li>
 *   <li>Convert DTOs to domain models</li>
 *   <li>Store metrics (in-memory for now)</li>
 *   <li>Provide metric retrieval operations</li>
 *   <li>Track ingestion statistics</li>
 * </ul>
 * 
 * @version 1.0
 * @since 1.0
 */
@Service
public class MetricsService {
    
    private static final Logger logger = LoggerFactory.getLogger(MetricsService.class);
    
    /**
     * In-memory storage for metrics.
     * Key: Metric ID (auto-generated), Value: Metric object
     * 
     * <p>Note: This is a temporary solution for demonstration.
     * Production implementation should use a database (e.g., TimescaleDB, InfluxDB).</p>
     */
    private final ConcurrentHashMap<Long, Metric> metricsStore = new ConcurrentHashMap<>();
    
    /**
     * Counter for generating unique metric IDs.
     */
    private final AtomicLong idCounter = new AtomicLong(0);
    
    /**
     * Counter for tracking total metrics ingested.
     */
    private final AtomicLong totalIngested = new AtomicLong(0);
    
    /**
     * Ingests a single metric from an API request.
     * 
     * <p>Validates the request, converts it to a domain model, and stores it.</p>
     * 
     * @param request The metric request to ingest
     * @return The stored metric with generated ID
     * @throws IllegalArgumentException if the request is invalid
     */
    public Metric ingestMetric(MetricRequest request) {
        logger.debug("Ingesting metric: {}", request);
        
        // Validate the request
        if (!request.isValid()) {
            String error = request.getValidationError();
            logger.error("Invalid metric request: {}", error);
            throw new IllegalArgumentException(error);
        }
        
        // Convert DTO to domain model
        Metric metric = convertToMetric(request);
        
        // Store the metric
        long id = idCounter.incrementAndGet();
        metricsStore.put(id, metric);
        totalIngested.incrementAndGet();
        
        logger.info("Metric ingested successfully - Service: {}, Endpoint: {}, Status: {}, Latency: {}ms",
                   metric.getServiceName(), metric.getEndpoint(), 
                   metric.getStatusCode(), metric.getLatencyMs());
        
        return metric;
    }
    
    /**
     * Ingests multiple metrics in a batch operation.
     * 
     * @param requests List of metric requests to ingest
     * @return List of ingested metrics
     * @throws IllegalArgumentException if any request is invalid
     */
    public List<Metric> ingestMetricsBatch(List<MetricRequest> requests) {
        logger.info("Ingesting batch of {} metrics", requests.size());
        
        List<Metric> ingestedMetrics = new ArrayList<>();
        
        for (MetricRequest request : requests) {
            Metric metric = ingestMetric(request);
            ingestedMetrics.add(metric);
        }
        
        logger.info("Batch ingestion complete - {} metrics processed", ingestedMetrics.size());
        return ingestedMetrics;
    }
    
    /**
     * Retrieves all stored metrics.
     * 
     * @return List of all metrics
     */
    public List<Metric> getAllMetrics() {
        return new ArrayList<>(metricsStore.values());
    }
    
    /**
     * Retrieves metrics for a specific service.
     * 
     * @param serviceName The service name to filter by
     * @return List of metrics for the specified service
     */
    public List<Metric> getMetricsByService(String serviceName) {
        return metricsStore.values().stream()
                .filter(m -> m.getServiceName().equals(serviceName))
                .toList();
    }
    
    /**
     * Gets the total count of ingested metrics.
     * 
     * @return Total number of metrics ingested since service start
     */
    public long getTotalIngestedCount() {
        return totalIngested.get();
    }
    
    /**
     * Gets the current count of stored metrics.
     * 
     * @return Current number of metrics in storage
     */
    public long getCurrentStorageCount() {
        return metricsStore.size();
    }
    
    /**
     * Clears all stored metrics.
     * <p>Useful for testing and resetting the service.</p>
     */
    public void clearAllMetrics() {
        logger.warn("Clearing all metrics from storage");
        metricsStore.clear();
    }
    
    /**
     * Converts a MetricRequest DTO to a Metric domain model.
     * 
     * @param request The request to convert
     * @return The domain model
     */
    private Metric convertToMetric(MetricRequest request) {
        return new Metric(
            request.getServiceName(),
            request.getEndpoint(),
            request.getTimestamp(),
            request.getLatencyMs(),
            request.getStatusCode(),
            request.getRequestId(),
            request.getRegion(),
            request.getMethod()
        );
    }
}
