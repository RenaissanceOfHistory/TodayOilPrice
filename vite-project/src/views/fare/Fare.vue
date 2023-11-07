<template>
  <div v-loading="loading" element-loading-text="加载中...">
    <!-- 容器 -->
    <el-container>
      <!-- 头部 -->
      <el-header class="h-auto clearfix py-2">
        <!-- 标题 -->
        <h3 class="d-inline-block">费用记录</h3>
        <!-- 存在车辆数据时，显示输入框 -->
        <template v-if="carInfoList.length > 0">
          <el-autocomplete 
            v-model="selectedCar" 
            placeholder="输入车辆名称"
            :fetch-suggestions="querySearch"
            clearable
            class="col-12 col-md-3 float-md-end"
            @select="handleSelect">
            <template #suffix>
              <el-icon class="el-input__icon"><Edit /></el-icon>
            </template>
            <template #default="{item}">
              <div>{{ item.carName }}</div>
            </template>
          </el-autocomplete>
        </template>
        <!-- 否则，显示通知 -->
        <template v-else>
          <el-alert title="无车辆" type="info" effect="light" show-icon></el-alert>
        </template>
      </el-header>

      <!-- 分隔线 -->
      <el-divider direction="horizontal" content-position="left" class="my-0"></el-divider>

      <!-- 主体 -->
      <el-main>
        <!-- 存在内容时，显示内容 -->
        <template v-if="carInfoList.length > 0 && recordList.length > 0">
          <!-- 添加按钮 -->
          <el-tooltip content="添加记录" placement="top" effect="dark">
            <el-button @click="addRecord" :icon="Plus" plain></el-button>
          </el-tooltip>

          <!-- 内容列表 -->
          <ul class="list-group list-unstyled mt-3">
            <template v-for="(item, _) in recordList" :key="_">
              <li class="list-group-item text-muted">
                <div class="d-flex align-items-center justify-content-between">
                  <p>
                    <!-- 判断支出还是收入类型 -->
                    <template v-if="'0' === item.fareType">
                      <span>{{ getLabel(options1, item.outType) }}</span>
                      <span class="d-inline-block ms-3">-{{ (item.outFare).toFixed(1) }}</span>
                    </template>
                    <template v-else>
                      <span>{{ getLabel(options2, item.inType) }}</span>
                      <span class="d-inline-block ms-3">+{{ (item.inFare).toFixed(1) }}</span>
                    </template>
                  </p>
                  <span class="small text-muted">{{ formatDate(new Date(item.addTime)) }}</span>
                </div>
                <div class="small text-muted" style="min-height: 25px;">
                  <el-text class="w-50">{{ item.info }}</el-text>
                </div>

                <!-- 功能提示 -->
                <div class="tip">
                  <el-icon class="me-3" @click="editRecord(item)"><Edit /></el-icon>
                  <el-popconfirm
                  title="确定删除吗?"
                  confirmButtonText="删除"
                  cancelButtonText="取消"
                  confirmButtonType="primary"
                  cancelButtonType="text"
                  icon="el-icon-question"
                  iconColor="#f90"
                  :hideIcon="false"
                  @confirm="deleteRecord(item)">
                  <template #reference>
                    <el-icon><Delete /></el-icon>
                  </template>
                  </el-popconfirm>
                </div>
              </li>
            </template>
          </ul>
        </template>
        <!-- 否则，显示空内容 -->
        <template v-else>
          <el-empty description="无费用记录">
            <el-button type="primary" plain @click="addRecord">添加记录</el-button>
          </el-empty>
        </template>
      </el-main>
    </el-container>

    <!-- 动态组件 -->
    <component :is='tabs[curTab]'></component>
  </div>
</template>

<script setup lang="ts">
import { ElNotification } from 'element-plus/lib/components/index.js';
import { ElMessage } from "element-plus/lib/components/index.js";
import { ref, onMounted, provide } from 'vue';
import axios from 'axios';
import { Delete, Edit, Plus } from "@element-plus/icons-vue";
import util from "../../utils/util";
import AddFare from './AddFare.vue';
import EditFare from './EditFare.vue';

// 车辆列表
const carInfoList = ref([]);
// 选择车辆
const selectedCar = ref("");
// 记录列表
const recordList = ref<any[]>([]);
// 组件列表
const tabs = [AddFare, EditFare];
// 当前组件下标
const curTab = ref(-1);
// 车辆id
let cid: any = "";
const loading = ref(false);

