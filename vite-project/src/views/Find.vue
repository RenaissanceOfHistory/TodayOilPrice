<template>
  <div>
    <el-container class="w-100">
      <el-main>
        <!-- 功能模块 -->
        <div class="row">
          <div class="col-12 col-sm-3 col-md-2 mb-2 module" v-for="(item, index) in modules" :key="index">
            <el-card shadow="hover" class="text-center" @click="toModule(item.to)">
              <img :src="item.img" :alt="item.title" :title="item.title" class="module-img">
              <p class="mt-2 text-muted">{{ item.title }}</p>
            </el-card>
          </div>
        </div>
      </el-main>
    </el-container>
  </div>
</template>

<script setup lang="ts">
import { ElNotification } from 'element-plus/lib/components/index.js';
import router from '../router/index';

const modules = [
  { title: "国际油价", img: "/static/img/earth.png", to: "/predict" },
  { title: "资讯", img: "/static/img/news.png" },
  { title: "优惠加油站", img: "/static/img/addOilStation.png" },
  { title: "调价日期", img: "/static/img/date.png" },
  { title: "油耗计算器", img: "/static/img/calculator.png", to: "/calculator" },
  { title: "停车场", img: "/static/img/parking.png" },
];

function toModule(to: string | undefined) {
  if (!to) {
    ElNotification({
      title: "提示",
      message: "功能暂未开发",
      duration: 1000
    });
    return;
  }

  router.push(to);
}
</script>

<style scoped>
.module {
  cursor: pointer;
}

.module-img {
  width: 100px;
  height: 100px;
}
</style>
