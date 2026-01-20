# Infrastructure Configuration

This directory contains infrastructure-related configuration files for CloudWatchr.

## Files

- **docker-compose.yml**: Docker Compose configuration for running all services in containers
- *Future additions*:
  - Kubernetes manifests
  - Terraform configurations
  - CI/CD pipeline definitions

## Docker Setup

### Prerequisites

- Docker 20.10+
- Docker Compose 2.0+

### Usage

*Note: Dockerfiles need to be created for each service before using docker-compose*

```bash
# Build and start all services
docker-compose up --build

# Start in background
docker-compose up -d

# Stop all services
docker-compose down

# View logs
docker-compose logs -f
```

## Creating Dockerfiles

Each service needs a Dockerfile. Example for a Spring Boot service:

```dockerfile
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

## Environment Variables

Configure environment-specific variables in docker-compose.yml or use a .env file.

## Networking

All services run on the `cloudwatchr-network` bridge network, allowing inter-service communication.

## Future Plans

- Kubernetes deployment manifests
- Helm charts
- Terraform infrastructure as code
- CI/CD pipeline with GitHub Actions
- Production-ready Docker images