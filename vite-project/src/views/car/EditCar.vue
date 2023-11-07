<template>
  <!-- 编辑车辆 -->
  <div v-loading="loading" element-loading-text="编辑中...">
    <!-- 对话框 -->
    <el-dialog title="编辑车辆信息" v-model="show" draggable @close="closeDialog">
      <!-- 表单 -->
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

        <el-form-item label="创建时间">
          <el-input :value="form.createTime" disabled></el-input>
        </el-form-item>
      </el-form>

      <el-text class="el-text--info el-text--small ms-3">注意：油箱容积影响油耗和用车费用等计算</el-text>

      <template #footer>
        <span>
          <el-button color="#c3e88d" class="w-75 mx-auto d-block" @click="editCar(formRef)">保存</el-button>
        </span>
      </template>
    </el-dialog>

  </div>
</template>

<script setup lang="ts">
import { ElMessage, FormInstance, FormRules } from 'element-plus/lib/components/index.js';
import { ref, reactive, inject, onMounted } from 'vue';
import axios from 'axios';
import util from "../../utils/util";

interface IForm {
  carName: string;
  volume: number;
  createTime: string;
}

let car: any = null;
const queryCar = inject("queryCar") as Function;
const curTab = inject("curTab") as any;
const show = ref(true);
const loading = ref(true);
const formRef = ref<FormInstance>();
const form = reactive<IForm>({
  carName: "",
  volume: 0,
  createTime: ""
});

const rules = reactive<FormRules>({
  carName: [
    { required: true, message: "车辆名称不能为空", trigger: "blur" },
    { max: 20, message: "最多20个字符", trigger: "blur" }
  ],
  volume: [
    { required: true, message: "油箱容积不能为空", trigger: "blur" }
  ]
});

onMounted(() => {
  car = sessionStorage.getItem("car");
  if (car != null) {
    car = JSON.parse(car);
    form.carName = car.carName;
    form.volume = car.volume;
    form.createTime = util.formatDate(new Date(car.createTime));
  }
});

async function editCar(f: FormInstance | undefined) {
  if (!f) return;

  await f.validate((valid, _) => {
    if (valid) {
      loading.value = true;
      // 更新车辆信息
      axios.put(`/api/cars/update`, {
        id: car.id,
        carName: form.carName,
        volume: form.volume
        }, {
        headers: {
          Authorization: localStorage.getItem("token")
        }}).then(value => {
        const result: any = value.data;
        show.value = false;
        loading.value = false;

        // 更新成功
        if (200 === result.code) {
          ElMessage.success(result.info);
          queryCar();
        } else {
          ElMessage.error(result.info);
        }
        curTab.value = -1;
        sessionStorage.removeItem("car");
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