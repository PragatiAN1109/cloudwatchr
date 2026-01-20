# CloudWatchr Scripts

This directory contains utility scripts for development and operations.

## Available Scripts

### Start All Services

**Location**: `../start-all-services.sh`

Starts all backend services and the frontend in the background.

```bash
./start-all-services.sh
```

**What it does:**
- Checks prerequisites (Java, Maven, Node.js)
- Starts all 5 backend services
- Starts the frontend
- Creates logs in `logs/` directory
- Performs health checks

### Stop All Services

**Location**: `../stop-all-services.sh`

Stops all running services.

```bash
./stop-all-services.sh
```

**What it does:**
- Stops all backend services
- Stops the frontend
- Optionally clears log files

## Making Scripts Executable

Before using the scripts, make them executable:

```bash
chmod +x start-all-services.sh
chmod +x stop-all-services.sh
```

## Logs

All service logs are stored in the `logs/` directory:
- `gateway-service.log`
- `metrics-ingestion-service.log`
- `analytics-service.log`
- `alerting-service.log`
- `ai-insights-service.log`
- `frontend.log`

### Viewing Logs

```bash
# View specific service log
tail -f logs/gateway-service.log

# View all logs
tail -f logs/*.log
```

## Process Management

PID files are stored in `logs/` for each service:
- `gateway-service.pid`
- `metrics-ingestion-service.pid`
- etc.

### Manual Process Control

```bash
# Kill a specific service
kill $(cat logs/gateway-service.pid)

# Force kill
kill -9 $(cat logs/gateway-service.pid)
```

## Troubleshooting

### Port Already in Use

If a port is already in use:

```bash
# Find and kill process on port 8080
lsof -ti:8080 | xargs kill -9
```

### Services Won't Start

1. Check prerequisites:
   ```bash
   java -version   # Should be 17+
   mvn -version    # Should be 3.8+
   node -version   # Should be 18+
   ```

2. Check logs:
   ```bash
   tail -f logs/<service-name>.log
   ```

3. Try manual start:
   ```bash
   cd <service-directory>
   mvn clean install
   mvn spring-boot:run
   ```

## Future Scripts

Planned additions:
- `build-all.sh` - Build all services
- `test-all.sh` - Run all tests
- `deploy.sh` - Deploy to production
- `backup.sh` - Backup data
- `health-check.sh` - Check all service health