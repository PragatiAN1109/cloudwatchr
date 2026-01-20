# CloudWatchr API Documentation

## Base URL

All API requests should go through the Gateway Service:

```
http://localhost:8080
```

## Health Endpoints

### Check Service Health

**GET** `/api/health`

Returns the health status of a service.

**Example Request:**
```bash
curl http://localhost:8080/api/metrics/health
```

**Example Response:**
```json
{
  "service": "metrics-ingestion-service",
  "status": "UP"
}
```

## Metrics API

### Get Metrics

**GET** `/api/metrics`

Retrieve collected metrics.

**Example:**
```bash
curl http://localhost:8080/api/metrics
```

### Submit Metrics

**POST** `/api/metrics`

Submit new metrics data.

**Request Body:**
```json
{
  "source": "server-01",
  "metrics": {
    "cpu": 45.2,
    "memory": 78.5,
    "disk": 62.1
  },
  "timestamp": "2024-01-20T10:30:00Z"
}
```

## Analytics API

### Get Analytics

**GET** `/api/analytics`

Retrieve analytics data.

**Example:**
```bash
curl http://localhost:8080/api/analytics
```

### Generate Report

**POST** `/api/analytics/report`

Generate an analytics report.

**Request Body:**
```json
{
  "timeRange": "24h",
  "metrics": ["cpu", "memory"],
  "aggregation": "average"
}
```

## Alerts API

### Get Alerts

**GET** `/api/alerts`

Retrieve all alerts.

**Example:**
```bash
curl http://localhost:8080/api/alerts
```

### Create Alert

**POST** `/api/alerts`

Create a new alert rule.

**Request Body:**
```json
{
  "name": "High CPU Alert",
  "condition": "cpu > 80",
  "severity": "high",
  "notification": {
    "email": "admin@example.com"
  }
}
```

## AI Insights API

### Get Insights

**GET** `/api/ai/insights`

Retrieve AI-powered insights.

**Example:**
```bash
curl http://localhost:8080/api/ai/insights
```

**Example Response:**
```json
{
  "message": "AI Insights service operational",
  "insights": [
    {
      "type": "anomaly",
      "description": "Sample insight - system performance normal"
    }
  ]
}
```

### Analyze Metrics

**POST** `/api/ai/analyze`

Submit metrics for AI analysis.

**Request Body:**
```json
{
  "metrics": {
    "cpu": [45, 67, 89, 92, 76],
    "memory": [62, 65, 68, 71, 69]
  },
  "timeWindow": "1h"
}
```

**Example Response:**
```json
{
  "message": "Analysis complete",
  "recommendation": "All systems operating normally"
}
```

## Error Responses

All endpoints may return the following error responses:

### 400 Bad Request
```json
{
  "error": "Invalid request format",
  "details": "Missing required field: source"
}
```

### 404 Not Found
```json
{
  "error": "Resource not found",
  "path": "/api/metrics/12345"
}
```

### 500 Internal Server Error
```json
{
  "error": "Internal server error",
  "message": "An unexpected error occurred"
}
```

## Rate Limiting

*Coming soon*

## Authentication

*Coming soon*