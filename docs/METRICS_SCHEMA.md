# Metrics Schema Documentation

## Overview

This document defines the **formal schema** for metric events in CloudWatchr. All incoming metrics must conform to this schema for successful ingestion and processing.

## Metric Event Schema

### Schema Version: 1.0

Each metric event represents a single API request/response cycle with performance and operational data.

---

## Field Specifications

### Required Fields

These fields **must** be present in every metric submission. Requests missing any required field will be rejected with a `400 Bad Request` error.

| Field Name | Type | Description | Validation | Example |
|------------|------|-------------|------------|---------|
| **serviceName** | String | Name of the service generating the metric | Not null, not empty | `"user-service"`, `"payment-api"` |
| **endpoint** | String | API endpoint path that was called | Not null, not empty | `"/api/users/123"`, `"/api/orders"` |
| **timestamp** | Instant (ISO-8601) | Exact time when metric was recorded (UTC) | Not null, valid ISO-8601 format | `"2024-01-20T10:30:45.123Z"` |
| **latencyMs** | Long | Request processing time in milliseconds | Not null, >= 0 | `150`, `2500` |
| **statusCode** | Integer | HTTP status code of the response | Not null, 100-599 | `200`, `404`, `500` |

### Optional Fields

These fields enhance metric context but are not required for ingestion.

| Field Name | Type | Description | Example |
|------------|------|-------------|---------|
| **requestId** | String | Unique identifier for request tracing | `"req-550e8400-e29b-41d4-a716-446655440000"` |
| **region** | String | Geographic region or data center | `"us-east-1"`, `"eu-west-1"`, `"on-premise-dc1"` |
| **method** | String | HTTP method used for the request | `"GET"`, `"POST"`, `"PUT"`, `"DELETE"` |

---

## Data Types

### Instant (Timestamp)

**Format:** ISO-8601 instant in UTC timezone

**Pattern:** `yyyy-MM-dd'T'HH:mm:ss.SSS'Z'`

**Examples:**
- `"2024-01-20T10:30:45.123Z"` ✓ Valid
- `"2024-01-20T10:30:45Z"` ✓ Valid (milliseconds optional)
- `"2024-01-20 10:30:45"` ✗ Invalid (not ISO-8601)
- `"2024-01-20T10:30:45+05:00"` ✗ Invalid (must be UTC with Z)

### Long (Latency)

**Range:** 0 to 9,223,372,036,854,775,807

**Validation:** Must be non-negative

**Examples:**
- `0` ✓ Valid (instant response)
- `150` ✓ Valid (150ms)
- `60000` ✓ Valid (60 seconds)
- `-50` ✗ Invalid (negative not allowed)

### Integer (Status Code)

**Range:** 100 to 599

**Validation:** Must be a valid HTTP status code

**Common Values:**
- `200` - OK
- `201` - Created
- `400` - Bad Request
- `401` - Unauthorized
- `404` - Not Found
- `500` - Internal Server Error
- `503` - Service Unavailable

**Examples:**
- `200` ✓ Valid
- `404` ✓ Valid
- `99` ✗ Invalid (below 100)
- `600` ✗ Invalid (above 599)

---

## JSON Examples

### Minimal Valid Metric (Required Fields Only)

```json
{
  "serviceName": "user-service",
  "endpoint": "/api/users/123",
  "timestamp": "2024-01-20T10:30:45.123Z",
  "latencyMs": 150,
  "statusCode": 200
}
```

### Complete Metric (All Fields)

```json
{
  "serviceName": "user-service",
  "endpoint": "/api/users/123",
  "timestamp": "2024-01-20T10:30:45.123Z",
  "latencyMs": 150,
  "statusCode": 200,
  "requestId": "req-550e8400-e29b-41d4-a716-446655440000",
  "region": "us-east-1",
  "method": "GET"
}
```

### Batch Submission Example

```json
[
  {
    "serviceName": "user-service",
    "endpoint": "/api/users/123",
    "timestamp": "2024-01-20T10:30:45.123Z",
    "latencyMs": 150,
    "statusCode": 200,
    "method": "GET"
  },
  {
    "serviceName": "order-service",
    "endpoint": "/api/orders",
    "timestamp": "2024-01-20T10:31:00.456Z",
    "latencyMs": 200,
    "statusCode": 201,
    "method": "POST",
    "region": "eu-west-1"
  },
  {
    "serviceName": "payment-api",
    "endpoint": "/api/payments/process",
    "timestamp": "2024-01-20T10:31:15.789Z",
    "latencyMs": 3500,
    "statusCode": 200,
    "method": "POST",
    "requestId": "payment-xyz-789"
  }
]
```

---

## API Endpoints

