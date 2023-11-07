<template>
  <div>
    
  </div>
</template>

<script setup lang="ts">
import router from '../router';

// 刷新当前路由
router.beforeEach((_, from, __) => {
  router.replace(from.fullPath);
});
</script>

<style>
  
</style>