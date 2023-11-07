<template>
  <div>
    <el-container>
      <el-main>
        <div class="row">
          <div class="col-12 col-md-7 mx-auto p-2 border-bottom border-5 border-success-subtle mt-5">
            <!-- 油耗计算器 -->
            <el-descriptions title="油耗计算器">
              <el-descriptions-item label="当前油价(元/升)">
                <el-input v-model.trim="calculator.oilPrice"
                   class="d-inline-block" @input="onInput('oilPrice')" @change="onChange('oilPrice')"></el-input>
              </el-descriptions-item>

              <el-descriptions-item label="加了多少钱的油(元)">
                <el-input v-model.trim="calculator.output" 
                  class="d-inline-block" @input="onInput('output')" @change="onChange('output')"></el-input>
              </el-descriptions-item>
              
              <el-descriptions-item label="或加了多少升油(升)"> 
                <el-input v-model.trim="calculator.volume" 
                  class="d-inline-block" @input="onInput('volume')" @change="onChange('volume')"></el-input>
              </el-descriptions-item>

              <el-descriptions-item label="行驶里程(公里)">
                <el-input v-model.trim="calculator.mile" 
                  class="d-inline-block" @input="onInput('mile')" @change="onChange('mile')"></el-input>
              </el-descriptions-item>

              <el-descriptions-item label="百公里油耗(升/百公里)">
                <el-input v-model.trim="calculator.consume" 
                  class="d-inline-block" @input="onInput('consume')" @change="onChange('consume')"></el-input>
              </el-descriptions-item>

              <el-descriptions-item label="每公里价钱(元/公里)">
                <el-input v-model.trim="calculator.pricePerMile" 
                  class="d-inline-block" @input="onInput('pricePerMile')" @change="onChange('pricePerMile')"></el-input>
              </el-descriptions-item>
            </el-descriptions>
          </div>
        </div>
      </el-main>
    </el-container>
  </div>
</template>

<script setup lang="ts">
import { reactive } from 'vue';

// 属性值
const calculator = reactive({
  oilPrice: "",
  output: "",
  volume: "",
  mile: "",
  consume: "",
  pricePerMile: ""
});

// 输入事件
function onInput(prop: string) {
  const tmp: string = calculator[prop];
  // 只能输入数字和.
  if (!/[\d\.]/.test(tmp.charAt(tmp.length - 1))) {
    calculator[prop] = tmp.substring(0, tmp.length - 1);
    return;
  }

  // 以.开头时，自动补0
  if ("." === tmp.charAt(0)) {
    calculator[prop] = `0${tmp}`;
  }

  // 计算相关属性值
  if ("oilPrice" === prop || "output" === prop) {
    calculator.volume = ((Number(calculator.output) / Number(calculator.oilPrice)) || 0).toFixed(2);
  } else if ("volume" === prop) {
    calculator.oilPrice = ((Number(calculator.output) / Number(calculator.volume)) || 0).toFixed(2);
  } else if ("mile" === prop) {
    calculator.consume = ((Number(calculator.volume) / Number(calculator.mile) * 100) || 0).toFixed(2);
    calculator.pricePerMile = ((Number(calculator.consume) * Number(calculator.oilPrice) / 100) || 0).toFixed(2);
  }
}

// 改变事件
function onChange(prop: string) {
  const tmp: string = calculator[prop];
  // 以.结尾时，自动补00
  if ("." === tmp.charAt(tmp.length - 1)) {
    calculator[prop] = `${tmp}00`;
  }
}
</script>

<style scoped></style>