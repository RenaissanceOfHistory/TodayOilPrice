<template>
  <div>
    <!-- 费用添加 -->
    <el-dialog title="新建费用" v-model="show" draggable @close="closeDialog">
      <!-- 表单 -->
      <el-form :model="form" ref="formRef" :rules="rules" label-width="80px">
        <el-form-item label="费用类型">
          <el-radio-group v-model="form.fareType">
            <el-radio label="0">支出</el-radio>
            <el-radio label="1">收入</el-radio>
          </el-radio-group>
        </el-form-item>
        
        <el-form-item label="日期时间" prop="addTime">
          <el-date-picker v-model="form.addTime" placeholder="选择日期时间" type="date"></el-date-picker>
        </el-form-item>

        <!-- 支出类型 -->
        <template v-if="'0' == form.fareType">
          <el-form-item label="支出类型">
            <el-select v-model="form.outType" clearable filterable>
            <el-option v-for="item in options1"
              :key="item.value"
              :label="item.label"
              :value="item.label">
            </el-option>
            </el-select>
          </el-form-item>
          
          <el-form-item label="支出" prop="outFare">
            <el-input v-model="form.outFare" clearable>
              <template #append>
                <span>元</span>
              </template>
            </el-input>
          </el-form-item>
        </template>

        <!-- 收入类型 -->
        <template v-else>
          <el-form-item label="收入类型">
            <el-select v-model="form.inType" clearable filterable>
            <el-option v-for="item in options2"
              :key="item.value"
              :label="item.label"
              :value="item.label">
            </el-option>
            </el-select>
          </el-form-item>
          
          <el-form-item label="收入" prop="inFare">
            <el-input v-model="form.inFare" clearable>
              <template #append>
                <span>元</span>
              </template>
            </el-input>
          </el-form-item>
        </template>
        
        <!-- 分隔线 -->
        <el-divider direction="horizontal" content-position="left">选填</el-divider>

        <el-form-item label="备注">
          <el-input type="textarea" :rows="2" v-model="form.info" placeholder="" :show-word-limit="true"
            :autosize="{ minRows: 5, maxRows: 6 }">
          </el-input>
        </el-form-item>
      </el-form>

      <template #footer>
        <span>
          <el-button color="#c3e88d" class="w-75 mx-auto d-block" @click="handleSubmit(formRef)">保存</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ElMessage, FormInstance, FormRules, ElNotification } from 'element-plus/lib/components/index.js';
import { ref, reactive, inject, watch } from 'vue';
import axios from 'axios';
import util from '../../utils/util';


// 表单接口
interface IForm {
  id: string;
  cid: string;
  fareType: string;
  addTime: string;
  outType: string;
  outFare: number;
  inType: string;
  inFare: number;
  info: string;
}

// 支出类型选项
const options1 = [
  {label: "行车费", value: 0},
  {label: "维修保养", value: 1},
  {label: "保险", value: 2},
  {label: "交通规费", value: 3},
  {label: "其它", value: 4}
];

// 收入类型选项
const options2 = [
  {label: "载客", value: 0},
  {label: "运货", value: 1},
  {label: "其它", value: 2}
];

// 注入依赖
const curTab = inject("curTab") as any;
const queryRecordByCid = inject("queryRecordByCid") as Function;
const getLabel = inject("getLabel") as Function;
// 显示组件
const show = ref(true);
// 表单引用
const formRef = ref<FormInstance>();
// 车辆id
const cid = sessionStorage.getItem("cid") || "";
// 车辆记录
let record: any = null;
// 表单
const form = reactive<IForm>({
  id: "",
  cid: "",
  fareType: "0",
  addTime: (new Date()).toString(),
  outType: options1[0].label,
  inType: options2[0].label,
  outFare: 0,
  inFare: 0,
  info: ""
});


// 校验规则
const rules = reactive<FormRules>({
  outFare: [
    { required: true, message: "费用不能为空", trigger: "blur" },
    {required: true, pattern: /^\d+(\.\d+)?$/, message: "费用必须为数字", trigger: "blur"}
  ],
  inFare: [
    { required: true, message: "费用不能为空", trigger: "blur" },
    {required: true, pattern: /^\d+(\.\d+)?$/, message: "费用必须为数字", trigger: "blur"}
  ],
});

// 编辑记录时，设置表单数据
record = sessionStorage.getItem("record");
if (record != null) {
  record = JSON.parse(record);
  form.id = record.id;
  form.cid = record.cid;
  form.fareType = record.fareType;
  form.addTime = record.addTime;
  form.outType = getLabel(options1, record.outType);
  form.inType = getLabel(options2, record.inType);
  form.outFare = record.outFare;
  form.inFare = record.inFare;
  form.info = record.info;
}

watch(() => form.fareType, () => {
  if ('0' === form.fareType) {
    form.inType = options2[0].label;
    form.inFare = 0;
  } else {
    form.outType = options1[0].label;
    form.outFare = 0;
  }
});

/** 提交表单 */
async function handleSubmit(f: FormInstance | undefined) {
  if (!f) return;

  await f.validate((valid, _) => {
    if (valid) {
      if (record != null) editRecord();
      else addRecord();
      return;
    }
    ElMessage.error("表单校验错误");
  });
}

const getValue = function(option:any[], label:string) {
    return option.filter((val) => val.label == label)[0].value || '0';
};

/** 添加记录 */
async function addRecord() {
  form.cid = cid;
  form.addTime = util.formatDate(new Date(form.addTime));
  form.outType = getValue(options1, form.outType);
  form.inType = getValue(options2, form.inType);
  axios.post(`/api/fares/add`, form, {
    headers: {
      Authorization: localStorage.getItem("token")
    }
  }).then(value => {
      const result: any = value.data;
      if (200 === result.code) {
        if (!!queryRecordByCid) queryRecordByCid(cid);
        ElMessage.success(result.info);
      } else {
        ElMessage.error(result.info);
      }
      curTab.value = -1;
      show.value = false;
      sessionStorage.removeItem("cid");
    }).catch(reason => {
      ElNotification({
        title: "提示",
        message: reason,
        duration: 0
      });
    });
}

/** 更新记录 */
async function editRecord() {
  form.addTime = util.formatDate(new Date(form.addTime));
  form.outType = getValue(options1, form.outType);
  form.inType = getValue(options2, form.inType);
  axios.put(`/api/fares/update`, form, {
    headers: {
      Authorization: localStorage.getItem("token")
    }
  }).then(value => {
      const result: any = value.data;
      if (200 === result.code) {
        curTab.value = -1;
        show.value = false;
        queryRecordByCid(cid);
        ElMessage.success(result.info);
        sessionStorage.removeItem("record");
        sessionStorage.removeItem("cid");
      } else {
        ElMessage.error(result.info);
      }
    }).catch(reason => {
      ElNotification({
        title: "提示",
        message: reason,
        duration: 0
      });
    });
}

function closeDialog() {
  curTab.value = -1;
  show.value = false;
}

</script>

<style>
.small-item {
  transform: scale(0.9);
}
</style>