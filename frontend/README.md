# CloudWatchr Frontend

Next.js-based frontend for the CloudWatchr cloud infrastructure monitoring platform.

## Technology Stack

- **Framework**: Next.js 14 with App Router
- **React**: 18.x
- **Styling**: Tailwind CSS
- **TypeScript**: 5.x

## Prerequisites

- Node.js 18.x or higher
- npm or yarn

## Getting Started

### 1. Install Dependencies

```bash
npm install
```

### 2. Configure Environment

Create a `.env.local` file:

```bash
cp .env.local.example .env.local
```

Edit `.env.local`:

```env
NEXT_PUBLIC_GATEWAY_URL=http://localhost:8080
```

### 3. Run Development Server

```bash
npm run dev
```

Open [http://localhost:3000](http://localhost:3000) in your browser.

## Available Scripts

- `npm run dev` - Start development server
- `npm run build` - Build for production
- `npm start` - Start production server
- `npm run lint` - Run ESLint

## Project Structure

```
frontend/
├── app/                  # Next.js App Router pages
│   ├── layout.tsx        # Root layout
│   ├── page.tsx          # Home page
│   ├── globals.css       # Global styles
│   ├── overview/         # Overview page
│   ├── alerts/           # Alerts page
│   ├── ai-copilot/       # AI Copilot page
│   └── settings/         # Settings page
├── components/          # React components
│   ├── Navigation.tsx   # Navigation bar
│   └── HealthCheck.tsx  # Health check component
├── public/              # Static assets
├── package.json         # Dependencies
├── tsconfig.json        # TypeScript config
├── tailwind.config.ts   # Tailwind config
└── next.config.js       # Next.js config
```

## Features

### Pages

1. **Home** (`/`) - Landing page with feature overview
2. **Overview** (`/overview`) - System overview and metrics
3. **Alerts** (`/alerts`) - Alert management
4. **AI Copilot** (`/ai-copilot`) - AI-powered insights
5. **Settings** (`/settings`) - Application settings

### Components

#### Navigation
- Responsive navigation bar
- Active route highlighting
- Links to all main pages

#### HealthCheck
- Real-time service health monitoring
- Connects to backend services via gateway
- Auto-refresh every 30 seconds
- Color-coded status indicators

## Backend Integration

### Gateway Connection

The frontend connects to the backend through the API Gateway:

```typescript
const gatewayUrl = process.env.NEXT_PUBLIC_GATEWAY_URL || 'http://localhost:8080'
```

### Health Check Endpoints

The HealthCheck component queries:
- Direct: `http://localhost:8080/api/health`
- Via Gateway:
  - `/api/metrics/health`
  - `/api/analytics/health`
  - `/api/alerts/health`
  - `/api/ai/health`

## Styling

### Tailwind CSS

The project uses Tailwind CSS for styling with:
- Responsive design
- Dark mode support
- Custom color schemes

### Adding Custom Styles

1. Use Tailwind utility classes in JSX
2. Add custom styles in `globals.css`
3. Extend Tailwind config in `tailwind.config.ts`

## Development Tips

### Hot Reload

Next.js automatically reloads when you save files.

### Type Checking

```bash
npx tsc --noEmit
```

### Linting

```bash
npm run lint
```

### Format Code

```bash
npx prettier --write .
```

## Building for Production

### Build

```bash
npm run build
```

### Start Production Server

```bash
npm start
```

### Export Static Site

Add to `next.config.js`:

```javascript
module.exports = {
  output: 'export'
}
```

Then:

```bash
npm run build
```

Static files will be in the `out/` directory.

## Troubleshooting

### Port Already in Use

```bash
# Kill process on port 3000
lsof -ti:3000 | xargs kill -9
```

### Module Not Found

```bash
# Clear cache and reinstall
rm -rf node_modules .next
npm install
```

### Backend Connection Issues

1. Verify gateway is running on port 8080
2. Check CORS configuration
3. Verify `.env.local` is properly configured

### Build Errors

```bash
# Clear Next.js cache
rm -rf .next
npm run build
```

## Environment Variables

| Variable | Description | Default |
|----------|-------------|--------|
| NEXT_PUBLIC_GATEWAY_URL | Backend gateway URL | http://localhost:8080 |

## Browser Support

- Chrome (latest)
- Firefox (latest)
- Safari (latest)
- Edge (latest)

## Contributing

See the main repository [README.md](../README.md) for contribution guidelines.