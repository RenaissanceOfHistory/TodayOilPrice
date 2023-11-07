<template>
  <div>
    <!-- 主体 -->
    <el-dialog title="主题" v-model="showTheme" width="30%" draggable @close="closeDialog" class="col-12 col-sm-5 col-md-4">
      <el-radio-group v-model="themeRadio" class="d-flex justify-content-center">
        <el-radio label="1" border>白天</el-radio>
        <el-radio label="2" border>黑夜</el-radio>
        <el-radio label="3" border>系统</el-radio>
      </el-radio-group>

      <template #footer>
        <span>
          <el-button type="primary" @click="showTheme = false">确认</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, inject } from 'vue';
const themeRadio = ref("1");
const showTheme = ref(true);
const curTab = inject("curTab") as any;

function closeDialog() {
  showTheme.value = false;
  curTab.value = -1;
}
</script>

<style>
  
</style>