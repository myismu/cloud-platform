<template>
  <div class="online-users-container">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>在线用户监控</span>
          <el-button type="primary" @click="refreshList">刷新</el-button>
        </div>
      </template>

      <el-table :data="onlineUsers" style="width: 100%" v-loading="loading">
        <el-table-column prop="systemCode" label="系统名称" />
        <el-table-column prop="userId" label="用户ID" width="100" />
        <el-table-column prop="username" label="用户名" width="120" />
        <el-table-column prop="ip" label="IP地址" width="140" />
        <el-table-column prop="userAgent" label="用户代理" show-overflow-tooltip />
        <el-table-column prop="loginTime" label="登录时间" width="180" />
        <el-table-column prop="clientType" label="设备类型" width="100" />
        <el-table-column label="操作" width="120" fixed="right">
          <template #default="scope">
            <el-button
              type="danger"
              size="small"
              @click="handleKickOut(scope.row)"
            >
              踢出
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script lang="ts" setup>
import { ref, onMounted } from "vue"
import { ElMessage, ElMessageBox } from "element-plus"
import { getOnlineUsers, kickOutUser } from "./apis"
import type { OnlineUser, ApiResponse } from "./apis/type"

const loading = ref(false)
const onlineUsers = ref<OnlineUser[]>([])

// 获取在线用户列表
const fetchOnlineUsers = async () => {
  loading.value = true
  try {
    const res = await getOnlineUsers()
    onlineUsers.value = (res as ApiResponse<OnlineUser[]>).data
  } catch (error) {
    ElMessage.error("获取在线用户列表失败")
  } finally {
    loading.value = false
  }
}

// 刷新列表
const refreshList = () => {
  fetchOnlineUsers()
}

// 踢出用户
const handleKickOut = (row: OnlineUser) => {
  ElMessageBox.confirm(
    `确定要踢出用户 ${row.username} 吗？`,
    "警告",
    {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
      type: "warning"
    }
  ).then(async () => {
    try {
      await kickOutUser(row.userId.toString())
      ElMessage.success("踢出成功")
      fetchOnlineUsers()
    } catch (error) {
      ElMessage.error("踢出失败")
    }
  })
}

onMounted(() => {
  fetchOnlineUsers()
})
</script>

<style scoped>
.online-users-container {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
