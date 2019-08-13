<template>
  <div class="app-container">
    <el-table
      v-loading="listLoading"
      :data="list"
      element-loading-text="Loading"
      border
      fit
      highlight-current-row
    >
      <el-table-column align="center" label="ID" width="95">
        <template slot-scope="scope">{{ scope.$index }}</template>
      </el-table-column>
      <el-table-column label="名称" width="110">
        <template slot-scope="scope">{{ scope.row.name }}</template>
      </el-table-column>
      <el-table-column label="组" width="110" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.group }}</span>
        </template>
      </el-table-column>
      <el-table-column label="类型" width="110" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.type }}</span>
        </template>
      </el-table-column>
      <el-table-column label="内容" width="110" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.file }} || {{ scope.row.url }}</span>
        </template>
      </el-table-column>
      <el-table-column label="策略" width="110" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.strategy }}</span>
        </template>
      </el-table-column>
      <el-table-column label="表达式" width="110" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.expression }}</span>
        </template>
      </el-table-column>
      <el-table-column class-name="status-col" label="任务状态" width="110" align="center">
        <template slot-scope="scope">
          <el-tag :type="scope.row.status | statusFilter">{{ scope.row.status }}</el-tag>
        </template>
      </el-table-column>

      <el-table-column class-name="status-col" label="操作" width="110" align="center"></el-table-column>

      <el-table-column label="所属部门" width="110" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.orgName }}</span>
        </template>
      </el-table-column>

      <el-table-column label="所属人" width="110" align="center">
        <template slot-scope="scope">{{ scope.row.owner }}</template>
      </el-table-column>
      <el-table-column align="center" prop="created_at" label="上次操作时间" width="200">
        <template slot-scope="scope">
          <i class="el-icon-time" />
          <span>{{ scope.row.display_time }}</span>
        </template>
      </el-table-column>

      <el-table-column label="描述" width="210" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.desc }}</span>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script lang="ts">
import { getList } from "@/api/table";
import { Component, Vue } from "vue-property-decorator";

@Component({
  filters: {
    statusFilter(status: string) {
      const statusMap: { [id: string]: string } = {
        published: "success",
        draft: "gray",
        deleted: "danger"
      };
      return statusMap[status];
    }
  }
})
export default class Index extends Vue {
  private list = null;
  private listLoading = true;
  private listQuery = {};

  private created() {
    this.fetchData();
  }

  private fetchData() {
    this.listLoading = true;
    getList(this.listQuery).then(response => {
      this.list = response.data.items;
      this.listLoading = false;
    });
  }
}
</script>
