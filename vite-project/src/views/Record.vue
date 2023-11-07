<template>
  <!-- 容器 -->
  <div class="container-fluid p-3">
    <div class="row">
      <!-- 侧边栏 -->
      <div class="aside col-12 col-md-4">
        <div class="aside-header d-flex justify-content-between">
          <!-- 输入框 -->
          <el-autocomplete v-model="selectedCar" placeholder="输入车辆名称" :fetch-suggestions="querySearch" clearable
            @select="handleSelect">
            <template #suffix>
              <el-icon class="el-input__icon">
                <Edit />
              </el-icon>
            </template>
            <template #default="{ item }">
              <div>{{ item.carName }}</div>
            </template>
          </el-autocomplete>

          <!-- 新建记录 -->
          <el-dropdown>
            <el-button type="primary" :icon="CirclePlus">新建记录</el-button>
            <template #dropdown>
              <el-dropdown-menu >
                <el-dropdown-item @click="addRecord(0)" :disabled="'' == selectedCar">新建加油记录</el-dropdown-item>
                <el-dropdown-item @click="addRecord(1)" :disabled="'' == selectedCar">新建费用记录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>

        <!-- 分隔线 -->
        <el-divider direction="horizontal" content-position="left"></el-divider>

        <!-- 统计信息 -->
        <div class="aside-main">
          <div v-for="(item, index) in statistic" :key="index" 
            class="col-12 d-inline-block statistic"
            :class="{'col-md-6': 0 !== index}">
            <el-card shadow="never" class="bg-success-subtle border-0">
              <el-statistic :value="item.value" class="text-center" :precision="item.precision">
                <template #title>
                  <div>{{ item.title }}</div>
                </template>
                <template #suffix>
                  <span class="unit">{{ item.unit }}</span>
                </template>
              </el-statistic>
            </el-card>
          </div>
        </div>
      </div>

      <!-- 主体 -->
      <div class="main col-12 col-md-8">
        <!-- 提示图标 -->
        <div class="text-end">
          <el-icon style="font-size: 1.5em; cursor: pointer;" @click="curTab = 2"><QuestionFilled /></el-icon>
        </div>
        <!-- 图形化显示 -->
        <div ref="lineChartRef" class="mt-5" style="height: 400px;"></div>
        <div ref="barChartRef" class="mt-5" style="height: 400px;"></div>
      </div>
    </div>

    <component :is='tabs[curTab]'></component>
  </div>
</template>

<script setup lang="ts">
import { ElNotification } from 'element-plus/lib/components/index.js';
import { ElMessage } from "element-plus/lib/components/index.js";
import { ref, onMounted, provide } from 'vue';
import axios from 'axios';
import { Edit, CirclePlus, QuestionFilled } from "@element-plus/icons-vue";
import echarts from "echarts";
import AddRecord from './addOil/AddRecord.vue';
import AddFare from './fare/AddFare.vue';
import RecordTip from "../components/RecordTip.vue";
import util from "../utils/util";

// 车辆信息列表
const carInfoList = ref([]);
// 输入框内容
const selectedCar = ref("");
// 图表引用
const lineChartRef = ref("lineChartRef");
const barChartRef = ref("barChartRef");
// 组件列表
const tabs = [AddRecord, AddFare, RecordTip];
// 当前组件下标
const curTab = ref(-1);
// 统计信息数组
const statistic = ref([
  { title: "油费总计", value: 0, unit: "元", precision: 1},
  { title: "上次油耗", value: 0, unit: "L/百公里", precision: 1},
  { title: "百公里油耗", value: 0, unit: "L/百公里", precision: 1},
  { title: "表显里程", value: 0, unit: "KM", precision: 0 },
  { title: "统计里程", value: 0, unit: "KM", precision: 0 },
  { title: "累计加油", value: 0, unit: "L", precision: 1 },
  { title: "成本", value: 0, unit: "元/KM", precision: 2 }
]);

provide("curTab", curTab);
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
  queryRecordByCid(car.id);
  queryFareRecordByCid(car.id);
}

/** 折线图 */
function drawLineChart(xList: string[], seriesList: number[]) {
  const lineChart = echarts.init(lineChartRef.value);
  lineChart.showLoading();

  window.addEventListener("resize", function() {
    lineChart.resize();
  }, false);  

  const option = {
    title: {
      text: "油耗",
      subtext: "(单位：L/百公里)",
      subtextStyle: {
        color: "#999",
        fontSize: 10
      },
    },
    legend: {
      data: ["油耗"]
    },
    tooltip: {
      trigger: 'axis',
      axisPointer: { type: 'cross' }
    },
    xAxis: {
      type: 'category',
      axisTick: {
        alignWithLabel: true
      },
      data: ['A', 'B', 'C']
    },
    yAxis: {
      type: 'value',
    },
    series: [
      {
        data: [120, 200, 150],
        type: 'line',
        name: "油耗",
        yAxisIndex: 0,
      }
    ]
  };

  lineChart.hideLoading();
  option.xAxis.data = xList;
  option.series[0].data = seriesList;
  // 设置数据
  lineChart.setOption(option);
}

