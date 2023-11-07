<template>
  <div>
    <!-- 首页容器 -->
    <el-container>
      <!-- 头部 -->
      <el-header class="h-auto">
        <!-- 标题 -->
        <el-text class="h5">今日最新国内油价查询</el-text>
        <!-- 分隔线 -->
        <el-divider direction="horizontal" content-position="left" class="mt-2"></el-divider>
        <!-- 省份集合 -->
        <el-space wrap>
          <!-- 存在数据时显示 -->
          <template v-if="provinceList.length > 0">
            <el-button v-for="(pro, index) in provinceList" :key="index" @click="showInfo(index)" text>{{ pro
            }}</el-button>
          </template>
          <!-- 否则，显示通知 -->
          <template v-else>
            <el-alert title="无省份信息" type="info" effect="light" show-icon closable></el-alert>
          </template>
        </el-space>
      </el-header>

      <!-- 主体 -->
      <el-main class="h-auto mt-5">
        <!-- 标题 -->
        <el-text class="h5">国内城市油价查询</el-text>
        <!-- 油价信息表格 -->
        <el-table class="mt-3" :data="infoList" height="300" border v-loading="loading">
          <el-table-column label="地区" prop="area"></el-table-column>
          <el-table-column label="89号汽油" prop="label_89"></el-table-column>
          <el-table-column label="92号汽油" prop="label_92"></el-table-column>
          <el-table-column label="95号汽油" prop="label_95"></el-table-column>
          <el-table-column label="0号柴油" prop="label_0"></el-table-column>
        </el-table>
      </el-main>
    </el-container>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue';
import axios from 'axios';
import { ElNotification } from 'element-plus/lib/components/index.js';

// 省份列表
const provinceList = ref<string[]>([]);
// 搜索省份名
const provinceName = ref("");
// 是否显示加载状态
const loading = ref(true);
// 省份信息列表
const infoList = ref<Object[]>([]);

/** 获取指定省份油价信息 */
async function getInfoList() {
  axios.get(`/api/oil-prices/prov/${provinceName.value}`)
    .then(value => {
      const result: any = value.data;
      loading.value = false;
      infoList.value = (200 === result.code) ? result.data : [];
    }).catch(reason => {
      infoList.value = [];
      ElNotification({
        title: "提示",
        message: reason,
        duration: 0
      });
    });
}

/** 获取省份集合 */
async function getProvinces() {
  axios.get(`/api/oil-prices/provinces`)
    .then(value => {
      const result: any = value.data;

      if (200 === result.code) {
        provinceList.value = result.data;
        if (provinceList.value.length > 0) {
          showInfo(0);
        }
      }
    });
}

onMounted(() => { getProvinces();});


/** 显示指定省份油价信息 */
function showInfo(index: number) {
  provinceName.value = provinceList.value[index];
  getInfoList();
}
</script>

<style></style>