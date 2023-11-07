<template>
  <div v-loading="loading" element-loading-text="登录中..." class="bg-light">
    <!-- 容器 -->
    <el-container>
      <!-- 主体 -->
      <el-main>
        <!-- 伸缩表单 -->
        <div class="row">
          <div class="col-12 col-md-6 col-lg-5 p-5 mt-5 mx-auto rounded bg-white">
            <!-- 表单 -->
            <el-form ref="formRef" :model="form" :rules="rules" label-width="80px" status-icon>
              <el-form-item label="账号" prop="username">
                <el-input v-model.trim="form.username" placeholder="输入账号" autofocus clearable :prefix-icon="User"></el-input>
              </el-form-item>

              <el-form-item label="密码" prop="password">
                <el-input v-model.trim="form.password" type="password" placeholder="输入密码" show-password
                  :prefix-icon="SetUp"></el-input>
              </el-form-item>

              <el-form-item label="验证码" prop="code">
                <el-input v-model.trim="form.code" clearable>
                <template #append>
                  <el-tooltip content="点击获取验证码" placement="top" effect="dark">
                      <el-image :src="codeUrl" fit="fill" style="width: 60px; height: 30px;" :lazy="true"
                        @click="getCode">
                        <template #error>
                          <div class="text-center" style="font-size: 1.2em;">
                            <el-tooltip content="验证码获取失败" placement="top" effect="dark">
                              <el-icon>
                                <IconPicture />
                              </el-icon>
                            </el-tooltip>
                          </div>
                        </template>
                      </el-image>
                    </el-tooltip>
                </template>
              </el-input>
              </el-form-item>

              <el-form-item>
                <el-button type="primary" @click="onSubmit(formRef)">登录</el-button>
                <el-button @click="onReset(formRef)">重置</el-button>
              </el-form-item>
            </el-form>
          </div>
        </div>
      </el-main>

      <!-- 提示 -->
      <el-footer class="text-center">
        <el-text>
          没有账号？
          <router-link to="/enroll" class="text-decoration-none">注册</router-link>
        </el-text>
      </el-footer>
    </el-container>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref, onMounted } from 'vue';
import { FormInstance, FormRules, ElMessage } from 'element-plus/lib/components/index';
import { User, SetUp, Picture as IconPicture } from "@element-plus/icons-vue";
import axios from 'axios';
import router from '../../router/index';

interface IForm {
  username: string;
  password: string;
  code: string;
}

const formRef = ref<FormInstance>();
const loading = ref(false);
const codeUrl = ref(`/api/users/code`);
const form = reactive<IForm>({
  username: "",
  password: "",
  code: ""
});

// 表单校验规则
const rules = reactive<FormRules>({
  username: [
    { required: true, message: "请输入账号", trigger: "blur" },
    { min: 3, max: 50, message: "账号长度在3~50之间", trigger: "blur" }], 
  password: [
    { required: true, message: "请输入密码", trigger: "blur" },
    { min: 6, max: 50, message: "密码长度在6~50之间", trigger: "blur" }], 
  code: [
    { required: true, message: "请输入验证码", trigger: "blur" }]
});

onMounted(() => { getCode();});

/** 登录 */
async function onSubmit(f: FormInstance | undefined) {
  if (!f) return;

  // 校验表单
  await f.validate((valid, _) => {
    if (valid) {
      loading.value = true;
      // 校验成功,提交登录数据
      axios.post(`/api/users/login/${form.code}`, form)
      .then(value => {
        loading.value = false;
        const result: any = value.data;

        // 登录成功
        if (200 === result.code) {
          localStorage.setItem("token", result.data);
          // 显示信息
          ElMessage.success(result.info);
          // 1秒后跳转到设置页面
          setTimeout(() => {
            router.replace("/settings");
          }, 1000);
        } else {
          ElMessage.error(result.info);
        }
      })
    } else {
      ElMessage.error(`表单校验错误`);
    }
  });
}

/** 重置表单 */
function onReset(f: FormInstance | undefined) {
  if (!f) return;
  f.resetFields();
}

/** 获取验证码 */
function getCode() {
  codeUrl.value += "?";
}
</script>

<style></style>