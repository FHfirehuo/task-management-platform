<template>
  <el-upload
    ref="upload"
    :action="url"
    :on-preview="handlePreview"
    :on-remove="handleRemove"
    :before-remove="beforeRemove"
    :on-success="onSuccess"
    multiple
    false
    :limit="1"
    with-credentials
    true
    :on-exceed="handleExceed"
  >
    <el-button size="small" type="primary">点击上传</el-button>
  </el-upload>
</template>

<script lang="ts">
import { Vue, Component } from "vue-property-decorator";

@Component
export default class SingleUpload extends Vue {
  private url: string = "";
  private handleRemove(file: string, fileList: any) {
    this.$emit("uploaded", "", "");
  }

  private handlePreview(file: string) {}

  private handleExceed(files: any, fileList: any) {
    this.$message.warning(
      `当前限制选择 1 个文件，本次选择了 ${
        files.length
      } 个文件，共选择了 ${files.length + fileList.length} 个文件`
    );
  }

  private beforeRemove(file: any, fileList: any) {
    return this.$confirm(`确定移除 ${file.name}？`);
  }

  private onSuccess(response: any, file: any, fileList: any) {
    this.$emit("uploaded", file.name, response.data);
  }

  private onError(err: any, file: any, fileList: any) {
    this.$emit("uploaded", "", "");
  }
}
</script>
<style lang='scss'  scoped>
</style>