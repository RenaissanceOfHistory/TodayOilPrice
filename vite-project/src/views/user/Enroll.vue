<template>
  <div v-loading="loading" element-loading-text="注册中..." class="bg-light">
    <!-- 容器 -->
    <el-container>
      <!-- 主体 -->
      <el-main>
        <!-- 伸缩表单 -->
        <div class="row">
          <div class="col-12 col-md-6 col-lg-5 p-5 mt-5 mx-auto bg-white rounded">
            <!-- 注册表单 -->
            <el-form ref="formRef" :model="form" :rules="rules" label-width="80px" status-icon>
              <el-form-item label="账号" prop="username">
                <el-input v-model.trim="form.username" placeholder="输入账号" autofocus clearable :prefix-icon="User"></el-input>
              </el-form-item>

              <el-form-item label="密码" prop="password">
                <el-input v-model.trim="form.password" type="password" placeholder="输入密码" show-password
                  :prefix-icon="SetUp"></el-input>
              </el-form-item>

              <el-form-item label="电话" prop="phone">
                <el-input v-model.trim="form.phone" placeholder="输入电话号码" clearable :prefix-icon="Phone"></el-input>
              </el-form-item>

              <el-form-item label="验证码" prop="code">
                <el-input v-model.trim="form.code" clearable>
                  <template #append>
                    <!-- 验证码悬停时提示 -->
                    <el-tooltip content="点击获取验证码" placement="top" effect="dark">
                      <!-- 验证码图片 -->
                      <el-image :src="codeUrl" fit="fill" style="width: 60px; height: 30px;" :lazy="true"
                        @click="getCode">
                        <!-- 图片加载错误时显示内容 -->
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
                <el-button type="primary" @click="onSubmit(formRef)">注册</el-button>
                <el-button @click="onReset(formRef)">重置</el-button>
              </el-form-item>
            </el-form>
          </div>
        </div>
      </el-main>

      <el-footer class="text-center">
        <el-text>
          已有账号？
          <router-link to="/login" class="text-decoration-none">登录</router-link>
        </el-text>
      </el-footer>
    </el-container>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref, onMounted } from 'vue';
import { FormInstance, FormRules, ElMessage } from 'element-plus/lib/components/index';
import { User, SetUp, Phone, Picture as IconPicture } from "@element-plus/icons-vue";
import axios from 'axios';
import router from '../../router/index';

interface IForm {
  username: string;
  phone: string;
  password: string;
  code: string;
}

const loading = ref(false);
const formRef = ref<FormInstance>();
// 验证码请求路径
const codeUrl = ref(`/api/users/code`);
// 表单
const form = reactive<IForm>({
  username: "",
  phone: "",
  password: "",
  code: ""
});

/** 自定义电话校验 */
function validatePhone(_: any, value: any, callback: any) {
  if ("" === value) {
    callback(new Error("电话号码不能为空"));
  } else if (!/^[1][3,4,5,6,7,8,9][0-9]{9}$/.test(value)) {
    callback(new Error("电话号码格式错误"));
  } else {
    callback();
  }
}

// 表单校验规则
const rules = reactive<FormRules>({
  username: [
    {required: true, message: "请输入账号", trigger: "blur"},
    {min: 3, max: 50, message: "账号长度在3~50之间", trigger: "blur"}
  ],
  phone: [
    {validator: validatePhone, trigger: "blur"},
    {required: true, trigger: "blur"}
  ],
  password: [
    {required: true, message: "请输入密码", trigger: "blur"},
    {min: 6, max: 50, message: "密码长度在6~50之间", trigger: "blur"}
  ],
  code: [
    {required: true, message: "请输入验证码", trigger: "blur"}
  ]
});

onMounted(() => {
  getCode();
});

/** 注册 */
async function onSubmit(f: FormInstance | undefined) {
  if (!f) return;

  /** 表单校验 */
  await f.validate((valid, _) => {
    // 校验成功,注册用户
    if (valid) {
      loading.value = true;
      axios.post(`/api/users/enroll/${form.code}`, {
        username: form.username,
        password: form.password,
        phone: form.phone
      }).then(value => {
        loading.value = false;
        const result: any = value.data;

        // 注册成功,显示信息
        if (200 === result.code) {
          ElMessage.success(result.info);
          // 在2秒后,跳转到登录页
          setTimeout(() => {
            router.replace("/login");
          }, 2000);
        } else {
          ElMessage.error(result.info);
        }
      })
    } else {
      ElMessage.error("表单校验失败");
    }
  });
}

/** 重置表单 */
function onReset(f: FormInstance | undefined) {
  if (!f) return;
  f.resetFields();
  ElMessage.success("重置成功");
}

/** 获取验证码 */
function getCode() {
  codeUrl.value += "?";
}
</script>

<style></style>