import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vite.dev/config/

export default defineConfig({
  // root: './client',
  plugins: [react()],
  server: {
    host: '0.0.0.0', // Expose to all network interfaces
    port: 5173, // You can change this to any port you want (e.g., 5173)
  },
});
