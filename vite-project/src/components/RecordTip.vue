<template>
  <div>
    <el-drawer 
      v-model="drawer"
      :before-close="() => {curTab = -1}"
      >
      <template #header>
        <h5>油耗及费用计算说明</h5>
      </template>

      <template #default>
        <div>
          <h6>1.油耗计算方式</h6>
          <p class="text-muted">百公里油耗 = (消耗的油量 / 行驶里程) x 100。<br />
              单次油耗 = ((上次'加油之后'的剩余油量 - 当前的油表剩余油量)) / (当前油表里程 - 上次加油时的油表里程) x 100</p>
        </div>
        <el-divider direction="horizontal" content-position="left"></el-divider>
        <div>
          <h6>2.加了两次油，为什么没有油耗数据？</h6>
          <p class="text-muted">可能你第二次勾选了"上次是否漏记"，该选项会影响单次油耗计算，因为这个时候不知道中间遗漏了
            多少加油数据。比如：加入你本次加油时勾选了"上次是否漏记"，那么就无法计算上次油耗。
          </p>
        </div>
        <el-divider direction="horizontal" content-position="left"></el-divider>
        <div>
          <h6>3.车辆油箱体积有什么作用？</h6>
          <p class="text-muted">车辆油箱容量影响油耗计算，请输入准确。不清楚时可以网上检索。<br />
              每次修改车辆油箱容量，都会导致车辆统计数据重新计算。</p>
        </div>
        <el-divider direction="horizontal" content-position="left"></el-divider>
        <div>
          <h6>4.上次漏记怎么办？</h6>
          <p class="text-muted">
            根据实际情况勾选"上次是否漏记"。<br />
            如果选中，本次加油到上次加油这两条记录不会计算油耗数据，系统会忽略这次油耗统计。<br />
            如果真的有遗漏，你可以把之前漏掉的加油数据逐条添加。
          </p>
        </div>
      </template>

      <template #footer>
        <p class="text-muted small text-center">
          如有其它疑问，可到官网<a href="javascript:void(0)" title="xxxx">xxxx</a>获取帮助
        </p>
      </template>
    </el-drawer>
  </div>
</template>

<script setup lang="ts">
import { ref, inject } from 'vue';

const drawer = ref(true);
const curTab = inject("curTab") as any;
console.log(curTab.value);
</script>

<style>
  
</style>