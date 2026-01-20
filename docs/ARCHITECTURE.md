# CloudWatchr Architecture

## Overview

CloudWatchr is built using a microservices architecture with the following components:

## System Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                      Frontend (Next.js)                      │
│                     Port 3000                                │
└───────────────────────────┬─────────────────────────────────┘
                            │
                            ▼
┌─────────────────────────────────────────────────────────────┐
│                   Gateway Service (Spring Cloud Gateway)      │
│                          Port 8080                           │
└───┬──────────┬──────────┬──────────┬─────────────────────────┘
    │          │          │          │
    ▼          ▼          ▼          ▼
┌────────┐ ┌────────┐ ┌────────┐ ┌────────┐
│Metrics │ │Analytics│ │Alerting│ │AI      │
│8081    │ │8082    │ │8083    │ │8084    │
└────────┘ └────────┘ └────────┘ └────────┘
```

## Services

### 1. Gateway Service (Port 8080)
- **Technology**: Spring Cloud Gateway
- **Purpose**: API Gateway and routing
- **Routes**:
  - `/api/metrics/**` → Metrics Ingestion Service
  - `/api/analytics/**` → Analytics Service
  - `/api/alerts/**` → Alerting Service
  - `/api/ai/**` → AI Insights Service

### 2. Metrics Ingestion Service (Port 8081)
- **Technology**: Spring Boot 3.x
- **Purpose**: Collect and ingest metrics from various sources
- **Responsibilities**:
  - Metrics collection
  - Data validation
  - Initial processing

### 3. Analytics Service (Port 8082)
- **Technology**: Spring Boot 3.x
- **Purpose**: Process and analyze metrics data
- **Responsibilities**:
  - Data aggregation
  - Statistical analysis
  - Report generation

### 4. Alerting Service (Port 8083)
- **Technology**: Spring Boot 3.x
- **Purpose**: Manage alerts and notifications
- **Responsibilities**:
  - Alert rule evaluation
  - Notification dispatch
  - Alert history

### 5. AI Insights Service (Port 8084)
- **Technology**: Spring Boot 3.x
- **Purpose**: AI-powered insights and recommendations
- **Responsibilities**:
  - Anomaly detection
  - Predictive analytics
  - Intelligent recommendations

### 6. Frontend (Port 3000)
- **Technology**: Next.js 14 with App Router
- **Purpose**: User interface
- **Features**:
  - Dashboard
  - Real-time monitoring
  - Alert management
  - AI copilot interface

## Communication Patterns

### Synchronous Communication
- Frontend ↔ Gateway: REST API calls
- Gateway ↔ Services: HTTP routing

### Health Checks
- All services expose `/api/health` endpoint
- Spring Boot Actuator endpoints at `/actuator/health`

## Design Principles

1. **Independent Deployability**: Each service can be deployed independently
2. **Single Responsibility**: Each service has a clear, focused purpose
3. **Loose Coupling**: Services communicate through well-defined APIs
4. **Technology Agnostic**: Services can use different technologies as needed

## Scalability

Each service can be scaled independently based on load:
- Horizontal scaling: Run multiple instances
- Load balancing: Distribute traffic across instances
- Database per service: Each service can have its own data store

## Security Considerations

- Gateway acts as single entry point
- Service-to-service authentication (future)
- API rate limiting (future)
- HTTPS/TLS encryption (production)