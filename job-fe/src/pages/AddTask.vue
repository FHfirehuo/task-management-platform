<template>
    <div>
        <el-form ref="form" :model="form" label-width="10%" size="mini">
            <el-form-item label="任务名称:">
                <el-input v-model="form.taskName" style="width: 50%"></el-input>
            </el-form-item>
            <el-form-item label="任务分组:">
                <el-input v-model="form.taskGroup" style="width: 50%"></el-input>
            </el-form-item>
            <el-form-item label="JDK版本:">
                <el-select v-model="form.jdkVersion" placeholder="请选择JDK版本">
                    <el-option :label="item" :value="index" v-for="item ,index in jdkVersions"
                               :key="index"></el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="Jar文件上传:">
                <el-upload
                        class="upload-demo"
                        action="https://jsonplaceholder.typicode.com/posts/"
                        :on-preview="handlePreview"
                        :on-remove="handleRemove"
                        :before-remove="beforeRemove"
                        multiple
                        :limit="3"
                        :on-exceed="handleExceed"
                        :file-list="form.fileList">
                    <el-button size="small" type="primary">点击上传</el-button>
                </el-upload>
            </el-form-item>

            <el-form-item label="自停:">
                <el-radio v-model="form.selfStop" label="1">是</el-radio>
                <el-radio v-model="form.selfStop" label="2">否</el-radio>
                <div><p>自停定义: 运行问一个周期会自我终止. 非阻塞的,如WEB程序即为非自停程序</p></div>
            </el-form-item>


            <el-form-item label="执行策略">
                <el-radio v-model="form.executionStrategy" :label="form.executionStrategy">单次执行</el-radio>
            </el-form-item>

            <el-form-item label="特殊资源">
                <el-radio-group v-model="form.resource">
                    <el-radio label="线上品牌商赞助"></el-radio>
                    <el-radio label="线下场地免费"></el-radio>
                </el-radio-group>
            </el-form-item>
            <el-form-item label="活动形式">
                <el-input type="textarea" v-model="form.desc"></el-input>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" @click="onSubmit">立即创建</el-button>
                <el-button>取消</el-button>
            </el-form-item>
        </el-form>
    </div>
</template>

<script>
    import {jdkVersions} from '../mocks/sider';

    export default {
        name: "AddTask",
        data() {
            return {
                form: {
                    taskName: '',
                    taskGroup: '',
                    jdkVersion: '',
                    fileList: [{
                        name: 'food.jpeg',
                        url: 'https://fuss10.elemecdn.com/3/63/4e7f3a15429bfda99bce42a18cdd1jpeg.jpeg?imageMogr2/thumbnail/360x360/format/webp/quality/100'
                    }, {
                        name: 'food2.jpeg',
                        url: 'https://fuss10.elemecdn.com/3/63/4e7f3a15429bfda99bce42a18cdd1jpeg.jpeg?imageMogr2/thumbnail/360x360/format/webp/quality/100'
                    }],
                    selfStop: '',
                    executionStrategy: '1',
                },
                jdkVersions: jdkVersions,

            }
        },
        methods: {
            onSubmit() {
                console.log('submit!');
            },
            handleRemove(file, fileList) {
                console.log(file, fileList);
            },
            handlePreview(file) {
                console.log(file);
            },
            handleExceed(files, fileList) {
                this.$message.warning(`当前限制选择 3 个文件，本次选择了 ${files.length} 个文件，共选择了 ${files.length + fileList.length} 个文件`);
            },
            beforeRemove(file, fileList) {
                return this.$confirm(`确定移除 ${file.name}？`);
            }
        }
    }

</script>

<style>
    .el-row {
        margin-bottom: 20px;
    }

    .el-col {
        border-radius: 4px;
    }

</style>