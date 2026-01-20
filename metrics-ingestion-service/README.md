# Metrics Ingestion Service

## Overview

The Metrics Ingestion Service is responsible for collecting and processing metric events from various services in the CloudWatchr platform. It validates incoming metrics, stores them, and provides retrieval capabilities.

## Metrics Schema

### Required Fields

Every metric submission **must** include these fields:

| Field | Type | Description | Example |
|-------|------|-------------|---------|
| `serviceName` | String | Name of the service | `"user-service"` |
| `endpoint` | String | API endpoint path | `"/api/users/123"` |
| `timestamp` | Instant | UTC timestamp (ISO-8601) | `"2024-01-20T10:30:45.123Z"` |
| `latencyMs` | Long | Response time in milliseconds | `150` |
| `statusCode` | Integer | HTTP status code (100-599) | `200` |

### Optional Fields

| Field | Type | Description | Example |
|-------|------|-------------|---------|
| `requestId` | String | Unique request identifier | `"req-uuid-1234"` |
| `region` | String | Geographic region | `"us-east-1"` |
| `method` | String | HTTP method | `"GET"` |

**📖 Full Schema Documentation:** See [docs/METRICS_SCHEMA.md](../docs/METRICS_SCHEMA.md)

## Quick Start

### 1. Start the Service

```bash
cd metrics-ingestion-service
mvn spring-boot:run
```

The service will start on **port 8081**.

### 2. Submit a Metric

```bash
curl -X POST http://localhost:8081/api/metrics \
  -H "Content-Type: application/json" \
  -d '{
    "serviceName": "user-service",
    "endpoint": "/api/users/123",
    "timestamp": "2024-01-20T10:30:45.123Z",
    "latencyMs": 150,
    "statusCode": 200,
    "method": "GET"
  }'
```

### 3. Retrieve Metrics

```bash
# Get all metrics
curl http://localhost:8081/api/metrics

# Get metrics for a specific service
curl http://localhost:8081/api/metrics/service/user-service

# Get ingestion statistics
curl http://localhost:8081/api/metrics/stats
```

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/metrics` | Submit a single metric |
| `POST` | `/api/metrics/batch` | Submit multiple metrics |
| `GET` | `/api/metrics` | Retrieve all metrics |
| `GET` | `/api/metrics/service/{name}` | Get metrics by service name |
| `GET` | `/api/metrics/stats` | Get ingestion statistics |
| `GET` | `/api/health` | Service health check |

## Project Structure

```
metrics-ingestion-service/
├── src/main/java/com/cloudwatchr/metrics/
│   ├── controller/
│   │   ├── HealthController.java      # Health check endpoint
│   │   └── MetricsController.java     # Metrics API endpoints
│   ├── dto/
│   │   └── MetricRequest.java         # API request DTO
│   ├── model/
│   │   └── Metric.java                # Core metric domain model
│   ├── service/
│   │   └── MetricsService.java        # Business logic
│   └── MetricsIngestionServiceApplication.java
└── pom.xml
```

## Key Classes

### Metric.java
- **Location:** `model/Metric.java`
- **Purpose:** Core domain model representing a metric event
- **Documentation:** Comprehensive JavaDoc with field specifications

### MetricRequest.java
- **Location:** `dto/MetricRequest.java`
- **Purpose:** DTO for API requests with validation
- **Validation:** Built-in validation methods

### MetricsService.java
- **Location:** `service/MetricsService.java`
- **Purpose:** Business logic for metric processing
- **Features:** Single/batch ingestion, retrieval, statistics

### MetricsController.java
- **Location:** `controller/MetricsController.java`
- **Purpose:** REST API endpoints
- **Endpoints:** Complete CRUD operations for metrics

## Examples

### Minimal Metric (Required Fields Only)

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

### Batch Submission

```bash
curl -X POST http://localhost:8081/api/metrics/batch \
  -H "Content-Type: application/json" \
  -d '[
    {
      "serviceName": "user-service",
      "endpoint": "/api/users",
      "timestamp": "2024-01-20T10:30:45.123Z",
      "latencyMs": 120,
      "statusCode": 200,
      "method": "GET"
    },
    {
      "serviceName": "order-service",
      "endpoint": "/api/orders",
      "timestamp": "2024-01-20T10:31:00.456Z",
      "latencyMs": 250,
      "statusCode": 201,
      "method": "POST"
    }
  ]'
```

## Validation

The service validates all incoming metrics:

- **Required fields** must be present and non-null
- **serviceName** and **endpoint** cannot be empty
- **latencyMs** must be >= 0
- **statusCode** must be between 100 and 599
- **timestamp** must be valid ISO-8601 format

Invalid submissions return `400 Bad Request` with error details.

## Storage

**Current Implementation:**
- In-memory storage using `ConcurrentHashMap`
- Thread-safe operations
- Suitable for development and testing

**Future Implementation:**
- TimescaleDB for time-series data
- InfluxDB for high-throughput metrics
- Retention policies and data aggregation

## Health Check

```bash
curl http://localhost:8081/api/health
```

**Response:**
```json
{
  "service": "metrics-ingestion-service",
  "status": "UP"
}
```

## Configuration

**Port:** 8081 (configured in `application.yml`)

```yaml
server:
  port: 8081

spring:
  application:
    name: metrics-ingestion-service
```

## Dependencies

- **Spring Boot 3.2.1**
- **Spring Web** - REST API support
- **Spring Actuator** - Health checks
- **Jackson** - JSON processing
- **SLF4J** - Logging

## Logging

The service logs:
- Info: Successful metric ingestion
- Debug: Metric retrieval operations
- Error: Validation failures
- Warn: Storage operations

## Testing

```bash
# Run unit tests
mvn test

# Run service and test manually
mvn spring-boot:run

# In another terminal:
curl -X POST http://localhost:8081/api/metrics -H "Content-Type: application/json" -d '{"serviceName":"test-service","endpoint":"/test","timestamp":"2024-01-20T10:00:00.000Z","latencyMs":100,"statusCode":200}'
```

## Integration

### Via Gateway

In production, access through the API Gateway:

```bash
# Through gateway (port 8080)
curl -X POST http://localhost:8080/api/metrics \
  -H "Content-Type: application/json" \
  -d '{...}'
```

### Direct Access

For testing, access the service directly:

```bash
# Direct access (port 8081)
curl -X POST http://localhost:8081/api/metrics \
  -H "Content-Type: application/json" \
  -d '{...}'
```

## Future Enhancements

- [ ] Persistent database integration
- [ ] Metric aggregation and rollups
- [ ] Custom validation rules
- [ ] Metric retention policies
- [ ] Bulk export capabilities
- [ ] Real-time metric streaming
- [ ] Advanced querying and filtering

## Related Documentation

- [Metrics Schema](../docs/METRICS_SCHEMA.md) - Complete schema specification
- [API Documentation](../docs/API.md) - API reference
- [Architecture](../docs/ARCHITECTURE.md) - System architecture
- [Main README](../README.md) - Project overview

---

**Port:** 8081  
**Version:** 1.0  
**Spring Boot:** 3.2.1
