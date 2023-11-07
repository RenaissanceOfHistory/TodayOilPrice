<template>
  <div>
    <el-container class="p-2">
      <!-- 标题 -->
      <el-header class="text-center">
        <h2>国际油价</h2>
      </el-header>

      <!-- 内容 -->
      <el-main>
        <h3>油价预测</h3>
        <p>国内成品油调整今年“第14轮”，第5个工作日。由于北半球夏季石油需求相对旺盛、美国开始采购补充战略
          原油储备等利好因素支撑，一些市场人士看好国际油价未来趋势。
        </p>
        <p>7月5日，原有变化率暂计为0.82%，预计汽柴油上调35元/吨，没超过上调红线（50元/吨），暂时处于搁浅
          区间。调价日期为：7月12日24时。
        </p>
        <p>国际油价7月4日，集体反弹收涨。截至收盘，美油8月合约涨1.73%报71美元/桶，布油9月合约涨1.97%报
          76.12美元/桶。
        </p>
      </el-main>

      <!-- 信息列表 -->
      <el-footer>
        <div class="row">
          <div class="col-12 col-md-6 mb-2 module" v-for="(item, index) in oilInfo" :key="index">
            <el-card shadow="hover" @click="showDetail(index)">
              <div class="d-flex align-items-center justify-content-between">
                <img :src="item.img" :alt="item.title" :title="item.title" class="img">
                <div class="h-100" style="width: 80%;">
                  <h6>{{ truncateText(item.title, 20) }}</h6>
                  <p class="small text-muted text-end mb-0 mt-3">{{ item.date }}</p>
                </div>
              </div>
            </el-card>
          </div>
        </div>
      </el-footer>
    </el-container>

    <!-- 详情对话框 -->
    <el-dialog title="详情" v-model="isDetailVisible" draggable center class="col-12 col-md-6">
      <div>
        <h6>{{ oilInfo[detailIndex].title }}</h6>
        <p class="text-secondary small">{{ oilInfo[detailIndex].date }}</p>
        <el-scrollbar height="150">
          <p class="text-muted">{{ oilInfo[detailIndex].detail }}</p>
        </el-scrollbar>
      </div>
      <template #footer>
        <span>
          <el-button type="primary" @click="isDetailVisible = false">确认</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';

const isDetailVisible = ref(false);
const detailIndex = ref(-1);
const oilInfo = [
  { 
    img: "/static/img/date.png", 
    title: "2023年6月28日24时起国内成品油价格按机制上调", 
    date: "2023-06-29",
    detail: `国家发展改革委6月28日发布消息，根据近期国际市场油价变化情况，按照现行成品油价形成机制，自2023年
              6月28日24时起，国内汽油、柴油价格每吨均提高70元。6月29日，本轮调控周期内，原油变化率暂计为1.55%，
              预计汽柴油上调70元/吨，折合为上调0.06-0.07元/升。调价窗口今晚24时。具体来看：92号汽油预测平均上涨
              0.06元/升，95号汽油...`
  },
];

function truncateText(text: string, length: number): string {
  return text.length > length ? text.substring(0, length) + "..." : text;
}

function showDetail(index: number) {
  detailIndex.value = index;
  isDetailVisible.value = true;
}
</script>

<style scoped>
.module {
  user-select: none;
  -moz-user-select: none;
  -webkit-user-select: none;
}
.img {
  width: 100px;
  height: 80px;
}
</style>