import { ElNotification } from "element-plus/lib/components/index.js";
import { RouteRecordRaw, createRouter, createWebHashHistory } from "vue-router";
 
// 路由表
const routes: RouteRecordRaw[] = [
    {path: "/", redirect: "/home"},
    {
        path: "/home", 
        component: () => import("../views/index/Home.vue"),
        children: [
            {path: "/index", component: () => import("../views/index/Index.vue")},
            {path: "/show0", component: () => import("../views/index/Show0.vue")},
            {path: "/show89", component: () => import("../views/index/Show89.vue")},
            {path: "/show92", component: () => import("../views/index/Show92.vue")},
            {path: "/show95", component: () => import("../views/index/Show95.vue")},
            {path: "/show98", component: () => import("../views/index/Show98.vue")},
            {path: "/myCar", component: () => import("../views/car/MyCar.vue")},
            {path: "/addOil", component: () => import("../views/addOil/Record.vue")},
            {path: "/fare", component: () => import("../views/fare/Fare.vue")},
        ]},
    {path: "/record", component: () => import("../views/Record.vue")},
    {path: "/find", component: () => import("../views/Find.vue")},
    {path: "/settings", component: () => import("../views/Settings.vue")},
    {path: "/login", component: () => import("../views/user/Login.vue")},
    {path: "/enroll", component: () => import("../views/user/Enroll.vue")},
    {path: "/redirect", component: () => import("../components/Redirect.vue")},
    {path: "/predict", component: () => import("../components/Predict.vue")},
    {path: "/calculator", component: () => import("../components/Calculator.vue")},
];
 
// 路由
const router = createRouter({
    history: createWebHashHistory(),
    routes
});

// 全局路由守卫
router.beforeEach((to, _, next) => {
    const token = localStorage.getItem("token");
    // 需要登录授权的集合
    const authList = ["/myCar", "/addOil", "/fare", "/record"];

    // 如果需要授权且未登录，显示提示通知
    if (authList.includes(to.path) && !token) {
        ElNotification({
            title: "提示",
            message: "用户未登录",
            duration: 1000
        });
    } else {
        next();
    }
});

export default router
