<template>
  <!-- 主页容器 -->
  <el-container>
    <!-- 侧边栏 -->
    <el-aside class="w-auto">
      <!-- 滚动条 -->
      <el-scrollbar>
        <!-- 菜单 -->
        <el-menu default-active="/index" unique-opened router :collapse="isCollapse">
          <!-- 菜单伸缩按钮 -->
          <el-menu-item @click="isCollapse = !isCollapse">
            <el-icon><switch-filled /></el-icon>
            <template #title>切换面板</template>
          </el-menu-item>
          
          <!-- 子菜单 -->
          <el-sub-menu v-for="(submenus, index) in menus" :index="'' + (index + 1)" :key="submenus">
            <!-- 图标、标题 -->
            <template #title>
              <el-button :icon="submenus.icon" style="all: initial;"></el-button>
              <span>{{ submenus.title }}</span>
            </template>
            <!-- 菜单项 -->
            <el-menu-item v-for="(item, _) in submenus.children" :index="item.index"
              :key="item">
              {{ item.title }}
            </el-menu-item>
          </el-sub-menu>
        </el-menu>
      </el-scrollbar>
    </el-aside>

    <!-- 主体 -->
    <el-main class="p-2">
      <router-view></router-view>
    </el-main>
  </el-container>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue';
import { SwitchFilled, Search, Van, List, Management } from "@element-plus/icons-vue";
import router from '../../router/index';

// 菜单是否折叠
const isCollapse = ref(false);
// 菜单配置列表
const menus = [{
  title: "油价查询",
  icon: Search,
  children: [
    { title: "推荐", index: "/index" },
    { title: "89号汽油", index: "/show89" },
    { title: "92号汽油", index: "/show92" },
    { title: "95号汽油", index: "/show95" },
    { title: "98号汽油", index: "/show98" },
    { title: "0号汽柴油", index: "/show0" }
  ],
}, {
  title: "车辆管理",
  icon: Van,
  children: [
    {title: "我的车辆", index: "/myCar"}
  ]
}, {
  title: "加油管理",
  icon: List,
  children: [
    {title: "加油记录", index: "/addOil"}
  ]
}, {
  title: "费用管理",
  icon: Management,
  children: [
    {title: "费用记录", index: "/fare"}
  ]
}];

onMounted(() => {
  router.push("/index");
});
</script>

<style>
  
</style>