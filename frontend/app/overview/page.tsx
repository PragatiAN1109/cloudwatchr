export default function Overview() {
  return (
    <div className="container mx-auto px-4 py-8">
      <h1 className="text-4xl font-bold mb-6">Overview</h1>
      <div className="bg-white dark:bg-gray-800 p-6 rounded-lg shadow-md">
        <h2 className="text-2xl font-semibold mb-4">System Overview</h2>
        <p className="text-gray-600 dark:text-gray-400 mb-4">
          Monitor all your cloud resources from a single dashboard.
        </p>
        <div className="grid md:grid-cols-3 gap-4">
          <div className="border border-gray-300 dark:border-gray-600 p-4 rounded">
            <h3 className="font-semibold text-lg mb-2">Active Services</h3>
            <p className="text-3xl font-bold text-blue-600">5</p>
          </div>
          <div className="border border-gray-300 dark:border-gray-600 p-4 rounded">
            <h3 className="font-semibold text-lg mb-2">Metrics Collected</h3>
            <p className="text-3xl font-bold text-green-600">1,247</p>
          </div>
          <div className="border border-gray-300 dark:border-gray-600 p-4 rounded">
            <h3 className="font-semibold text-lg mb-2">Active Alerts</h3>
            <p className="text-3xl font-bold text-yellow-600">0</p>
          </div>
        </div>
      </div>
    </div>
  )
}