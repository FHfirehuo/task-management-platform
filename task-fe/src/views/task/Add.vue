<template>
  <div class="app-container">
    <el-form ref="form" :model="form" label-width="120px">
      <el-form-item label="任务名称">
        <el-input v-model="form.name" />
      </el-form-item>
      <el-form-item label="任务分组">
        <el-input v-model="form.group" />
      </el-form-item>

      <el-form-item label="任务类型">
        <el-radio-group v-model="form.type" @change="typeChange">
          <el-radio label="JAR" />
          <el-radio label="URL" />
        </el-radio-group>
      </el-form-item>

      <el-form-item label="上传文件" v-if="'JAR' === form.type">
        <el-input v-model="form.file" />
      </el-form-item>

      <el-form-item label="url地址" v-if="'URL' === form.type">
        <el-input v-model="form.url" />
      </el-form-item>

      <el-form-item label="JDK" v-if="'JAR' === form.type">
        <el-select v-model="form.jdk" placeholder="暂不支持jdk8以上版本" style="width: 1158px;">
          <el-option label="JDK8" value="8" />
          <el-option label="JDK11" value="11" />
        </el-select>
      </el-form-item>

      <el-form-item label="执行策略">
        <el-select
          v-model="form.strategy"
          placeholder="请选择执行策略; 无间隔空歇的请使用常驻模式; 间隔大于等于1秒小于等于300秒请使用循环模式；间隔大于300秒的请使用定时模式"
          style="width: 1158px;"
        >
          <el-option label="手动执行" value="manual" />
          <el-option label="定时任务" value="quartz" />
          <el-option label="循环任务" value="cycle" v-if="'JAR' === form.type" />
          <el-option label="常驻任务" value="permanent" v-if="'JAR' === form.type" />
        </el-select>
      </el-form-item>
      <el-form-item label="定时任务表达式" v-if="'quartz' === form.strategy">
        <el-input v-model="form.expression" />
      </el-form-item>

      <el-form-item label="循环JAR任务间隔" v-if="'cycle' === form.strategy">
        <el-input v-model="form.interval" />
      </el-form-item>

      <el-form-item label="JAR任务参数" v-if="'JAR' === form.type">
        <el-input v-model="form.args" />
      </el-form-item>
      <el-form-item label="任务描述">
        <el-input v-model="form.desc" type="textarea" />
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="onSubmit">Create</el-button>
        <el-button @click="onCancel">Cancel</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";

@Component
export default class add extends Vue {
  private form = {
    name: "",
    group: "",
    type: "JAR",
    file: "",
    url: "",
    jdk: 8,
    strategy: "quartz",
    expression: "",
    interval: 1,
    args: "",
    desc: ""
  };

  private onSubmit() {
    this.$message("submit!");
  }

  private onCancel() {
    this.$message({
      message: "cancel!",
      type: "warning"
    });
  }

  private typeChange(value: string) {
    if ("URL" === value) {
      this.form.strategy = "quartz";
    }
  }
}
</script>

<style lang="scss" scoped>
.line {
  text-align: center;
}
</style>
