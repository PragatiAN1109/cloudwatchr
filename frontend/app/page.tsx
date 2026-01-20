import HealthCheck from '@/components/HealthCheck'

export default function Home() {
  return (
    <div className="container mx-auto px-4 py-8">
      <div className="max-w-4xl mx-auto">
        <h1 className="text-5xl font-bold text-center mb-4">
          CloudWatchr
        </h1>
        <p className="text-xl text-center text-gray-600 dark:text-gray-400 mb-8">
          AI-Powered Cloud Infrastructure Monitoring
        </p>
        
        <div className="grid md:grid-cols-2 gap-6 mb-8">
          <div className="bg-white dark:bg-gray-800 p-6 rounded-lg shadow-md">
            <h2 className="text-2xl font-semibold mb-3">🎯 Real-time Monitoring</h2>
            <p className="text-gray-600 dark:text-gray-400">
              Monitor your cloud infrastructure in real-time with comprehensive metrics collection and analysis.
            </p>
          </div>
          
          <div className="bg-white dark:bg-gray-800 p-6 rounded-lg shadow-md">
            <h2 className="text-2xl font-semibold mb-3">🤖 AI Insights</h2>
            <p className="text-gray-600 dark:text-gray-400">
              Leverage machine learning for anomaly detection and intelligent recommendations.
            </p>
          </div>
          
          <div className="bg-white dark:bg-gray-800 p-6 rounded-lg shadow-md">
            <h2 className="text-2xl font-semibold mb-3">🔔 Smart Alerting</h2>
            <p className="text-gray-600 dark:text-gray-400">
              Get notified instantly when issues are detected with configurable alert thresholds.
            </p>
          </div>
          
          <div className="bg-white dark:bg-gray-800 p-6 rounded-lg shadow-md">
            <h2 className="text-2xl font-semibold mb-3">📊 Advanced Analytics</h2>
            <p className="text-gray-600 dark:text-gray-400">
              Deep dive into your metrics with powerful analytics and visualization tools.
            </p>
          </div>
        </div>
        
        <HealthCheck />
      </div>
    </div>
  )
}