<template>
  <!-- 显示油价信息 -->
  <el-container>
    <!-- 头部 -->
    <el-header class="h-auto">
      <!-- 油价标题 -->
      <el-row class="p-3">
        <el-col class="el-col-24 el-col-md-12 mt-2">
          <el-text class="h3">今日{{ info }}号{{ '0' == info ? '柴油' : '汽油' }}价格查询</el-text>
        </el-col>
        <!-- 查询输入框 -->
        <el-col class="el-col-24 el-col-offset-0 el-col-md-offset-6 el-col-md-6 mt-2">
          <el-input v-model="search" placeholder="输入查询" maxlength="50" clearable :prefix-icon="Search"></el-input>
        </el-col>
      </el-row>
    </el-header>

    <!-- 主体 -->
    <el-main>
      <!-- 表格数据 -->
      <el-table :data="filterData" border height="300" :default-sort="{ prop: 'area', order: 'descending' }"
        v-loading="loading">
        <el-table-column label="地区" sortable prop="area"></el-table-column>
        <el-table-column label="价格（元）" sortable prop="price"></el-table-column>
      </el-table>

      <!-- 图形化显示 -->
      <div ref="chartRef" class="mt-5" style="width: 800px; height: 400px;"></div>
    </el-main>
  </el-container>
</template>

<script setup lang="ts">
import echarts from "echarts";
import { onMounted, ref, computed } from 'vue';
import axios from 'axios';
import { Search } from "@element-plus/icons-vue";

// 接收父组件属性
const props = defineProps<{ info: string }>();
// 图表引用
const chartRef = ref("chartRef");
// 油价信息列表
const infoList = ref<Object[]>([]);
// 查询关键字
const search = ref("");
// 是否显示加载状态
const loading = ref(true);
// 计算属性根据地区过滤列表
const filterData = computed(() =>
  infoList.value.filter(data =>
    !search.value ||
    (data as any).area.toLowerCase().includes(search.value.toLowerCase())
  )
);

/** echart趋势图 */
function drawChart() {
  // 初始化对象
  let chart = echarts.init(chartRef.value);
  // 显示加载状态
  chart.showLoading();

  // 初始设置
  let option = {
    title: {
      text: "油价趋势表"
    },
    tooltip: {},
    legend: {
      data: ["地区"]
    },
    xAxis: {
      data: ['A', 'B', 'C', 'D', 'E']
    },
    yAxis: {},
    series: [
      {
        data: [10, 22, 28, 43, 49],
        type: 'line',
        stack: 'x',
        smooth: true
      },
    ]
  };

  // 获取数据
  if (infoList.value.length > 0) {
    // 区域列表、价格列表
    const areaList: string[] = [],
      priceList: number[] = [];

    // 遍历数组，添加数据进列表
    for (const item of infoList.value) {
      const tmp: any = item;
      areaList.push(tmp.area.substring(0, tmp.area.indexOf(props.info)));
      priceList.push(Number(tmp.price) || 0);
    }

    // 更新图标数据
    option.xAxis.data = areaList;
    option.series[0].data = priceList;
    // 隐藏加载状态
    chart.hideLoading();
    // 设置数据
    chart.setOption(option);
  }
}

// 挂载时查询油价信息
onMounted(() => {
  queryByLabel();
});

/** 根据标号查询油价 */
function queryByLabel() {
  axios.get(`/api/oil-prices/label/${props.info}`)
    .then(value => {
      const result: any = value.data;
      loading.value = false;

      if (200 === result.code) {
        infoList.value = result.data;
        drawChart();
      } else {
        infoList.value = [];
      }
    });
}
</script>

<style>
.card {
  width: 25%;
  box-sizing: border-box;
}
</style>