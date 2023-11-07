<template>
  <!-- 添加车辆 -->
  <div v-loading="loading" element-loading-text="创建中...">
    <!-- 对话框 -->
    <el-dialog title="创建新车辆" v-model="show" class="col-12 col-md-6 col-lg-5" draggable @close="closeDialog">
      <!-- 内容表单 -->
      <el-form :model="form" ref="formRef" :rules="rules" label-width="80px" class="bg-light px-2 py-3" status-icon>
        <el-form-item label="汽车名称" prop="carName">
          <el-input v-model.trim="form.carName"></el-input>
        </el-form-item>

        <el-form-item label="油箱容积" prop="volume">
          <el-input v-model.number="form.volume" maxlength="3">
            <template #append>
              <span>L</span>
            </template>
          </el-input>
        </el-form-item>
      </el-form>

      <!-- 分隔线 -->
      <el-text class="el-text--info el-text--small ms-3">注意：油箱容积影响油耗和用车费用等计算</el-text>

      <!-- 按钮 -->
      <template #footer>
        <span>
          <el-button color="#c3e88d" class="w-75 mx-auto d-block" @click="addCar(formRef)">保存</el-button>
        </span>
      </template>
    </el-dialog>

  </div>
</template>

<script setup lang="ts">
import { ElMessage, FormInstance, FormRules } from 'element-plus/lib/components/index.js';
import { ref, reactive, inject } from 'vue';
import axios from 'axios';

// 表单接口
interface IForm {
  carName: string;
  volume: number;
}

// 注入引用
const queryCar = inject("queryCar") as Function;
const curTab = inject("curTab") as any;
// 显示对话框
const show = ref(true);
// 表单引用
const formRef = ref<FormInstance>();
// 加载状态
const loading = ref(false);
// 表单
const form = reactive<IForm>({
  carName: "",
  volume: 0
});

// 表单规则
const rules = reactive<FormRules>({
  carName: [
    { required: true, message: "车辆名称不能为空", trigger: "blur" },
    { max: 20, message: "最多20个字符", trigger: "blur" }
  ],
  volume: [
    { required: true, message: "油箱容积不能为空", trigger: "blur" }
  ]
});


/** 添加车辆 */
async function addCar(f: FormInstance | undefined) {
  if (!f) return;

  // 异步校验表单
  await f.validate((valid, _) => {
    if (valid) {
      // 设置加载状态
      loading.value = true;
      // 提交数据
      axios.post(`/api/cars/add`, form, {
        headers: {
          Authorization: localStorage.getItem("token")
        }
      }).then(value => {
        const result: any = value.data;
        // 请求成功时关闭对话框、加载状态
        show.value = false;
        loading.value = false;

        // 执行成功
        if (200 === result.code) {
          // 显示信息
          ElMessage.success(result.info);
          // 查询车辆
          queryCar();
        } else {
          ElMessage.error(result.info);
        }
        // 关闭组件
        curTab.value = -1;
      }).catch(reason => {
        ElMessage.error(reason);
      });
    } else {
      ElMessage({
        message: "表单校验失败",
        grouping: true,
        type: "error"
      });
    }
  });
}

function closeDialog() {
  curTab.value = -1;
  show.value = false;
}
</script>

<style></style>