<template>
  <el-container>
    <!-- 头部导航 -->
    <el-header>
      <!-- 固定于顶部 -->
      <el-affix :offset="0">
        <!-- 头部菜单 -->
        <el-menu mode="horizontal" :default-active="activeIndex" active-text-color="#61d794">
          <!-- logo -->
          <el-menu-item index="1" disabled>
            <el-icon class="text-success">
              <Sunrise />
            </el-icon>
            <el-text class="text-success title">今日油价</el-text>
          </el-menu-item>

          <!-- 导航项 -->
          <el-menu-item v-for="item in items" :key="item.index" :index="item.index">
            <router-link :to="item.to" class="text-decoration-none">{{ item.text }}</router-link>
          </el-menu-item>

          <!-- 登录/注册导航 -->
          <el-menu-item index="6" class="ms-auto h-auto" style="cursor: pointer;">
            <el-dropdown>
              <el-button @click="toLogin" style="all: initial;">{{ loginText }}</el-button>
              <!-- 下拉提示 -->
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item :disabled="!isLogin" @click="logout">退出登录</el-dropdown-item>
                  <el-dropdown-item :disabled="!isLogin">
                    <el-popconfirm title="确定要注销吗?" confirmButtonText="注销" cancelButtonText="取消" confirmButtonType="primary"
                      cancelButtonType="text" icon="el-icon-question" iconColor="#f90" :hideIcon="false" @confirm="removeUser"
                      :disabled="!isLogin">
                      <template #reference>注销账户</template>
                    </el-popconfirm>
                  </el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </el-menu-item>
        </el-menu>
      </el-affix>
    </el-header>
  </el-container>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { Sunrise } from "@element-plus/icons-vue";
import router from '../router/index';
import axios from 'axios';
import { ElMessage, ElNotification } from 'element-plus/lib/components/index.js';


// 导航激活下标
const activeIndex = ref("2");
// 用户是否登录
const isLogin = ref(false);
// 登录文本（用户名/初始值）
const loginText = ref("登录/注册");
// 导航项
const items = [
  { text: "首页", index: "2", to: "/home" },
  { text: "记录", index: "3", to: "/record" },
  { text: "发现", index: "4", to: "/find" },
  { text: "设置", index: "5", to: "/settings" },
];

onMounted(() => {
  setLoginText();
});

/** 获取登录状态 */
function setLoginText(timeout = 0) {
  let token = localStorage.getItem("token");

  if (null == token) {
    console.log(`.......`);
    clearTimeout(timeout);
    timeout = setTimeout(() => {setLoginText(timeout)}, 1000);
    return;
  }
  
  // 根据Token获取用户信息
  axios.get(`/api/users/query/name/uid`, {
      headers: {
        Authorization: token
      }
    }).then(value => {
      const result: any = value.data;
      if (200 === result.code) {
        isLogin.value = true;
        loginText.value = result.data;
      }
  }).catch(_ => {
    localStorage.removeItem("token");
  });
}

/** 跳转到登录页 */
function toLogin() {
  router.push("/login");
}

/** 退出登录 */
function logout() {
  localStorage.removeItem("token");
  isLogin.value = false;
  loginText.value = "登录/注册";
   setTimeout(() => {
    router.replace("/home");
   }, 1000);
}

/** 删除用户 */
function removeUser() {
  let token = localStorage.getItem("token");
  if (token != null) {
    // 调用后端接口删除用户
    axios.delete(`/api/users/remove/uid`, {
      headers: {
        Authorization: token
      }
    }).then(value => {
        const result: any = value.data;

        // 判断状态码-成功
        if (200 === result.code) {
          // 退出登录
          logout();
          // 显示提示信息
          ElMessage.success(result.info);
          // 1秒后跳转到登录页
          setTimeout(() => {
            router.replace("/login");
          }, 1000);
        } else {
          // 失败，显示错误提示
          ElMessage.error(result.info);
        }
      }).catch(reason => {
        // 连接异常时显示错误通知
        ElNotification({
          title: "提示",
          message: reason,
          duration: 0
        });
      });
  }
}

</script>

<style scoped>
.title {
  font-size: 1.5em;
  font-family: 'Courier New', Courier, monospace;
  font-weight: bold;
}
</style>