# CloudWatchr

**CloudWatchr** is a production-grade cloud infrastructure monitoring platform with AI-powered insights. Built with a microservices architecture using Spring Boot and Next.js.

## Overview

CloudWatchr provides real-time monitoring, analytics, alerting, and AI-driven insights for cloud infrastructure. The platform consists of:

- **Metrics Ingestion**: Collect and process metrics from various sources
- **Analytics Engine**: Process and analyze metrics data
- **Intelligent Alerting**: Smart alerting with configurable thresholds
- **AI Insights**: Machine learning-powered anomaly detection and recommendations
- **Modern UI**: React-based dashboard for visualization and management

## Architecture

This is a **single repository** with multiple independent Spring Boot microservices and a Next.js frontend:

```
cloudwatchr/
├── gateway-service/              # API Gateway (Port 8080)
├── metrics-ingestion-service/    # Metrics collection (Port 8081)
├── analytics-service/            # Data analytics (Port 8082)
├── alerting-service/             # Alert management (Port 8083)
├── ai-insights-service/          # AI-powered insights (Port 8084)
├── frontend/                     # Next.js UI (Port 3000)
├── infra/                        # Infrastructure configs
├── docs/                         # Documentation
└── README.md
```

## Prerequisites

Before running CloudWatchr, ensure you have:

- **Java 17** or higher
- **Maven 3.8+**
- **Node.js 18+** and npm
- **Git**

### Verify installations:

```bash
java -version    # Should show Java 17+
mvn -version     # Should show Maven 3.8+
node -version    # Should show Node 18+
```

## Quick Start

### 1. Clone the repository

```bash
git clone https://github.com/PragatiAN1109/cloudwatchr.git
cd cloudwatchr
```

### 2. Start Backend Services

Each service runs independently. Open separate terminal windows for each:

#### Gateway Service (Port 8080)
```bash
cd gateway-service
mvn spring-boot:run
```

#### Metrics Ingestion Service (Port 8081)
```bash
cd metrics-ingestion-service
mvn spring-boot:run
```

#### Analytics Service (Port 8082)
```bash
cd analytics-service
mvn spring-boot:run
```

#### Alerting Service (Port 8083)
```bash
cd alerting-service
mvn spring-boot:run
```

#### AI Insights Service (Port 8084)
```bash
cd ai-insights-service
mvn spring-boot:run
```

### 3. Start Frontend

```bash
cd frontend
npm install
npm run dev
```

The UI will be available at: http://localhost:3000

## Service Ports

| Service | Port | URL |
|---------|------|-----|
| Gateway | 8080 | http://localhost:8080 |
| Metrics Ingestion | 8081 | http://localhost:8081 |
| Analytics | 8082 | http://localhost:8082 |
| Alerting | 8083 | http://localhost:8083 |
| AI Insights | 8084 | http://localhost:8084 |
| Frontend | 3000 | http://localhost:3000 |

## API Routes (via Gateway)

All backend services are accessible through the gateway:

- **Metrics**: `http://localhost:8080/api/metrics/**`
- **Analytics**: `http://localhost:8080/api/analytics/**`
- **Alerts**: `http://localhost:8080/api/alerts/**`
- **AI Insights**: `http://localhost:8080/api/ai/**`

### Health Checks

Each service exposes health endpoints:

```bash
# Direct service health
curl http://localhost:8081/api/health

# Via gateway
curl http://localhost:8080/api/metrics/health

# Actuator health
curl http://localhost:8081/actuator/health
```

Expected response:
```json
{
  "service": "metrics-ingestion-service",
  "status": "UP"
}
```

## Development Workflow

### Running Individual Services

You can run services independently for development:

```bash
# Run with Maven
cd <service-directory>
mvn spring-boot:run

# Or compile and run
mvn clean package
java -jar target/<service-name>-0.0.1-SNAPSHOT.jar
```

### Frontend Development

```bash
cd frontend
npm run dev      # Development server
npm run build    # Production build
npm start        # Production server
```

## Common Issues & Troubleshooting

### Port Already in Use

**Error**: `Port 8080 already in use`

**Solution**:
```bash
# Find process using the port (Mac/Linux)
lsof -i :8080

# Kill the process
kill -9 <PID>

# Windows
netstat -ano | findstr :8080
taskkill /PID <PID> /F
```

### Java Version Mismatch

**Error**: `Unsupported class file major version`

**Solution**: Ensure Java 17 is active:
```bash
java -version
# If wrong version, update JAVA_HOME
export JAVA_HOME=$(/usr/libexec/java_home -v 17)  # Mac
```

### Maven Build Failures

**Solution**:
```bash
# Clean Maven cache
mvn clean install -U

# Skip tests if needed
mvn clean install -DskipTests
```

### Frontend Not Starting

**Solution**:
```bash
# Clear node_modules and reinstall
rm -rf node_modules package-lock.json
npm install
npm run dev
```

### Gateway Not Routing

**Issue**: Requests to gateway return 404

**Solution**: Ensure target services are running before starting the gateway. Check logs for routing configuration.

## Testing

### Backend Services

```bash
cd <service-directory>
mvn test
```

### Frontend

```bash
cd frontend
npm test
```

## Building for Production

### Backend

```bash
# Build all services
for service in gateway-service metrics-ingestion-service analytics-service alerting-service ai-insights-service; do
  cd $service
  mvn clean package
  cd ..
done
```

### Frontend

```bash
cd frontend
npm run build
```

## Project Structure Details

### Backend Services

Each service follows this structure:
```
service-name/
├── src/
│   ├── main/
│   │   ├── java/com/cloudwatchr/<service>/
│   │   │   ├── Application.java
│   │   │   ├── controller/
│   │   │   ├── service/
│   │   │   └── config/
│   │   └── resources/
│   │       └── application.yml
│   └── test/
└── pom.xml
```

### Frontend

```
frontend/
├── app/              # Next.js App Router
├── components/       # React components
├── public/          # Static assets
└── package.json
```

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

MIT License - see LICENSE file for details

## Support

For issues and questions:
- GitHub Issues: https://github.com/PragatiAN1109/cloudwatchr/issues
- Documentation: See `/docs` directory

---

**Built with ❤️ for cloud infrastructure monitoring**