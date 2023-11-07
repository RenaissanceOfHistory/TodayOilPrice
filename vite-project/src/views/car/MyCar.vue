<template>
  <div>
    <!-- 容器 -->
    <el-container>
      <!-- 头部 -->
      <el-header class="h-auto">
        <!-- 标题 -->
        <h3 class="d-inline-block me-3 py-3">我的车辆</h3>
        <!-- 添加按钮 -->
        <el-tooltip content="添加车辆" placement="right">
          <el-button type="primary" size="small" class="mb-2" plain @click="curTab = 0">
            <el-icon>
              <Plus />
            </el-icon>
          </el-button>
        </el-tooltip>
      </el-header>

      <!-- 分隔线 -->
      <el-divider direction="horizontal" content-position="left" class="my-0"></el-divider>

      <!-- 主体 -->
      <el-main v-loading="loading" element-loading-text="加载中...">
        <!-- 滚动条 -->
        <el-scrollbar height="300" v-if="carInfoList.length > 0">
          <el-space wrap :size="10">
            <!-- 汽车列表 -->
            <template v-for="(item, _) in carInfoList" :key="item">
              <div class="car-container">
                <div class="car-header bg-success-subtle text-white text-center">
                  <h6 style="line-height: 2em;">{{ item.carName }}</h6>
                </div>
                <div class="car-body p-2 text-muted">
                  <el-text>油箱容量：{{ item.volume }}L</el-text><br />
                  <el-text>行驶里程：{{ item.mile || 0 }}KM</el-text>

                  <!-- 功能提示 -->
                  <div class="tip text-white">
                    <el-tooltip content="编辑" placement="top" effect="dark">
                      <el-icon class="me-3" @click="editCar(item)">
                        <Edit />
                      </el-icon>
                    </el-tooltip>

                    <el-popconfirm title="你确定要删除吗？" confirmButtonText="删除" cancelButtonText="取消"
                      confirmButtonType="primary" cancelButtonType="text" icon="el-icon-question" iconColor="#f90"
                      hideIcon="false" @confirm="deleteCar(item)">
                      <template #reference>
                        <el-icon>
                          <Delete />
                        </el-icon>
                      </template>
                    </el-popconfirm>
                  </div>
                </div>
              </div>
            </template>
          </el-space>
        </el-scrollbar>
        <template v-else>
          <el-empty description="无车辆"></el-empty>
        </template>
      </el-main>
    </el-container>

    <!-- 动态组件 -->
    <component :is='tabs[curTab]'></component>
  </div>
</template>

<script setup lang="ts">
import { Delete, Edit, Plus } from '@element-plus/icons-vue';
import { ref, onMounted, provide } from 'vue';
import AddCar from "./AddCar.vue";
import EditCar from './EditCar.vue';
import axios from 'axios';
import { ElMessage } from "element-plus/lib/components/index.js";

const tabs = [AddCar, EditCar];
const curTab = ref(-1);
const carInfoList = ref<any[]>([]);
const loading = ref(true);

provide("queryCar", queryCar);
provide("curTab", curTab);

onMounted(() => {queryCar();});

/** 查询车辆 */
async function queryCar() {
  let token = localStorage.getItem("token");
  // 已登录
  if (token != null) {
    // 请求车辆数据
    axios.get(`/api/cars/query/uid`, {
      headers: {
        Authorization: token
      }}).then(value => {
        const result: any = value.data;
        loading.value = false;
        if (200 === result.code) {
          carInfoList.value = result.data;
          carInfoList.value.forEach(value => {
            queryMaxMileByCid(value["id"]).then(val => {
              value["mile"] = val;
            });
          });
        } else {
          ElMessage.error(result.info);
        }
      }).catch(reason => {
        ElMessage.error(reason);
      });
  }
}

async function queryMaxMileByCid(cid:number) {
  let mile = 0;
  await axios.get(`/api/add-oils/query/mile/${cid}`, {
    headers: {
      Authorization: localStorage.getItem("token")
    }
  }).then(value => {
    const result: any = value.data;
    if (200 == result.code) {
      mile = result.data || 0;
    }
  });
  return mile;
}

/** 编辑车辆 */
function editCar(car: any) {
  // 切换到编辑组件
  curTab.value = 1;
  // 设置会话缓存
  sessionStorage.setItem("car", JSON.stringify(car));
}

/** 删除车辆 */
function deleteCar(car: any) {
  axios.delete(`/api/cars/remove/id/${car.id}`, {
    headers: {
      Authorization: localStorage.getItem("token")
    }
  }).then(value => {
      const result: any = value.data;
      if (200 === result.code) {
        ElMessage.success(result.info);
        setTimeout(() => {queryCar();}, 1000);
      } else {
        ElMessage.error(result.info);
      }
    });
}
</script>

<style>
.car-container {
  width: 200px;
  transition: .3s;
  cursor: pointer;
}

.car-body {
  position: relative;
}

/* 车辆功能提示 */
.car-container .tip {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  visibility: hidden;
  opacity: 0;
  font-size: 1.2em;
  text-align: center;
}

.car-container:hover .tip {
  visibility: visible;
  opacity: 1;
}

.car-container:hover,
.car-container:hover .car-header {
  background-color: rgba(0, 0, 0, 0.45) !important;
}
</style>