/** 柱状图 */
function drawBarChart(xList: string[], incomeList: number[], outputList: number[]) {
  const barChart = echarts.init(barChartRef.value);
  barChart.showLoading();

  // 监听窗口改变，调整图表大小
  window.addEventListener("resize", function() {
    barChart.resize();
  }, false);

  // 图表数据
  const option = {
    title: {
      text: "费用",
      subtext: "(单位：元)",
      subtextStyle: {
        color: "#999",
        fontSize: 10
      },
    },
    legend: {
      data: ["支出", "收入"]
    },
    tooltip: {},
    xAxis: {
      data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
    },
    yAxis: {
    },
    series: [
      {
        name: "支出",
        type: 'bar',
        itemStyle: {
          color: "#f56c6c"
        },
        barWidth: "20%",
        data: [23, 24, 18, 25, 27, 28, 25]
      },
      {
        name: "收入",
        type: 'bar',
        itemStyle: {
          color: "#b3e19d"
        },
        barWidth: "20%",
        data: [26, 24, 18, 22, 23, 20, 27]
      }
    ]
  };

  barChart.hideLoading();
  // 设置数据
  option.xAxis.data = xList;
  option.series[0].data = outputList;
  option.series[1].data = incomeList;
  barChart.setOption(option);
}

/** 查询指定车辆加油记录 */
async function queryRecordByCid(cid: string) {
  axios.get(`/api/add-oils/query/cid/${cid}`, {
    headers: {
      Authorization: localStorage.getItem("token")
    }
  }).then(value => {
      const result: any = value.data;
        if (200 === result.code) {
          // 如果存在数据
          if (result.data.length > 0) {
            let record = result.data[0],  // 上次油耗
                diffMile = 0,             // 统计里程
                lastRecord = record,      // 上一个加油记录对象
                seriesList: number[] = [],  // 图表数据
                xAxis: string[] = [];     // 图表x轴数据
            
            for (const item of result.data) {
              // 获取最新加油记录
              if (new Date(record.addTime).getTime() < new Date(item.addTime).getTime()) {
                record = item;
              }
              // 获取统计里程
              diffMile += Math.abs(lastRecord.mile - item.mile);
              lastRecord = item;
              // 油费总计
              statistic.value[0].value += item.balance;
              // 累计加油
              statistic.value[5].value += item.volume;
              seriesList.push(Number((item.volume / record.mile * 100).toFixed(2)) || 0)
              xAxis.push(util.formatDate(new Date(item.addTime)));
            }

            statistic.value[1].value = (record.volume / record.mile * 100) || 0;
            statistic.value[2].value = (record.volume / record.mile * 100) || 0;
            statistic.value[3].value = record.mile || 0;
            statistic.value[4].value = diffMile || 0;
            statistic.value[6].value = (statistic.value[2].value * record.balance / 100) || 0;
            drawLineChart(xAxis, seriesList);
          }
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

/** 查询指定车辆费用记录集合 */
async function queryFareRecordByCid(cid: string) {
  axios.get(`/api/fares/query/cid/${cid}`, {
    headers: {
      Authorization: localStorage.getItem("token")
    }
  }).then(value => {
      const result: any = value.data;
        if (200 === result.code) {
          let incomeList: number[] = [],
              outputList: number[] = [],
              xAxis: string[] = [],
              tmp: any[] = result.data;
          
          // 对列表进行排序
          tmp.sort((o1, o2) => new Date(o1.addTime).getTime() - new Date(o2.addTime).getTime());
          // 按天进行描述
          for (const item of tmp) {
            // 获取日期
            let date = util.formatDate(new Date(item.addTime));
            date = date.substring(0, date.indexOf(" "));
            
            // 不存在时添加进列表
            if (!xAxis.includes(date)) {
              xAxis.push(date);
              incomeList.push(item.inFare);
              outputList.push(item.outFare);
            } else {
              // 存在时自加
              const index = xAxis.indexOf(date);
              incomeList[index] += item.inFare;
              outputList[index] += item.outFare;
            }
          }
          drawBarChart(xAxis, incomeList, outputList);
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

function addRecord(index: number) {
  curTab.value = index;
  const cid = carInfoList.value.filter((val) => val["carName"] == selectedCar.value)[0]["id"];
  sessionStorage.setItem("cid", cid);
}
</script>

<style scoped>
.statistic {
  box-sizing: border-box;
  padding: 5px;
}

.unit {
  font-size: 0.5em !important;
  color: rgba(0, 0, 0, 0.45) !important;
}
</style>