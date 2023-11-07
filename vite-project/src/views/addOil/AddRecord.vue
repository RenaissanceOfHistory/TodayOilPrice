<template>
  <div>
    <!-- 新建加油 -->
    <el-dialog title="新建加油" v-model="show" draggable @close="closeDialog" class="col-12 col-sm-10 col-md-7">
      <el-form :model="form" ref="formRef" :rules="rules" label-width="120px">
        <el-form-item label="日期时间" prop="addTime">
          <el-date-picker v-model="form.addTime" placeholder="选择日期时间" type="date"></el-date-picker>
        </el-form-item>

        <el-form-item label="当前里程" prop="mile">
          <el-input v-model="form.mile" placeholder="上次里程*KM" clearable></el-input>
        </el-form-item>

        <el-space size="small" class="border-top border-bottom w-100 d-flex justify-content-evenly">
          <el-form-item label="金额" prop="balance" class="small-item d-inline-block">
            <el-input v-model="form.balance" placeholder="0" style="width: 10em;" @input="handleInput(1)">
              <template #append>
                <span>元</span>
              </template>
            </el-input>
          </el-form-item>

          <el-form-item label="单价" prop="unitPrice" class="small-item d-inline-block">
            <el-input v-model="form.unitPrice" placeholder="0" style="width: 12em;" @input="handleInput(2)">
              <template #append>
                <span>元/L</span>
              </template>
            </el-input>
          </el-form-item>

          <el-form-item label="油量" prop="volume" class="small-item d-inline-block">
            <el-input v-model="form.volume" placeholder="0" style="width: 10em;" @input="handleInput(3)">
              <template #append>
                <span>L</span>
              </template>
            </el-input>
          </el-form-item>
        </el-space>

        <el-form-item label="加油前剩余油量" prop="leftover" class="mt-4">
          <el-radio-group v-model="form.leftover">
            <el-radio-button :label="10">油灯亮了</el-radio-button>
            <el-radio-button :label="25">1/4</el-radio-button>
            <el-radio-button :label="33">1/3</el-radio-button>
            <el-radio-button :label="50">1/2</el-radio-button>
          </el-radio-group>
          <el-slider v-model="form.leftover"></el-slider>
        </el-form-item>

        <el-form-item label="标号">
          <el-radio-group v-model="form.note">
            <el-radio label="92#"></el-radio>
            <el-radio label="95#"></el-radio>
            <el-radio label="98#"></el-radio>
            <el-radio label="0#"></el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="上次是否漏记">
          <el-checkbox v-model="form.forgot" :indeterminate="false"></el-checkbox>
        </el-form-item>

        <el-divider direction="horizontal" content-position="left">选填</el-divider>

        <el-form-item label="加油站">
          <el-input disabled placeholder="输入加油站"></el-input>
        </el-form-item>

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
import { ref, reactive, inject } from 'vue';
import axios from 'axios';
import util from '../../utils/util';

// 表单接口
interface IForm {
  id: string;
  cid: string;
  addTime: string;
  mile: number;
  balance: number;
  unitPrice: number;
  volume: number;
  leftover: number;
  note: string;
  forgot: boolean;
  staId: string;
  info: string;
}

// 获取组件下标引用
const curTab = inject("curTab") as any;
const queryRecordByCid = inject("queryRecordByCid") as Function;
const show = ref(true);
// 表单引用
const formRef = ref<FormInstance>();
// 车辆id
const cid = sessionStorage.getItem("cid") as string | "";
let record: any = null;
// 表单
const form = reactive<IForm>({
  id: "",
  cid: "",
  addTime: (new Date()).toString(),
  mile: 0,
  balance: 0,
  unitPrice: 0,
  volume: 0,
  leftover: 10,
  note: "92#",
  forgot: false,
  staId: "",
  info: ""
});

// 表单规则
const rules = reactive<FormRules>({
  mile: [
    { required: true, message: "当前里程不能为空", trigger: "blur" },
    {required: true, pattern: /^\d+(\.\d+)?$/, message: "当前里程必须为数字", trigger: "blur"}
  ],
  balance: [
    { required: true, message: "金额不能为空", trigger: "blur" },
    {required: true, pattern: /^\d+(\.\d+)?$/, message: "金额必须为数字", trigger: "blur"}
  ],
  unitPrice: [
    { required: true, message: "单价不能为空", trigger: "blur" },
    {required: true, pattern: /^\d+(\.\d+)?$/, message: "单价必须为数字", trigger: "blur"}
  ],
  volume: [
    {required: true, pattern: /^\d+(\.\d+)?$/, message: "油量必须为数字", trigger: "blur"}
  ]
});

// 编辑加油记录时设置值
record = sessionStorage.getItem("record");
if (record != null) {
  record = JSON.parse(record);
  form.id = record.id;
  form.cid = record.cid;
  form.addTime = record.addTime;
  form.mile = record.mile;
  form.balance = record.balance;
  form.unitPrice = record.unitPrice;
  form.volume = record.volume;
  form.leftover = record.leftover;
  form.note = record.note;
  form.forgot = JSON.parse(record.forgot);
  form.staId = record.staId;
  form.info = record.info;
}

/** 表单提交 */
async function handleSubmit(f: FormInstance | undefined) {
  // 如果对象未定义，结束程序
  if (!f) return;

  // 验证表单
  await f.validate((valid, _) => {
    if (valid) {
      // 如果record不为null，编辑加油记录

      if (record != null) editRecord();
      else addRecord();
    } else {
      ElMessage.error("表单校验错误");
    }
  });
}

/** 添加加油记录 */
async function addRecord() {
  form.cid = cid;
  form.addTime = util.formatDate(new Date(form.addTime));
  axios.post(`/api/add-oils/add`, form, {
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

/** 编辑加油记录 */
async function editRecord() {
  axios.put(`/api/add-oils/update`, form, {
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
        sessionStorage.removeItem("cId");
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

/** 处理金额、单价和容积的输入 */
function handleInput(index: number) {
  // 获取值
  const balance = Number(form.balance) || 0,
    unitPrice = Number(form.unitPrice) || 0,
    volume = Number(form.volume) || 0;

  // 输入金额
  if (1 === index) {
    // 设置单价和容积
    form.unitPrice = Number((unitPrice / volume).toFixed(2)) || 0;
    form.volume = Number((balance / volume).toFixed(2)) || 0;
  } else if (2 === index) { // 输入单价
    // 设置容积
    form.volume = Number((balance / unitPrice).toFixed(2)) || 0;
  } else { // 输入容积
    // 设置单价
    form.unitPrice = Number((balance / volume).toFixed(2)) || 0;
  }
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