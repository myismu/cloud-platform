<template>
  <div class="login-history-manage">
    <!-- 搜索表单 -->
    <el-card class="search-card">
      <el-form :model="queryParams" ref="queryForm" :inline="true">
        <el-form-item label="用户ID" prop="userId">
          <el-input v-model="queryParams.userId" placeholder="请输入用户ID" clearable />
        </el-form-item>
        <el-form-item label="用户名" prop="username">
          <el-input v-model="queryParams.username" placeholder="请输入用户名" clearable />
        </el-form-item>
        <el-form-item label="IP地址" prop="ipAddress">
          <el-input v-model="queryParams.ipAddress" placeholder="请输入IP地址" clearable />
        </el-form-item>
        <el-form-item label="设备类型" prop="clientType" style="width: 240px">
          <el-select v-model="queryParams.clientType" placeholder="请选择设备类型" clearable>
            <el-option label="PC" value="PC" />
            <el-option label="Android" value="Android" />
            <el-option label="iOS" value="iOS" />
            <el-option label="H5" value="H5" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleQuery">查询</el-button>
          <el-button @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 登录历史列表 -->
    <el-card class="table-card">
      <el-table v-loading="loading" :data="loginHistoryList">
        <el-table-column type="index" label="序号" width="50" />
        <el-table-column prop="systemId" label="系统Id" />
        <el-table-column prop="userId" label="用户ID" width="100" />
        <el-table-column prop="username" label="用户名" width="120" />
        <el-table-column prop="loginTime" label="登录时间" width="180" />
        <el-table-column prop="ipAddress" label="IP地址" width="140" />
        <el-table-column prop="clientType" label="设备类型" width="100" />
        <el-table-column prop="userAgent" label="用户代理" show-overflow-tooltip />
        <el-table-column label="操作" width="180" fixed="right">
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
    <el-dialog title="登录历史详情" v-model="dialogVisible" width="600px" append-to-body>
      <el-descriptions :column="1" border>
        <el-descriptions-item label="用户ID">{{ detail.userId }}</el-descriptions-item>
        <el-descriptions-item label="用户名">{{ detail.username }}</el-descriptions-item>
        <el-descriptions-item label="登录时间">{{ detail.loginTime }}</el-descriptions-item>
        <el-descriptions-item label="IP地址">{{ detail.ipAddress }}</el-descriptions-item>
        <el-descriptions-item label="设备类型">{{ detail.clientType }}</el-descriptions-item>
        <el-descriptions-item label="用户代理">{{ detail.userAgent }}</el-descriptions-item>
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
import { getLoginHistoryListApi, getLoginHistoryByIdApi, deleteLoginHistoryApi } from "./apis/login-history"
import type { LoginHistory } from "./apis/type"

// 查询参数
const queryParams = reactive<LoginHistory.LoginHistoryQuery>({
  current: 1,
  size: 10,
  userId: "",
  username: "",
  ipAddress: "",
  clientType: "",
  timeRange: undefined
})

// 登录历史列表数据
const loginHistoryList = ref<LoginHistory.LoginHistoryInfo[]>([])
const total = ref(0)
const loading = ref(false)

// 详情对话框
const dialogVisible = ref(false)
const detail = reactive<LoginHistory.LoginHistoryInfo>({
  id: "",
  userId: "",
  username: "",
  loginTime: "",
  ipAddress: "",
  userAgent: "",
  clientType: "",
  createTime: "",
  updateTime: ""
})

// 获取登录历史列表
const getList = async () => {
  loading.value = true
  try {
    const res = await getLoginHistoryListApi(queryParams)
    if (res.code === 200) {
      loginHistoryList.value = res.data.records
      total.value = res.data.total
    }
  } catch (error) {
    console.error("获取登录历史列表失败:", error)
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
  queryParams.username = ""
  queryParams.ipAddress = ""
  queryParams.clientType = ""
  queryParams.timeRange = undefined
  handleQuery()
}

// 时间范围改变
const handleTimeRangeChange = (val: [string, string] | undefined) => {
  queryParams.timeRange = val
}

// 查看详情
const handleView = async (row: LoginHistory.LoginHistoryInfo) => {
  try {
    const res = await getLoginHistoryByIdApi(row.id)
    if (res.code === 200) {
      Object.assign(detail, res.data)
      dialogVisible.value = true
    }
  } catch (error) {
    console.error("获取登录历史详情失败:", error)
  }
}

// 删除按钮
const handleDelete = (row: LoginHistory.LoginHistoryInfo) => {
  ElMessageBox.confirm("确认要删除该登录历史记录吗？", "警告", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning"
  }).then(async () => {
    try {
      const res = await deleteLoginHistoryApi(row.id)
      if (res.code === 200) {
        ElMessage.success(res.message)
        getList()
      }
    } catch (error) {
      console.error("删除登录历史记录失败:", error)
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
.login-history-manage {
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
