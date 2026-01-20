export default function AICopilot() {
  return (
    <div className="container mx-auto px-4 py-8">
      <h1 className="text-4xl font-bold mb-6">AI Copilot</h1>
      <div className="bg-white dark:bg-gray-800 p-6 rounded-lg shadow-md">
        <h2 className="text-2xl font-semibold mb-4">AI-Powered Insights</h2>
        <p className="text-gray-600 dark:text-gray-400 mb-4">
          Get intelligent recommendations and anomaly detection powered by machine learning.
        </p>
        <div className="space-y-4">
          <div className="border-l-4 border-blue-500 pl-4 py-2">
            <p className="font-semibold">Insight: System Performance</p>
            <p className="text-sm text-gray-600 dark:text-gray-400">
              Your system is performing optimally. No anomalies detected in the last 24 hours.
            </p>
          </div>
          <div className="border-l-4 border-green-500 pl-4 py-2">
            <p className="font-semibold">Recommendation: Resource Optimization</p>
            <p className="text-sm text-gray-600 dark:text-gray-400">
              Current resource utilization is efficient. No optimization needed at this time.
            </p>
          </div>
        </div>
      </div>
    </div>
  )
}