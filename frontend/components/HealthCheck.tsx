'use client'

import { useState, useEffect } from 'react'

interface ServiceHealth {
  service: string
  status: 'UP' | 'DOWN' | 'CHECKING'
}

export default function HealthCheck() {
  const [services, setServices] = useState<ServiceHealth[]>([
    { service: 'gateway-service', status: 'CHECKING' },
    { service: 'metrics-ingestion-service', status: 'CHECKING' },
    { service: 'analytics-service', status: 'CHECKING' },
    { service: 'alerting-service', status: 'CHECKING' },
    { service: 'ai-insights-service', status: 'CHECKING' },
  ])

  const gatewayUrl = process.env.NEXT_PUBLIC_GATEWAY_URL || 'http://localhost:8080'

  useEffect(() => {
    const checkHealth = async () => {
      const serviceChecks = [
        { name: 'gateway-service', port: 8080, direct: true },
        { name: 'metrics-ingestion-service', port: 8081, route: '/api/metrics/health' },
        { name: 'analytics-service', port: 8082, route: '/api/analytics/health' },
        { name: 'alerting-service', port: 8083, route: '/api/alerts/health' },
        { name: 'ai-insights-service', port: 8084, route: '/api/ai/health' },
      ]

      const results = await Promise.all(
        serviceChecks.map(async (check) => {
          try {
            const url = check.direct 
              ? `http://localhost:${check.port}/api/health`
              : `${gatewayUrl}${check.route}`
            
            const response = await fetch(url, {
              method: 'GET',
              headers: { 'Content-Type': 'application/json' },
            })
            
            if (response.ok) {
              const data = await response.json()
              return { service: check.name, status: 'UP' as const }
            }
            return { service: check.name, status: 'DOWN' as const }
          } catch (error) {
            return { service: check.name, status: 'DOWN' as const }
          }
        })
      )

      setServices(results)
    }

    checkHealth()
    const interval = setInterval(checkHealth, 30000) // Check every 30 seconds

    return () => clearInterval(interval)
  }, [gatewayUrl])

  const getStatusColor = (status: string) => {
    switch (status) {
      case 'UP':
        return 'text-green-600 dark:text-green-400'
      case 'DOWN':
        return 'text-red-600 dark:text-red-400'
      default:
        return 'text-yellow-600 dark:text-yellow-400'
    }
  }

  const getStatusIcon = (status: string) => {
    switch (status) {
      case 'UP':
        return '✓'
      case 'DOWN':
        return '✗'
      default:
        return '⟳'
    }
  }

  return (
    <div className="bg-white dark:bg-gray-800 p-6 rounded-lg shadow-md">
      <h2 className="text-2xl font-semibold mb-4">Service Health Check</h2>
      <div className="space-y-3">
        {services.map((service) => (
          <div 
            key={service.service}
            className="flex items-center justify-between p-3 border border-gray-200 dark:border-gray-700 rounded"
          >
            <span className="font-medium">{service.service}</span>
            <span className={`font-bold ${getStatusColor(service.status)}`}>
              {getStatusIcon(service.status)} {service.status}
            </span>
          </div>
        ))}
      </div>
      <p className="text-sm text-gray-500 dark:text-gray-400 mt-4">
        Gateway URL: {gatewayUrl}
      </p>
    </div>
  )
}