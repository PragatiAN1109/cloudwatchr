export default function Alerts() {
  return (
    <div className="container mx-auto px-4 py-8">
      <h1 className="text-4xl font-bold mb-6">Alerts</h1>
      <div className="bg-white dark:bg-gray-800 p-6 rounded-lg shadow-md">
        <h2 className="text-2xl font-semibold mb-4">Alert Management</h2>
        <p className="text-gray-600 dark:text-gray-400 mb-4">
          Configure and manage your alert rules and notifications.
        </p>
        <div className="bg-green-50 dark:bg-green-900/20 border border-green-200 dark:border-green-800 p-4 rounded">
          <p className="text-green-800 dark:text-green-300 font-semibold">
            ✓ All systems operational - No active alerts
          </p>
        </div>
      </div>
    </div>
  )
}