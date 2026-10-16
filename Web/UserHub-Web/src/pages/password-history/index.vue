<template>
  <div class="password-history-manage">
    <!-- 搜索表单 -->
    <el-card class="search-card">
      <el-form :model="queryParams" ref="queryForm" :inline="true">
        <el-form-item label="用户ID" prop="userId">
          <el-input v-model="queryParams.userId" placeholder="请输入用户ID" clearable />
        </el-form-item>
        <el-form-item label="修改原因" prop="changeReason">
          <el-input v-model="queryParams.changeReason" placeholder="请输入修改原因" clearable />
        </el-form-item>
        <!-- <el-form-item label="修改时间" prop="changeTime">
          <el-date-picker
            v-model="queryParams.changeTime"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            value-format="YYYY-MM-DD"
            @change="handleTimeRangeChange"
          />
        </el-form-item> -->
        <el-form-item>
          <el-button type="primary" @click="handleQuery">查询</el-button>
          <el-button @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 密码历史列表 -->
    <el-card class="table-card">
      <el-table v-loading="loading" :data="passwordHistoryList">
        <el-table-column type="index" label="序号" width="50" />
        <el-table-column prop="userId" label="用户ID" />
        <el-table-column prop="systemId" label="系统Id" />
        <el-table-column prop="changeTime" label="修改时间" />
        <el-table-column prop="changeReason" label="修改原因" show-overflow-tooltip />
        <el-table-column prop="createTime" label="创建时间" />
        <el-table-column label="操作" width="180">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleView(row)">查看</el-button>
            <el-button type="danger" link @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination">
        <el-pagination v-model:current-page="queryParams.current" v-model:page-size="queryParams.size"
          :total="total" :page-sizes="[10, 20, 50, 100]" layout="total, sizes, prev, pager, next"
          @size-change="handleSizeChange" @current-change="handleCurrentChange" />
      </div>
    </el-card>

    <!-- 查看详情对话框 -->
    <el-dialog title="密码历史详情" v-model="dialogVisible" width="600px" append-to-body>
      <el-descriptions :column="1" border>
        <el-descriptions-item label="用户ID">{{ detail.userId }}</el-descriptions-item>
        <el-descriptions-item label="密码">{{ detail.password }}</el-descriptions-item>
        <el-descriptions-item label="修改时间">{{ detail.changeTime }}</el-descriptions-item>
        <el-descriptions-item label="修改原因">{{ detail.changeReason }}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ detail.createTime }}</el-descriptions-item>
        <el-descriptions-item label="更新时间">{{ detail.updateTime }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from "vue"
import { ElMessage, ElMessageBox } from "element-plus"
import type { FormInstance } from "element-plus"
import { getPasswordHistoryListApi, getPasswordHistoryByIdApi, deletePasswordHistoryApi } from "./apis/password-history"
import type { PasswordHistory } from "./apis/type"

// 查询参数
const queryParams = reactive<PasswordHistory.PasswordHistoryQuery>({
  current: 1,
  size: 10,
  userId: "",
  changeReason: "",
  changeTime: undefined
})

// 密码历史列表数据
const passwordHistoryList = ref<PasswordHistory.PasswordHistoryInfo[]>([])
const total = ref(0)
const loading = ref(false)

// 详情对话框
const dialogVisible = ref(false)
const detail = reactive<PasswordHistory.PasswordHistoryInfo>({
  id: "",
  userId: "",
  password: "",
  changeTime: "",
  changeReason: "",
  createTime: "",
  updateTime: ""
})

// 获取密码历史列表
const getList = async () => {
  loading.value = true
  try {
    const res = await getPasswordHistoryListApi(queryParams)
    if (res.code === 200) {
      passwordHistoryList.value = res.data.records
      total.value = res.data.total
    }
  } catch (error) {
    console.error("获取密码历史列表失败:", error)
  } finally {
    loading.value = false
  }
}

// 查询按钮
const handleQuery = () => {
  queryParams.current = 1
  getList()
}

// 重置按钮
const resetQuery = () => {
  queryParams.userId = ""
  queryParams.changeReason = ""
  queryParams.changeTime = undefined
  handleQuery()
}

// 时间范围改变
const handleTimeRangeChange = (val: [string, string] | undefined) => {
  queryParams.changeTime = val
}

// 查看详情
const handleView = async (row: PasswordHistory.PasswordHistoryInfo) => {
  try {
    const res = await getPasswordHistoryByIdApi(row.id)
    if (res.code === 200) {
      Object.assign(detail, res.data)
      dialogVisible.value = true
    }
  } catch (error) {
    console.error("获取密码历史详情失败:", error)
  }
}

// 删除按钮
const handleDelete = (row: PasswordHistory.PasswordHistoryInfo) => {
  ElMessageBox.confirm("确认要删除该密码历史记录吗？", "警告", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning"
  }).then(async () => {
    try {
      const res = await deletePasswordHistoryApi(row.id)
      if (res.code === 200) {
        ElMessage.success(res.message)
        getList()
      }
    } catch (error) {
      console.error("删除密码历史记录失败:", error)
    }
  }).catch(() => {
    ElMessage.info("已取消删除")
  })
}

// 分页大小改变
const handleSizeChange = (val: number) => {
  queryParams.size = val
  getList()
}

// 页码改变
const handleCurrentChange = (val: number) => {
  queryParams.current = val
  getList()
}

// 初始化
onMounted(() => {
  getList()
})
</script>

<style scoped>
.password-history-manage {
  padding: 20px;
}

.search-card {
  margin-bottom: 20px;
}

.table-card {
  margin-bottom: 20px;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style>
