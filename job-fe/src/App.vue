<template>
    <div>
        <el-container style="height: 100%; border: 1px solid #eee">
            <el-header>
                <i class="el-icon-edit"/>
                管理系统
            </el-header>
            <el-container>
                <el-aside width="200px">
                    <el-menu
                            class="el-menu-vertical-demo"
                            @select="jump"
                            background-color="#303133"
                            text-color="#fff"
                            active-text-color="#ffd04b">
                        <el-menu-item v-for="item,index in siderDatas" :key="index" :index="index+''">
                            <i class="el-icon-menu"></i>
                            <span slot="title">{{item}}</span>
                        </el-menu-item>
                    </el-menu>
                </el-aside>
                <el-main>
                    <Table/>
                </el-main>
            </el-container>
        </el-container>
    </div>
</template>

<style>
    .el-header {
        background-color: #000000;
        color: #FFFFFF;
        line-height: 60px;
        font-weight: bold;
        border-radius: 2px;
        box-shadow: 0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04)

    }

    .el-aside {
        background-color: #303133;
        line-height: 100%;
    }
</style>

<script>
    import {siderDatas} from './mocks/sider.js'
    import Table from "@/pages/Table";


    export default {
        name: "APP",
        components: {Table},
        data() {

            active :'';
            const item = {
                date: '2016-05-02',
                name: '王小虎',
                address: '上海市普陀区金沙江路 1518 弄'
            };
            alert(siderDatas);
            return {
                tableData: Array(10).fill(item),
                siderDatas: siderDatas
            }
        },
        methods: {
            jump(key, keypath) {
                alert(key);
                alert(keypath);
            }
        }
    };
</script>