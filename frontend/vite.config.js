import { defineConfig, loadEnv } from 'vite'
import react from '@vitejs/plugin-react'

export default defineConfig(({ mode }) => {
  // W vite.config.js używamy loadEnv do załadowania zmiennych środowiskowych
  const env = loadEnv(mode, process.cwd(), '');
  // W Dockerze użyj API_TARGET, lokalnie domyślnie localhost
  const apiTarget = env.API_TARGET || 'http://localhost:8080';
  
  return {
    plugins: [react()],
    server: {
      host: '0.0.0.0',
      port: 5173,
      proxy: {
        '/api': {
          target: apiTarget,
          changeOrigin: true,
          secure: false,
          ws: true
        }
      }
    }
  }
})
