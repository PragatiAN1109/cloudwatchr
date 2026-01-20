package com.cloudwatchr.metrics.model;

import java.time.Instant;
import java.util.Objects;

/**
 * Represents a single metric event captured from a service endpoint.
 * 
 * <p>This class defines the core schema for incoming metric events in CloudWatchr.
 * Each metric represents a single API request/response cycle with performance
 * and operational data.</p>
 * 
 * <h2>Field Specifications:</h2>
 * 
 * <h3>Required Fields:</h3>
 * <ul>
 *   <li><b>serviceName</b> (String): The name of the service generating the metric
 *       <br>Example: "user-service", "payment-api", "auth-service"</li>
 *   <li><b>endpoint</b> (String): The API endpoint path that was called
 *       <br>Example: "/api/users/{id}", "/api/payments/process"</li>
 *   <li><b>timestamp</b> (Instant): The exact time when the metric was recorded (UTC)
 *       <br>Format: ISO-8601 instant (e.g., "2024-01-20T10:30:45.123Z")</li>
 *   <li><b>latencyMs</b> (Long): The request processing time in milliseconds
 *       <br>Example: 150 (represents 150ms response time)</li>
 *   <li><b>statusCode</b> (Integer): HTTP status code of the response
 *       <br>Example: 200, 404, 500</li>
 * </ul>
 * 
 * <h3>Optional Fields:</h3>
 * <ul>
 *   <li><b>requestId</b> (String): Unique identifier for request tracing
 *       <br>Example: UUID or correlation ID for distributed tracing</li>
 *   <li><b>region</b> (String): Geographic region or data center where request was processed
 *       <br>Example: "us-east-1", "eu-west-1", "ap-southeast-2"</li>
 *   <li><b>method</b> (String): HTTP method used for the request
 *       <br>Example: "GET", "POST", "PUT", "DELETE", "PATCH"</li>
 * </ul>
 * 
 * <h2>Usage Example:</h2>
 * <pre>{@code
 * Metric metric = new Metric(
 *     "user-service",
 *     "/api/users/123",
 *     Instant.now(),
 *     150L,
 *     200,
 *     "req-uuid-1234",
 *     "us-east-1",
 *     "GET"
 * );
 * }</pre>
 * 
 * <h2>JSON Representation:</h2>
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
public class Metric {
    
    // ==================== REQUIRED FIELDS ====================
    
    /**
     * The name of the service that generated this metric.
     * <p>This identifies which microservice or application component the metric originated from.</p>
     * <p><b>Required:</b> Yes</p>
     * <p><b>Validation:</b> Cannot be null or empty</p>
     */
    private String serviceName;
    
    /**
     * The API endpoint path that was called.
     * <p>Represents the route or resource path accessed during the request.</p>
     * <p><b>Required:</b> Yes</p>
     * <p><b>Validation:</b> Cannot be null or empty</p>
     * <p><b>Example:</b> "/api/users/{id}", "/api/orders", "/health"</p>
     */
    private String endpoint;
    
    /**
     * The timestamp when this metric was recorded (UTC).
     * <p>Stored as an {@link Instant} for precise, timezone-independent time tracking.</p>
     * <p><b>Required:</b> Yes</p>
     * <p><b>Validation:</b> Cannot be null, should not be in the future</p>
     * <p><b>Format:</b> ISO-8601 instant (e.g., "2024-01-20T10:30:45.123Z")</p>
     */
    private Instant timestamp;
    
    /**
     * The request processing latency in milliseconds.
     * <p>Measures the time taken to process and respond to the request.</p>
     * <p><b>Required:</b> Yes</p>
     * <p><b>Validation:</b> Cannot be null, must be non-negative</p>
     * <p><b>Range:</b> 0 to Long.MAX_VALUE (in practice, values over 60000ms indicate timeouts)</p>
     */
    private Long latencyMs;
    
    /**
     * The HTTP status code of the response.
     * <p>Standard HTTP status codes indicating the result of the request.</p>
     * <p><b>Required:</b> Yes</p>
     * <p><b>Validation:</b> Cannot be null, must be a valid HTTP status code (100-599)</p>
     * <p><b>Common Values:</b></p>
     * <ul>
     *   <li>200: Success</li>
     *   <li>400: Bad Request</li>
     *   <li>404: Not Found</li>
     *   <li>500: Internal Server Error</li>
     * </ul>
     */
    private Integer statusCode;
    
    // ==================== OPTIONAL FIELDS ====================
    
    /**
     * Unique identifier for request tracing and correlation.
     * <p>Used for distributed tracing across multiple services.</p>
     * <p><b>Required:</b> No (Optional)</p>
     * <p><b>Format:</b> Typically UUID or correlation ID</p>
     * <p><b>Example:</b> "req-550e8400-e29b-41d4-a716-446655440000"</p>
     */
    private String requestId;
    
    /**
     * Geographic region or data center identifier.
     * <p>Indicates where the request was processed geographically.</p>
     * <p><b>Required:</b> No (Optional)</p>
     * <p><b>Examples:</b> "us-east-1", "eu-west-1", "ap-southeast-2", "on-premise-dc1"</p>
     */
    private String region;
    
    /**
     * HTTP method used for the request.
     * <p>Indicates the type of HTTP operation performed.</p>
     * <p><b>Required:</b> No (Optional)</p>
     * <p><b>Valid Values:</b> GET, POST, PUT, DELETE, PATCH, HEAD, OPTIONS</p>
     * <p><b>Default:</b> If not provided, may be inferred from context or left null</p>
     */
    private String method;
    
    // ==================== CONSTRUCTORS ====================
    
    /**
     * Default constructor for frameworks and serialization.
     */
    public Metric() {
    }
    
    /**
     * Full constructor with all fields.
     * 
     * @param serviceName The service name (required)
     * @param endpoint The API endpoint (required)
     * @param timestamp The timestamp (required)
     * @param latencyMs The latency in milliseconds (required)
     * @param statusCode The HTTP status code (required)
     * @param requestId The request ID (optional)
     * @param region The region (optional)
     * @param method The HTTP method (optional)
     */
    public Metric(String serviceName, String endpoint, Instant timestamp, 
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
     * @param latencyMs The latency in milliseconds (required)
     * @param statusCode The HTTP status code (required)
     */
    public Metric(String serviceName, String endpoint, Instant timestamp, 
                  Long latencyMs, Integer statusCode) {
        this(serviceName, endpoint, timestamp, latencyMs, statusCode, null, null, null);
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
    
    // ==================== OBJECT METHODS ====================
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Metric metric = (Metric) o;
        return Objects.equals(serviceName, metric.serviceName) &&
               Objects.equals(endpoint, metric.endpoint) &&
               Objects.equals(timestamp, metric.timestamp) &&
               Objects.equals(latencyMs, metric.latencyMs) &&
               Objects.equals(statusCode, metric.statusCode) &&
               Objects.equals(requestId, metric.requestId) &&
               Objects.equals(region, metric.region) &&
               Objects.equals(method, metric.method);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(serviceName, endpoint, timestamp, latencyMs, 
                          statusCode, requestId, region, method);
    }
    
    @Override
    public String toString() {
        return "Metric{" +
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