### Submit Single Metric

**Endpoint:** `POST /api/metrics`

**Request Body:** Single metric object (JSON)

**Response:** 
- `201 Created` - Metric ingested successfully
- `400 Bad Request` - Invalid metric data

**Example Request:**
```bash
curl -X POST http://localhost:8081/api/metrics \
  -H "Content-Type: application/json" \
  -d '{
    "serviceName": "user-service",
    "endpoint": "/api/users/123",
    "timestamp": "2024-01-20T10:30:45.123Z",
    "latencyMs": 150,
    "statusCode": 200
  }'
```

**Success Response:**
```json
{
  "message": "Metric ingested successfully",
  "metric": {
    "serviceName": "user-service",
    "endpoint": "/api/users/123",
    "timestamp": "2024-01-20T10:30:45.123Z",
    "latencyMs": 150,
    "statusCode": 200,
    "requestId": null,
    "region": null,
    "method": null
  }
}
```

### Submit Batch Metrics

**Endpoint:** `POST /api/metrics/batch`

**Request Body:** Array of metric objects (JSON)

**Response:**
- `201 Created` - Batch ingested successfully
- `400 Bad Request` - Invalid metric data in batch

**Example Request:**
```bash
curl -X POST http://localhost:8081/api/metrics/batch \
  -H "Content-Type: application/json" \
  -d '[
    {
      "serviceName": "user-service",
      "endpoint": "/api/users/123",
      "timestamp": "2024-01-20T10:30:45.123Z",
      "latencyMs": 150,
      "statusCode": 200
    },
    {
      "serviceName": "order-service",
      "endpoint": "/api/orders",
      "timestamp": "2024-01-20T10:31:00.456Z",
      "latencyMs": 200,
      "statusCode": 201
    }
  ]'
```

---

## Validation Rules

### Field-Level Validation

1. **serviceName**
   - Must not be null
   - Must not be empty or whitespace-only
   - Recommended format: lowercase with hyphens (e.g., `user-service`)

2. **endpoint**
   - Must not be null
   - Must not be empty or whitespace-only
   - Should start with `/`
   - Example: `/api/users/{id}`

3. **timestamp**
   - Must not be null
   - Must be valid ISO-8601 instant
   - Should not be in the future (though not strictly enforced)
   - Must include timezone indicator (Z for UTC)

4. **latencyMs**
   - Must not be null
   - Must be >= 0
   - Values over 60000 (60 seconds) indicate potential timeouts

5. **statusCode**
   - Must not be null
   - Must be between 100 and 599 (inclusive)
   - Should be a valid HTTP status code

### Common Validation Errors

| Error Message | Cause | Solution |
|--------------|-------|----------|
| `serviceName is required and cannot be empty` | Missing or empty serviceName | Provide a valid service name |
| `endpoint is required and cannot be empty` | Missing or empty endpoint | Provide a valid endpoint path |
| `timestamp is required` | Missing timestamp field | Include a valid ISO-8601 timestamp |
| `latencyMs must be non-negative` | Negative latency value | Use a value >= 0 |
| `statusCode must be between 100 and 599` | Invalid HTTP status code | Use a valid HTTP status code |

---

## Best Practices

### Service Naming
- Use lowercase with hyphens: `user-service`, `payment-api`
- Be consistent across your infrastructure
- Keep names short but descriptive

### Endpoint Paths
- Include path parameters in template form: `/api/users/{id}` instead of `/api/users/123`
- This allows better aggregation and analysis
- For actual request tracking, use `requestId`

### Timestamps
- Always use UTC timezone
- Include millisecond precision when available
- Generate timestamp as close to the actual event as possible

### Request IDs
- Use UUID format for global uniqueness
- Include for distributed tracing scenarios
- Helps correlate metrics across services

### Regions
- Use consistent region identifiers (e.g., AWS region names)
- Helps with geographic performance analysis
- Useful for multi-region deployments

### HTTP Methods
- Use standard HTTP method names: GET, POST, PUT, DELETE, PATCH
- Use uppercase convention
- Helps with method-specific performance analysis

---

## Implementation Notes

### Current Storage
- In-memory storage (ConcurrentHashMap)
- Suitable for development and testing
- **Production**: Will be migrated to TimescaleDB or InfluxDB

### Future Enhancements
- Additional optional fields (user agent, IP address, etc.)
- Nested metric metadata
- Custom tags/labels support
- Retention policies
- Automatic metric aggregation

---

## Related Documentation

- [API Documentation](API.md)
- [Architecture Overview](ARCHITECTURE.md)
- [Main README](../README.md)

---

**Version:** 1.0  
**Last Updated:** January 20, 2024  
**Maintained By:** CloudWatchr Team