// 支出类型选项
const options1 = [
  {label: "行车费", value: 0},
  {label: "维修保养", value: 1},
  {label: "保险", value: 2},
  {label: "交通规费", value: 3},
  {label: "其它", value: 4}
];

// 收入类型选项
const options2 = [
  {label: "载客", value: 0},
  {label: "运货", value: 1},
  {label: "其它", value: 2}
];

// 提供依赖
provide("curTab", curTab);
provide("queryRecordByCid", queryRecordByCid);
provide("getLabel", getLabel);

onMounted(() => {queryCars();});


function getLabel(option:any[], type:number) {
  return option.filter((val) => val.value == type)[0].label || "";
}


/** 查询车辆 */
async function queryCars() {
  let token = localStorage.getItem("token");
  // 已登录
  if (token != null) {
    // 请求车辆数据
    axios.get(`/api/cars/query/uid`, {
      headers: {
        Authorization: token
      }
    }).then(value => {
        const result: any = value.data;
        if (200 === result.code) carInfoList.value = result.data;
        else ElMessage.error(result.info);
      }).catch(reason => {
          ElNotification({
            title: "提示",
            message: reason,
            duration: 0
          });
      });
  } else {
    ElNotification({
      title: "提示",
      message: "用户未登录",
      duration: 0
    });
  }
}

/** 查询指定车辆费用记录集合 */
async function queryRecordByCid(cid: string) {
  axios.get(`/api/fares/query/cid/${cid}`, {
    headers: {
      Authorization: localStorage.getItem("token")
    }
  }).then(value => {
      const result: any = value.data;
      loading.value = false;
        if (200 === result.code) {
          recordList.value = result.data;
        } else {
          ElMessage.error(result.info);
        }
    }).catch(reason => {
      ElNotification({
        title: "提示",
        message: reason,
        duration: 0
      });
    });
}

/** 输入时过滤 */
function querySearch(query: string, cb: any) {
  const results = query
    ? carInfoList.value.filter(createFilter(query))
    : carInfoList.value;
  cb(results);
}

/** 过滤器 */
function createFilter(query: string) {
  return (car: any) => {
    return (
      car.carName.toLowerCase().indexOf(query.toLowerCase()) === 0
    )
  }
}

/** 处理选择车辆 */
function handleSelect(car: any) {
  selectedCar.value = car.carName;
  cid = car.id;
  loading.value = true;
  queryRecordByCid(car.id);
}

/** 添加记录 */
function addRecord() {
  if (cid !== "") {
    sessionStorage.removeItem("record");
    sessionStorage.setItem("cid", cid);
    curTab.value = 0;
  }
}

/** 格式化日期 */
function formatDate(date: Date) {
  const y = date.getFullYear(),
        m = util.format(date.getMonth() + 1),
        d = util.format(date.getDate());
  return `${m}月${d}-${y}年`;
}

/** 删除记录 */
async function deleteRecord(record: any) {
  axios.delete(`/api/fares/remove/id/${record.id}/${cid}`, {
    headers: {
      Authorization: localStorage.getItem("token")
    }
  }).then(value => {
      const result: any = value.data;
          
      if (200 === result.code) {
        queryRecordByCid(cid);
        ElMessage.success(result.info);
      } else {
        ElMessage.error(result.info);
      }
    }).catch(reason => {
      ElNotification({
        title: "提示",
        message: reason,
        duration: 0
      });
    });
}

/** 编辑记录 */
function editRecord(record: any) {
  if (cid !== "") {
    sessionStorage.setItem("cid", cid);
    sessionStorage.setItem("record", JSON.stringify(record));
    curTab.value = 1;
  }
}
</script>

<style>
.list-group-item {
  position: relative;
}

.list-group-item:hover {
  background-color: rgba(0, 0, 0, 0.45);
}

/* 功能提示 */
.list-group-item .tip {
  position: absolute;
  color: #fff;
  z-index: 255;
  top: 50%;
  left: 50%;
  font-size: 1.5em;
  transform: translate(-50%, -50%);
  transition: .3s;
  visibility: hidden;
  opacity: 0;
}

.list-group-item:hover .tip {
  visibility: visible;
  opacity: 1;
}
</style>