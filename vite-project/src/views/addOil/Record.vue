<template>
  <div>
    <!-- 容器 -->
    <el-container>
      <!-- 头部 -->
      <el-header class="h-auto clearfix py-2">
        <!-- 标题 -->
        <h3 class="d-inline-block">加油记录</h3>
        <!-- 如果存在车辆，显示输入框 -->
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
        <!-- 如果存在内容，显示内容 -->
        <template v-if="carInfoList.length > 0 && recordList.length > 0">
          <!-- 添加记录按钮 -->
          <el-tooltip content="添加记录" placement="top" effect="dark">
            <el-button @click="addRecord" :icon="Plus" plain></el-button>
          </el-tooltip>

          <!-- 内容列表 -->
          <ul class="list-group list-unstyled mt-3">
            <template v-for="(item, _) in recordList" :key="_">
              <li class="list-group-item text-muted">
                <div class="d-flex align-items-center justify-content-between">
                  <span>{{ formatDate(new Date(item.addTime)) }}</span>
                  <span>记录里程：<span class="text-dark">{{ item.mile }}</span> km</span>
                </div>
                <div class="d-flex align-items-center justify-content-between mt-2">
                  <span><span class="text-dark">{{ item.balance }}</span>元</span>
                  <span><span class="text-dark">{{ item.note }}</span>{{ '0#' == item.note ? '柴油' : '汽油' }}</span>
                  <span><span class="text-dark">+{{ item.volume }}</span>L</span>
                  <span><span class="text-dark">{{ item.unitPrice }}</span>元/L</span>
                </div>
                <!-- 悬停时功能提示 -->
                <div class="tip">
                  <!-- 编辑 -->
                  <el-icon class="me-3" @click="editRecord(item)"><Edit /></el-icon>
                  <!-- 删除 -->
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
        <!-- 不存在内容时，显示空状态 -->
        <template v-else>
          <el-empty description="无加油记录">
            <el-button type="primary" plain @click="addRecord">添加记录</el-button>
          </el-empty>
        </template>
      </el-main>
    </el-container>

    <!-- 组件切换 -->
    <component :is='tabs[curTab]'></component>
  </div>
</template>

<script setup lang="ts">
import { ElNotification } from 'element-plus/lib/components/index.js';
import { ElMessage } from "element-plus/lib/components/index.js";
import { ref, onMounted, provide } from 'vue';
import axios from 'axios';
import { Delete, Edit, Plus } from "@element-plus/icons-vue";
import AddRecord from './AddRecord.vue';
import util from "../../utils/util";
import EditRecord from './EditRecord.vue';

// 车辆信息列表
const carInfoList = ref([]);
// 输入框内容
const selectedCar = ref("");
// 记录列表
const recordList = ref<any[]>([]);
// 组件列表
const tabs = [AddRecord, EditRecord];
// 当前组件下标
const curTab = ref(-1);
// 车辆id
let cid: any = "";

// 提供依赖
provide("curTab", curTab);
provide("queryRecordByCid", queryRecordByCid);
// 页面挂载时查询车辆
onMounted(() => {
  queryCars();
});

/** 查询用户所属车辆 */
async function queryCars() {
  let token = localStorage.getItem("token");
  // 存在用户时，转化为对象
  if (token != null) {
    // 请求车辆
    axios.get(`/api/cars/query/uid`, {
      headers: {
        Authorization: token
      }
    }).then(value => {
        const result: any = value.data;
        // 查询成功，设置车辆信息
        if (200 === result.code) {
          carInfoList.value = result.data;
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
  } else {
    ElNotification({
      title: "提示",
      message: "用户未登录",
      duration: 0
    });
  }
}

/** 查询指定车辆加油记录 */
async function queryRecordByCid(cid: string) {
  axios.get(`/api/add-oils/query/cid/${cid}`, {
    headers: {
      Authorization: localStorage.getItem("token")
    }
  }).then(value => {
      const result: any = value.data;
        if (200 === result.code) recordList.value = result.data;
        else ElMessage.error(result.info);
    }).catch(reason => {
      ElNotification({
        title: "提示",
        message: reason,
        duration: 0
      });
    });
}

/** 输入框输入过滤 */
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
  // 设置值
  selectedCar.value = car.carName;
  cid = car.id;
  // 查询车辆加油记录
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
  axios.delete(`/api/add-oils/remove/id/${record.id}/${cid}`, {
    headers: {
      Authorization: localStorage.getItem("token")
    }
  }).then(value => {
      const result: any = value.data;
          
      // 删除成功
      if (200 === result.code) {
        // 查询新数据
        queryRecordByCid(cid);
        // 显示信息
        ElMessage.success(result.info);
      } else {
        ElMessage.error(result.info);
      }
    }).catch(reason => {
      // 显示异常信息
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

<style scoped>
/* 记录列表 */
.list-group-item {
  position: relative;
}

/* 记录列表悬停时样式 */
.list-group-item:hover {
  background-color: rgba(0, 0, 0, 0.45);
}

/* 列表项功能提示 */
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

/* 列表项悬停时提示 */
.list-group-item:hover .tip {
  visibility: visible;
  opacity: 1;
}
</style>