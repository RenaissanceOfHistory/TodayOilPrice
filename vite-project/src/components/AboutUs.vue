<template>
  <div>
    <!-- 关于我们 -->
    <el-dialog title="关于我们" v-model="show" draggable @close="closeDialog" class="col-12 col-md-7">
      <!-- 容器 -->
      <el-container class="bg-light text-center">
        <!-- 主题 -->
        <el-main class="p-5">
          <div class="logo rounded-2 d-inline-block mt-3 mx-auto">
            今日<br /><span>油价</span>
          </div>
        </el-main>

        <!-- 尾部 -->
        <el-footer class="text-muted small">
          <el-text>@xxxx技术有限公司</el-text>
          <el-text class="small d-block">V2.5.xx</el-text>
        </el-footer>
      </el-container>

      <!-- 关闭按钮 -->
      <template #footer>
        <span>
          <el-button type="primary" @click="show = false">确认</el-button>
        </span>
      </template>
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

<style>
.logo {
  background-color: #fff;
  padding: 5px;
  font-size: 2em;
}

.logo span {
  color: #fff;
  background-color: #42b883;
  border-radius: 3px;
  padding: 0px 10px;
  text-align: center;
}
</style>