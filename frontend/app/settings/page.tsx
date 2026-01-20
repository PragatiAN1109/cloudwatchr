export default function Settings() {
  return (
    <div className="container mx-auto px-4 py-8">
      <h1 className="text-4xl font-bold mb-6">Settings</h1>
      <div className="bg-white dark:bg-gray-800 p-6 rounded-lg shadow-md">
        <h2 className="text-2xl font-semibold mb-4">Application Settings</h2>
        <div className="space-y-4">
          <div>
            <label className="block font-semibold mb-2">Gateway URL</label>
            <input 
              type="text" 
              value={process.env.NEXT_PUBLIC_GATEWAY_URL || 'http://localhost:8080'} 
              readOnly
              className="w-full p-2 border border-gray-300 dark:border-gray-600 rounded bg-gray-50 dark:bg-gray-900"
            />
          </div>
          <div>
            <label className="block font-semibold mb-2">Refresh Interval</label>
            <select className="w-full p-2 border border-gray-300 dark:border-gray-600 rounded bg-white dark:bg-gray-900">
              <option>10 seconds</option>
              <option>30 seconds</option>
              <option>1 minute</option>
              <option>5 minutes</option>
            </select>
          </div>
        </div>
      </div>
    </div>
  )
}