<template>
  <div>
    <!-- 容器 -->
    <el-container class="mx-2">
      <!-- 头部 -->
      <el-header class="h-auto p-5 bg-success-subtle rounded">
        <div class="d-flex align-items-center justify-content-start">
          <!-- 用户图像 -->
          <el-tooltip :content="isLogin ? '点击选择头像' : '未登录'" placement="top" effect="dark">
            <el-upload ref="upload" action="" :auto-upload="false" :limit="1" :disabled="!isLogin"
              :on-change="uploadAvatar" :show-file-list="false" accept="image/jpeg,image/png">
              <el-avatar icon="el-icon-user-solid" class="me-3" :size="100" :src="avatar" @error="() => true">
                <img :src="avatar" alt="" title="" />
              </el-avatar>
            </el-upload>
          </el-tooltip>

          <el-text class="text-muted mt-5">
            <!-- 用户已登录,显示用户名 -->
            <span v-if="isLogin">{{ username }}</span>
            <!-- 否则,添加登录路由 -->
            <router-link to="/login" class="text-decoration-none text-success user-select-none" v-else>
              登录<el-icon class="el-icon--right">
                <Edit />
              </el-icon>
            </router-link>
          </el-text>
        </div>
      </el-header>

      <!-- 主体 -->
      <el-main>
        <!-- 功能模块 -->
        <div class="clearfix float-container row">
          <template v-for="(item, _) in modules" :key="item">
            <div class="module p-1 text-muted col-6 col-sm-3 col-md-2" @click="item.func">
              <div class="module-body p-2 border rounded d-flex justify-content-center flex-column align-items-center">
                <el-button :icon="item.icon" style="all: initial;"></el-button>
                <el-text>{{ item.name }}</el-text>
              </div>
            </div>
          </template>
        </div>
      </el-main>
    </el-container>

    <!-- 动态组件 -->
    <component :is='tabs[curTab]' v-show="curTab >= 0"></component>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, provide } from 'vue';
import { Comment as IconComment, Edit, SetUp, Aim, List, MoreFilled } from '@element-plus/icons-vue';
import Theme from '../components/Theme.vue';
import Feedback from '../components/Feedback.vue';
import AboutUs from '../components/AboutUs.vue';
import Service from '../components/Service.vue';
import axios from 'axios';
import { ElMessage } from 'element-plus/lib/components/index.js';


// 是否已登录
const isLogin = ref(false);
// 图像
const avatar = ref("");
// 组件列表
const tabs = [Theme, Feedback, AboutUs, Service];
// 当前组件下标
const curTab = ref(-1);
const username = ref("");
const upload = ref();

// 功能模块配置
const modules = [
  { name: "主题", icon: SetUp, func: () => {curTab.value = 0} },
  { name: "意见反馈", icon: IconComment, func: () => {curTab.value = 1} },
  { name: "关于我们", icon: Aim, func: () => {curTab.value = 2} },
  { name: "服务协议", icon: List, func: () => {curTab.value = 3} },
  { name: "隐私政策", icon: MoreFilled, func: () => {curTab.value = 3} },
];

provide("curTab", curTab);
onMounted(() => {
  queryUsername();
});


async function queryUsername() {
  const token = localStorage.getItem("token");
  if (token != null) {
    axios.get(`/api/users/query/name/uid`, {
      headers: {
        Authorization: token
      }
    }).then(value => {
      const result: any = value.data;
      if (200 === result.code) {
        isLogin.value = true;
        username.value = result.data;
      }
    });

    downloadAvatar();
  }
}


async function uploadAvatar(file: MediaImage) {
  if (Number(file["raw"].sizes) > 10 * 1024 * 1024) {
    ElMessage.alert("文件大小超过10MB");
    file["raw"] = null;
    return;
  }

  // 将选择图片转换为base64编码
  const reader = new FileReader();
  reader.readAsDataURL(file["raw"]);
  reader.onload = e => {
    const code = e.target?.result;
    upload.value.clearFiles();
    avatar.value = code as string;
    sessionStorage.setItem("avatar", avatar.value);
    axios.post(`/api/users/image/upload`, {
      "avatar": code
    }, {
      headers: {
        Authorization: localStorage.getItem("token")
      }
    });
  }
}

async function downloadAvatar() {
  await axios.get("/api/users/image/download", {
    headers: {
      Authorization: localStorage.getItem("token")
    },
    responseType: "blob",
  }).then((value) => {
    avatar.value = URL.createObjectURL(new Blob([value.data]));
    sessionStorage.setItem("avatar", avatar.value);
  })
}

</script>

<style>
.float-container {
  box-sizing: border-box;
}

.module {
  box-sizing: border-box;
  transition: .3s;
  float: left;
}

.module-body:hover {
  transform: scale(1.01);
  box-shadow: 0 0 5px 5px #eee;
}

</style>
