#!/bin/bash

# CloudWatchr - Stop All Services
# This script stops all running services

echo "====================================="
echo "  CloudWatchr - Stopping All Services"
echo "====================================="
echo ""

# Colors
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m'

# Function to stop a service
stop_service() {
    local service_name=$1
    local pid_file="logs/$service_name.pid"
    
    if [ -f "$pid_file" ]; then
        local pid=$(cat $pid_file)
        if ps -p $pid > /dev/null 2>&1; then
            echo -e "${YELLOW}Stopping $service_name (PID: $pid)...${NC}"
            kill $pid
            sleep 2
            
            # Force kill if still running
            if ps -p $pid > /dev/null 2>&1; then
                echo -e "${YELLOW}Force stopping $service_name...${NC}"
                kill -9 $pid
            fi
            
            echo -e "${GREEN}✓ $service_name stopped${NC}"
        else
            echo -e "${YELLOW}⚠ $service_name was not running${NC}"
        fi
        rm $pid_file
    else
        echo -e "${YELLOW}⚠ No PID file found for $service_name${NC}"
    fi
}

# Stop all services
if [ -d "logs" ]; then
    stop_service "gateway-service"
    stop_service "metrics-ingestion-service"
    stop_service "analytics-service"
    stop_service "alerting-service"
    stop_service "ai-insights-service"
    stop_service "frontend"
    
    echo ""
    echo -e "${GREEN}All services stopped${NC}"
    
    # Ask if user wants to clear logs
    echo ""
    read -p "Do you want to clear log files? (y/n) " -n 1 -r
    echo
    if [[ $REPLY =~ ^[Yy]$ ]]; then
        rm -rf logs/*.log
        echo -e "${GREEN}✓ Log files cleared${NC}"
    fi
else
    echo -e "${YELLOW}No logs directory found. Services may not be running.${NC}"
fi

echo ""
echo "====================================="
echo -e "${GREEN}Cleanup Complete${NC}"
echo "====================================="