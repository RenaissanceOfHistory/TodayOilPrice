<template>
  <div>
    <!-- 意见反馈 -->
    <el-dialog title="意见反馈" v-model="show" draggable @close="closeDialog" class="col-12 col-md-5">
      <!-- 意见框 -->
      <el-input type="textarea" :rows="5" v-model.trim="textarea" placeholder="输入你的意见..." :maxlength="255" :show-word-limit="true"
      :autosize="{ minRows: 5, maxRows: 5 }">
      </el-input>

      <!-- 底部按钮 -->
      <template #footer>
        <span>
          <el-button type="primary" @click="onSubmit">提交</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, inject } from 'vue';

const show = ref(true);
const textarea = ref("");
const curTab = inject("curTab") as any;

/** 反馈提交 */
function onSubmit() {
  show.value = false;
  console.log(textarea.value);
}

function closeDialog() {
  curTab.value = -1;
  show.value = false;
}
</script>

<style></style>