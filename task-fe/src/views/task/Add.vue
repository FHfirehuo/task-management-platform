<template>
  <div class="app-container">
    <el-form
      ref="form"
      :model="form"
      label-width="120px"
    >
      <el-form-item label="任务名称">
        <el-input v-model="form.name" />
      </el-form-item>
      <el-form-item label="任务组">
        <el-input v-model="form.name" />
      </el-form-item>

      <el-form-item label="任务类型">
        <el-select
          v-model="form.region"
          placeholder="请选择执内容"
        > 
          <el-option
            label="JAR"
            value="shanghai"
          />
          <el-option
            label="URL"
            value="beijing"
          />
        </el-select>
      </el-form-item>

      <el-form-item label="上传文件">
        <el-input v-model="form.name" />
      </el-form-item>

      <el-form-item label="url地址">
        <el-input v-model="form.name" />
      </el-form-item>

      <el-form-item label="JDK">
        <el-select
          v-model="form.region"
          placeholder="暂不支持jdk8以上版本"
        >
          <el-option
            label="JDK8"
            value="shanghai"
          />
          <el-option
            label="Zone two"
            value="beijing"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="执行策略">
        <el-select
          v-model="form.region"
          placeholder="请选择执行策略"
        >
          <el-option
            label="手动执行"
            value="shanghai"
          />
          <el-option
            label="定时任务"
            value="beijing"
          />
          <el-option
            label="循环任务"
            value="beijing"
          />
          <el-option
            label="常驻任务"
            value="beijing"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="定时开启">
        <el-col :span="11">
          <el-date-picker
            v-model="form.date1"
            type="date"
            placeholder="Pick a date"
            style="width: 100%;"
          />
        </el-col>
        <el-col
          :span="2"
          class="line"
        >
          定时关闭
        </el-col>
        <el-col :span="11">
          <el-time-picker
            v-model="form.date2"
            type="fixed-time"
            placeholder="Pick a time"
            style="width: 100%;"
          />
        </el-col>
      </el-form-item>
      <el-form-item label="Instant delivery">
        <el-switch v-model="form.delivery" />
      </el-form-item>
      <el-form-item label="表达式">
        <el-input v-model="form.name" />
      </el-form-item>
      <el-form-item label="参数">
        <el-input v-model="form.name" />
      </el-form-item>
      <el-form-item label="Activity type">
        <el-checkbox-group v-model="form.type">
          <el-checkbox
            label="Online activities"
            name="type"
          />
          <el-checkbox
            label="Promotion activities"
            name="type"
          />
          <el-checkbox
            label="Offline activities"
            name="type"
          />
          <el-checkbox
            label="Simple brand exposure"
            name="type"
          />
        </el-checkbox-group>
      </el-form-item>
      <el-form-item label="Resources">
        <el-radio-group v-model="form.resource">
          <el-radio label="Sponsor" />
          <el-radio label="Venue" />
        </el-radio-group>
      </el-form-item>
      <el-form-item label="任务描述">
        <el-input
          v-model="form.desc"
          type="textarea"
        />
      </el-form-item>
      <el-form-item>
        <el-button
          type="primary"
          @click="onSubmit"
        >
          Create
        </el-button>
        <el-button @click="onCancel">
          Cancel
        </el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator'

@Component
export default class add extends Vue {
  private form = {
    name: '',
    region: '',
    date1: '',
    date2: '',
    delivery: false,
    type: [],
    resource: '',
    desc: ''
  };

  private onSubmit() {
    this.$message('submit!')
  }

  private onCancel() {
    this.$message({
      message: 'cancel!',
      type: 'warning'
    })
  }
}
</script>

<style lang="scss" scoped>
.line {
  text-align: center;
}
</style>
