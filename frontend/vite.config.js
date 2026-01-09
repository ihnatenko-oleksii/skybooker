import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

export default defineConfig({
  plugins: [react()],
  server: {
    host: '0.0.0.0',
    port: 5173,
    proxy: {
      '/api': {
        // W Dockerze użyj nazwy serwisu "backend", lokalnie "localhost"
        target: process.env.VITE_API_URL || 'http://localhost:8080',
        changeOrigin: true,
        secure: false,
        ws: true
      }
    }
  }
})
