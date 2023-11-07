<template>
  <div>
    <!-- 用户协议 -->
    <el-dialog title="用户协议" v-model="show" draggable @close="closeDialog" class="col-12 col-md-7">
      <!-- 容器 -->
      <el-container>
        <!-- 头部 -->
        <el-header class="h-auto text-center">
          <h4>今日油价用户协议</h4>
        </el-header>

        <!-- 主体 -->
        <el-main>
          <p><strong>更新时间：</strong>xxxx年xx月xx日</p>
          <p><strong>生效时间：</strong>xxxx年xx月xx日</p>
          <el-scrollbar height="200">
            <p v-for="i in 10" :key="i">content...</p>
          </el-scrollbar>
        </el-main>
      </el-container>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, inject } from 'vue';

const show = ref(true);
const curTab = inject("curTab") as any;

function closeDialog() {
  show.value = false;
  curTab.value = -1;
}
</script>

<style></style>