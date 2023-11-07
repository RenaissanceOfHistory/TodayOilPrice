import { createApp } from 'vue'
import App from './App.vue'
import router from "./router";

// element-plus
import * as ElementPlus from "element-plus";
import "element-plus/theme-chalk/index.css";

// bootstrap
import "./assets/style.scss";
import "@popperjs/core";
import "bootstrap";


const app = createApp(App);
app
.use(router)
.use(ElementPlus)
.mount("#app");
