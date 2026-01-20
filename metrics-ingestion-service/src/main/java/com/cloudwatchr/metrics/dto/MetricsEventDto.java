package com.cloudwatchr.metrics.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;

import java.time.Instant;
import java.util.Objects;

/**
 * REST-facing Data Transfer Object for metric event submission.
 * 
 * <p>This DTO represents the exact structure that the REST API accepts for
 * metric submissions. It includes comprehensive validation annotations to
 * ensure data integrity at the API boundary.</p>
 * 
 * <h2>Validation Rules:</h2>
 * <ul>
 *   <li><b>serviceName</b>: Required, not blank (@NotBlank)</li>
 *   <li><b>endpoint</b>: Required, not blank (@NotBlank)</li>
 *   <li><b>timestamp</b>: Required, not null (@NotNull)</li>
 *   <li><b>latencyMs</b>: Required, not null, must be positive (@NotNull, @Positive)</li>
 *   <li><b>statusCode</b>: Required, not null, must be 100-599 (@NotNull, @Min(100), @Max(599))</li>
 *   <li><b>requestId</b>: Optional</li>
 *   <li><b>region</b>: Optional</li>
 *   <li><b>method</b>: Optional</li>
 * </ul>
 * 
 * <h2>Usage with @Valid:</h2>
 * <pre>{@code
 * @PostMapping("/api/metrics")
 * public ResponseEntity<?> ingestMetric(@Valid @RequestBody MetricsEventDto event) {
 *     // Validation is automatically performed by Spring
 *     // If validation fails, returns 400 Bad Request with error details
 * }
 * }</pre>
 * 
 * <h2>Example JSON:</h2>
 * <pre>{@code
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
public class MetricsEventDto {
    
    /**
     * The name of the service generating the metric.
     * <p><b>Validation:</b> Must not be null or blank (whitespace-only)</p>
     * <p><b>Example:</b> "user-service", "payment-api"</p>
     */
    @NotBlank(message = "serviceName must not be blank")
    private String serviceName;
    
    /**
     * The API endpoint path that was called.
     * <p><b>Validation:</b> Must not be null or blank (whitespace-only)</p>
     * <p><b>Example:</b> "/api/users/123", "/api/orders"</p>
     */
    @NotBlank(message = "endpoint must not be blank")
    private String endpoint;
    
    /**
     * The timestamp when the metric was recorded (UTC).
     * <p><b>Validation:</b> Must not be null</p>
     * <p><b>Format:</b> ISO-8601 instant (e.g., "2024-01-20T10:30:45.123Z")</p>
     */
    @NotNull(message = "timestamp must not be null")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant timestamp;
    
    /**
     * Request processing latency in milliseconds.
     * <p><b>Validation:</b> Must not be null and must be positive (> 0)</p>
     * <p><b>Example:</b> 150, 2500</p>
     */
    @NotNull(message = "latencyMs must not be null")
    @Positive(message = "latencyMs must be positive")
    private Long latencyMs;
    
    /**
     * HTTP status code of the response.
     * <p><b>Validation:</b> Must not be null and must be between 100 and 599 (inclusive)</p>
     * <p><b>Common Values:</b> 200 (OK), 404 (Not Found), 500 (Internal Server Error)</p>
     */
    @NotNull(message = "statusCode must not be null")
    @Min(value = 100, message = "statusCode must be at least 100")
    @Max(value = 599, message = "statusCode must be at most 599")
    private Integer statusCode;
    
    /**
     * Unique request identifier for distributed tracing.
     * <p><b>Validation:</b> Optional, no validation constraints</p>
     * <p><b>Example:</b> "req-550e8400-e29b-41d4-a716-446655440000"</p>
     */
    private String requestId;
    
    /**
     * Geographic region or data center where the request was processed.
     * <p><b>Validation:</b> Optional, no validation constraints</p>
     * <p><b>Example:</b> "us-east-1", "eu-west-1"</p>
     */
    private String region;
    
    /**
     * HTTP method used for the request.
     * <p><b>Validation:</b> Optional, no validation constraints</p>
     * <p><b>Example:</b> "GET", "POST", "PUT", "DELETE"</p>
     */
    private String method;
    
    // ==================== CONSTRUCTORS ====================
    
    /**
     * Default no-args constructor for JSON deserialization.
     */
    public MetricsEventDto() {
    }
    
    /**
     * Full constructor with all fields.
     * 
     * @param serviceName The service name (required)
     * @param endpoint The API endpoint (required)
     * @param timestamp The timestamp (required)
     * @param latencyMs The latency in milliseconds (required, positive)
     * @param statusCode The HTTP status code (required, 100-599)
     * @param requestId The request ID (optional)
     * @param region The region (optional)
     * @param method The HTTP method (optional)
     */
    public MetricsEventDto(String serviceName, String endpoint, Instant timestamp,
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
    
    /**
     * Constructor with only required fields.
     * 
     * @param serviceName The service name (required)
     * @param endpoint The API endpoint (required)
     * @param timestamp The timestamp (required)
     * @param latencyMs The latency in milliseconds (required, positive)
     * @param statusCode The HTTP status code (required, 100-599)
     */
    public MetricsEventDto(String serviceName, String endpoint, Instant timestamp,
                          Long latencyMs, Integer statusCode) {
        this(serviceName, endpoint, timestamp, latencyMs, statusCode, null, null, null);
    }
    
    // ==================== GETTERS AND SETTERS ====================
    
    /**
     * Gets the service name.
     * 
     * @return The service name
     */
    public String getServiceName() {
        return serviceName;
    }
    
    /**
     * Sets the service name.
     * 
     * @param serviceName The service name (must not be blank)
     */
    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }
    
    /**
     * Gets the endpoint path.
     * 
     * @return The endpoint path
     */
    public String getEndpoint() {
        return endpoint;
    }
    
    /**
     * Sets the endpoint path.
     * 
     * @param endpoint The endpoint path (must not be blank)
     */
    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }
    
    /**
     * Gets the timestamp.
     * 
     * @return The timestamp
     */
    public Instant getTimestamp() {
        return timestamp;
    }
    
    /**
     * Sets the timestamp.
     * 
     * @param timestamp The timestamp (must not be null)
     */
    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }
    
    /**
     * Gets the latency in milliseconds.
     * 
     * @return The latency in milliseconds
     */
    public Long getLatencyMs() {
        return latencyMs;
    }
    
    /**
     * Sets the latency in milliseconds.
     * 
     * @param latencyMs The latency (must not be null and must be positive)
     */
    public void setLatencyMs(Long latencyMs) {
        this.latencyMs = latencyMs;
    }
    
    /**
     * Gets the HTTP status code.
     * 
     * @return The HTTP status code
     */
    public Integer getStatusCode() {
        return statusCode;
    }
    
    /**
     * Sets the HTTP status code.
     * 
     * @param statusCode The status code (must not be null and must be 100-599)
     */
    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }
    
    /**
     * Gets the request ID.
     * 
     * @return The request ID, or null if not provided
     */
    public String getRequestId() {
        return requestId;
    }
    
    /**
     * Sets the request ID.
     * 
     * @param requestId The request ID (optional)
     */
    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
    
    /**
     * Gets the region.
     * 
     * @return The region, or null if not provided
     */
    public String getRegion() {
        return region;
    }
    
    /**
     * Sets the region.
     * 
     * @param region The region (optional)
     */
    public void setRegion(String region) {
        this.region = region;
    }
    
    /**
     * Gets the HTTP method.
     * 
     * @return The HTTP method, or null if not provided
     */
    public String getMethod() {
        return method;
    }
    
    /**
     * Sets the HTTP method.
     * 
     * @param method The HTTP method (optional)
     */
    public void setMethod(String method) {
        this.method = method;
    }
    
    // ==================== OBJECT METHODS ====================
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MetricsEventDto that = (MetricsEventDto) o;
        return Objects.equals(serviceName, that.serviceName) &&
               Objects.equals(endpoint, that.endpoint) &&
               Objects.equals(timestamp, that.timestamp) &&
               Objects.equals(latencyMs, that.latencyMs) &&
               Objects.equals(statusCode, that.statusCode) &&
               Objects.equals(requestId, that.requestId) &&
               Objects.equals(region, that.region) &&
               Objects.equals(method, that.method);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(serviceName, endpoint, timestamp, latencyMs,
                          statusCode, requestId, region, method);
    }
    
    @Override
    public String toString() {
        return "MetricsEventDto{" +
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
