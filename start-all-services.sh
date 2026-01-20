#!/bin/bash

# CloudWatchr - Start All Services
# This script starts all backend services and the frontend

echo "====================================="
echo "  CloudWatchr - Starting All Services"
echo "====================================="
echo ""

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Check if Java is installed
if ! command -v java &> /dev/null; then
    echo -e "${RED}Error: Java is not installed${NC}"
    echo "Please install Java 17 or higher"
    exit 1
fi

# Check if Maven is installed
if ! command -v mvn &> /dev/null; then
    echo -e "${RED}Error: Maven is not installed${NC}"
    echo "Please install Maven 3.8 or higher"
    exit 1
fi

# Check if Node is installed
if ! command -v node &> /dev/null; then
    echo -e "${RED}Error: Node.js is not installed${NC}"
    echo "Please install Node.js 18 or higher"
    exit 1
fi

echo -e "${GREEN}✓ Prerequisites check passed${NC}"
echo ""

# Function to start a service
start_service() {
    local service_name=$1
    local port=$2
    
    echo -e "${YELLOW}Starting $service_name on port $port...${NC}"
    cd $service_name
    mvn spring-boot:run > ../logs/$service_name.log 2>&1 &
    echo $! > ../logs/$service_name.pid
    cd ..
    echo -e "${GREEN}✓ $service_name started (PID: $(cat logs/$service_name.pid))${NC}"
    sleep 2
}

# Create logs directory
mkdir -p logs

echo "Starting Backend Services..."
echo ""

# Start services in order
start_service "metrics-ingestion-service" "8081"
start_service "analytics-service" "8082"
start_service "alerting-service" "8083"
start_service "ai-insights-service" "8084"
start_service "gateway-service" "8080"

echo ""
echo "Starting Frontend..."
echo ""

cd frontend

# Check if node_modules exists
if [ ! -d "node_modules" ]; then
    echo -e "${YELLOW}Installing frontend dependencies...${NC}"
    npm install
fi

# Check if .env.local exists
if [ ! -f ".env.local" ]; then
    echo -e "${YELLOW}Creating .env.local from example...${NC}"
    cp .env.local.example .env.local
fi

npm run dev > ../logs/frontend.log 2>&1 &
echo $! > ../logs/frontend.pid
cd ..

echo -e "${GREEN}✓ Frontend started (PID: $(cat logs/frontend.pid))${NC}"
echo ""

echo "====================================="
echo -e "${GREEN}All Services Started Successfully!${NC}"
echo "====================================="
echo ""
echo "Service URLs:"
echo "  Gateway:              http://localhost:8080"
echo "  Metrics Ingestion:    http://localhost:8081"
echo "  Analytics:            http://localhost:8082"
echo "  Alerting:             http://localhost:8083"
echo "  AI Insights:          http://localhost:8084"
echo "  Frontend:             http://localhost:3000"
echo ""
echo "Logs are available in the 'logs/' directory"
echo ""
echo "To stop all services, run: ./stop-all-services.sh"
echo ""
echo "Health check: curl http://localhost:8080/api/health"
echo ""

# Wait a moment for services to start
echo "Waiting for services to initialize..."
sleep 10

# Check if services are responding
echo ""
echo "Checking service health..."
for port in 8080 8081 8082 8083 8084; do
    if curl -s http://localhost:$port/api/health > /dev/null; then
        echo -e "${GREEN}✓ Service on port $port is responding${NC}"
    else
        echo -e "${YELLOW}⚠ Service on port $port is not responding yet${NC}"
    fi
done

echo ""
echo -e "${GREEN}Setup complete! Open http://localhost:3000 in your browser${NC}"