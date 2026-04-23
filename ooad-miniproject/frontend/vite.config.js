import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import electron from 'vite-plugin-electron'
import renderer from 'vite-plugin-electron-renderer'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [
    react(),
    electron([
      {
        // Main-process entry file of the Electron App
        entry: 'main.cjs',
      },
    ]),
    renderer(),
  ],
  server: {
    port: 5173,
    strictPort: true,
  },
})
