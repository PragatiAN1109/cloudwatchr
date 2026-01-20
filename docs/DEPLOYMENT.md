# Deployment Guide

## Local Development

See the main [README.md](../README.md) for local development setup.

## Production Deployment

### Prerequisites

- Docker and Docker Compose (recommended)
- Java 17 runtime
- Node.js 18+ runtime
- Reverse proxy (nginx, Apache, etc.)

### Building Services

#### Backend Services

```bash
# Build all services
for service in gateway-service metrics-ingestion-service analytics-service alerting-service ai-insights-service; do
  cd $service
  mvn clean package -DskipTests
  cd ..
done
```

JAR files will be in `target/` directory of each service.

#### Frontend

```bash
cd frontend
npm run build
```

### Docker Deployment (Recommended)

*Docker configuration coming soon*

### Manual Deployment

#### 1. Deploy Backend Services

On production servers:

```bash
# Copy JAR files to server
scp target/*.jar user@server:/opt/cloudwatchr/

# Run as systemd service or supervisor
java -jar /opt/cloudwatchr/gateway-service-0.0.1-SNAPSHOT.jar
```

#### 2. Deploy Frontend

```bash
# Option 1: Node.js server
cd frontend
npm start

# Option 2: Static export
npm run build
# Serve the 'out' directory with nginx or similar
```

### Environment Variables

#### Backend Services

```bash
# Set in application.yml or as environment variables
SERVER_PORT=8080
SPRING_PROFILES_ACTIVE=production
```

#### Frontend

```bash
NEXT_PUBLIC_GATEWAY_URL=https://api.cloudwatchr.com
```

### Health Checks

Configure health check endpoints in your load balancer:

```
GET /actuator/health
```

Expected response: `200 OK`

### Monitoring

- Enable Spring Boot Actuator metrics
- Use Prometheus for metrics collection
- Use Grafana for visualization

### Scaling

#### Horizontal Scaling

- Run multiple instances of each service
- Use load balancer (nginx, HAProxy, AWS ALB)
- Configure session persistence if needed

#### Vertical Scaling

Adjust JVM heap size:

```bash
java -Xmx2g -Xms1g -jar service.jar
```

### Security

1. **HTTPS**: Use SSL/TLS certificates
2. **Firewall**: Restrict access to internal services
3. **Authentication**: Implement OAuth2 or JWT
4. **Secrets**: Use environment variables or secret management

### Backup and Recovery

*Configuration coming soon*

### Troubleshooting

#### Service Won't Start

```bash
# Check logs
tail -f /var/log/cloudwatchr/service.log

# Check port availability
netstat -tulpn | grep 8080
```

#### High Memory Usage

```bash
# Adjust JVM settings
java -XX:+UseG1GC -Xmx1g -jar service.jar
```