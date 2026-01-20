package com.cloudwatchr.metrics.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.Instant;

/**
 * Data Transfer Object for receiving metric data via API requests.
 * 
 * <p>This DTO handles incoming metric submissions and validates the data
 * before processing. It mirrors the {@link com.cloudwatchr.metrics.model.Metric}
 * structure but is specifically designed for API layer validation and
 * serialization.</p>
 * 
 * <h2>Validation Rules:</h2>
 * <ul>
 *   <li><b>serviceName</b>: Required, not null, not empty</li>
 *   <li><b>endpoint</b>: Required, not null, not empty</li>
 *   <li><b>timestamp</b>: Required, not null, must be valid ISO-8601 instant</li>
 *   <li><b>latencyMs</b>: Required, not null, must be >= 0</li>
 *   <li><b>statusCode</b>: Required, not null, must be 100-599</li>
 *   <li><b>requestId</b>: Optional</li>
 *   <li><b>region</b>: Optional</li>
 *   <li><b>method</b>: Optional, if provided should be valid HTTP method</li>
 * </ul>
 * 
 * <h2>Example JSON Request:</h2>
 * <pre>{@code
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
 * }</pre>
 * 
 * @version 1.0
 * @since 1.0
 */
public class MetricRequest {
    
    /**
     * The name of the service generating the metric.
     * <p><b>Required:</b> Yes</p>
     * <p><b>Validation:</b> Must not be null or empty</p>
     */
    private String serviceName;
    
    /**
     * The API endpoint path.
     * <p><b>Required:</b> Yes</p>
     * <p><b>Validation:</b> Must not be null or empty</p>
     */
    private String endpoint;
    
    /**
     * The timestamp when the metric was recorded.
     * <p><b>Required:</b> Yes</p>
     * <p><b>Format:</b> ISO-8601 instant (e.g., "2024-01-20T10:30:45.123Z")</p>
     * <p><b>Validation:</b> Must not be null, must be valid ISO-8601</p>
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant timestamp;
    
    /**
     * Request processing latency in milliseconds.
     * <p><b>Required:</b> Yes</p>
     * <p><b>Validation:</b> Must not be null, must be >= 0</p>
     */
    private Long latencyMs;
    
    /**
     * HTTP status code.
     * <p><b>Required:</b> Yes</p>
     * <p><b>Validation:</b> Must not be null, must be 100-599</p>
     */
    private Integer statusCode;
    
    /**
     * Unique request identifier for tracing.
     * <p><b>Required:</b> No (Optional)</p>
     */
    private String requestId;
    
    /**
     * Geographic region or data center.
     * <p><b>Required:</b> No (Optional)</p>
     */
    private String region;
    
    /**
     * HTTP method (GET, POST, PUT, DELETE, etc.).
     * <p><b>Required:</b> No (Optional)</p>
     */
    private String method;
    
    // ==================== CONSTRUCTORS ====================
    
    /**
     * Default constructor for JSON deserialization.
     */
    public MetricRequest() {
    }
    
    /**
     * Full constructor for testing and manual creation.
     */
    public MetricRequest(String serviceName, String endpoint, Instant timestamp,
                        Long latencyMs, Integer statusCode, String requestId,
                        String region, String method) {
        this.serviceName = serviceName;
        this.endpoint = endpoint;
        this.timestamp = timestamp;
        this.latencyMs = latencyMs;
        this.statusCode = statusCode;
        this.requestId = requestId;
        this.region = region;
        this.method = method;
    }
    
    // ==================== VALIDATION ====================
    
    /**
     * Validates the required fields.
     * 
     * @return true if all required fields are present and valid
     */
    public boolean isValid() {
        return serviceName != null && !serviceName.trim().isEmpty() &&
               endpoint != null && !endpoint.trim().isEmpty() &&
               timestamp != null &&
               latencyMs != null && latencyMs >= 0 &&
               statusCode != null && statusCode >= 100 && statusCode <= 599;
    }
    
    /**
     * Gets validation error message if data is invalid.
     * 
     * @return Error message, or null if valid
     */
    public String getValidationError() {
        if (serviceName == null || serviceName.trim().isEmpty()) {
            return "serviceName is required and cannot be empty";
        }
        if (endpoint == null || endpoint.trim().isEmpty()) {
            return "endpoint is required and cannot be empty";
        }
        if (timestamp == null) {
            return "timestamp is required";
        }
        if (latencyMs == null) {
            return "latencyMs is required";
        }
        if (latencyMs < 0) {
            return "latencyMs must be non-negative";
        }
        if (statusCode == null) {
            return "statusCode is required";
        }
        if (statusCode < 100 || statusCode > 599) {
            return "statusCode must be between 100 and 599";
        }
        return null;
    }
    
    // ==================== GETTERS AND SETTERS ====================
    
    public String getServiceName() {
        return serviceName;
    }
    
    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }
    
    public String getEndpoint() {
        return endpoint;
    }
    
    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }
    
    public Instant getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }
    
    public Long getLatencyMs() {
        return latencyMs;
    }
    
    public void setLatencyMs(Long latencyMs) {
        this.latencyMs = latencyMs;
    }
    
    public Integer getStatusCode() {
        return statusCode;
    }
    
    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }
    
    public String getRequestId() {
        return requestId;
    }
    
    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
    
    public String getRegion() {
        return region;
    }
    
    public void setRegion(String region) {
        this.region = region;
    }
    
    public String getMethod() {
        return method;
    }
    
    public void setMethod(String method) {
        this.method = method;
    }
    
    @Override
    public String toString() {
        return "MetricRequest{" +
               "serviceName='" + serviceName + '\'' +
               ", endpoint='" + endpoint + '\'' +
               ", timestamp=" + timestamp +
               ", latencyMs=" + latencyMs +
               ", statusCode=" + statusCode +
               ", requestId='" + requestId + '\'' +
               ", region='" + region + '\'' +
               ", method='" + method + '\'' +
               '}';
    }
}
