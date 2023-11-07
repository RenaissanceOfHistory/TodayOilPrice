import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [vue()],
  server: {
    base: "/today-oil-price/v1/",
    proxy: {
      //api是自行设置的请求前缀，任何请求路径以/api开头的请求将被代理到对应的target目标
      "/api": {
        target: "http://localhost:8766/today-oil/v1",
        changeOrigin: true,
        secure: true,
        rewrite: (path) => path.replace(/^\/api/, "")
      }
    }
  },
});


