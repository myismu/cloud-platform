<template>
  <div class="user-manage">
    <!-- 搜索表单 -->
    <el-card class="search-card">
      <el-form :model="queryParams" ref="queryForm" :inline="true">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="queryParams.username" placeholder="请输入用户名" clearable />
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="queryParams.email" placeholder="请输入邮箱" clearable />
        </el-form-item>
        <el-form-item label="手机号" prop="phoneNumber">
          <el-input v-model="queryParams.phoneNumber" placeholder="请输入手机号" clearable />
        </el-form-item>
        <el-form-item label="是否锁定" prop="isLocked">
          <el-select v-model="queryParams.isLocked" placeholder="是否锁定"  style="width: 140px" clearable>
            <el-option label="锁定" :value="1" />
            <el-option label="正常" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleQuery">查询</el-button>
          <el-button @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 操作按钮 -->
    <el-card class="table-card">
      <template #header>
        <el-button type="primary" @click="handleAdd">新增用户</el-button>
      </template>

      <!-- 用户列表 -->
      <el-table v-loading="loading" :data="userList">
        <el-table-column type="index" label="序号" width="50" />
        <el-table-column prop="username" label="用户名" />
        <el-table-column prop="email" label="邮箱" />
        <el-table-column prop="phoneNumber" label="手机号" />
        <el-table-column prop="isLocked" label="是否锁定">
          <template #default="{ row }">
            <el-tag :type="row.isLocked === 1 ? 'danger' : 'success'">
              {{ row.isLocked === 0 ? '正常' : '锁定' }}
            </el-tag>
          </template>
        </el-table-column>
        <!-- <el-table-column prop="online" label="是否在线">
          <template #default="{ row }">
            <el-tag :type="row.online == 1 ? 'success' : 'danger'">
              {{ row.online == 1 ? '在线' : '离线' }}
            </el-tag>
          </template>
        </el-table-column> -->
        <el-table-column label="操作" width="180">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleUpdate(row)">编辑</el-button>
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

    <!-- 用户表单对话框 -->
    <el-dialog :title="dialogTitle" v-model="dialogVisible" width="500px" append-to-body>
      <el-form ref="userFormRef" :model="userForm" :rules="rules" label-width="80px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="userForm.username" placeholder="请输入用户名" />
        </el-form-item>
        <el-form-item label="密码" prop="password" v-if="!userForm.id">
          <el-input v-model="userForm.password" type="password" placeholder="请输入密码" />
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="userForm.email" placeholder="请输入邮箱" />
        </el-form-item>
        <el-form-item label="手机号" prop="phoneNumber">
          <el-input v-model="userForm.phoneNumber" placeholder="请输入手机号" />
        </el-form-item>
        <el-form-item label="是否锁定" prop="isLocked">
          <el-switch v-model="userForm.isLocked" :active-value="1" :inactive-value="0" active-text="锁定"
            inactive-text="正常" />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="dialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="submitForm">确 定</el-button>
        </div>
      </template>
    </el-dialog>

  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import type { FormInstance, FormItemRule } from 'element-plus'
import { getUserListApi, createUserApi, updateUserApi, deleteUserApi } from './apis/user'
import type { User } from './apis/type'

// 查询参数
const queryParams = reactive<User.UserQuery>({
  current: 1,
  size: 10,
  username: "",
  email: "",
  phoneNumber: "",
  isLocked: undefined
})

// 用户列表数据
const userList = ref<User.UserInfo[]>([])
const total = ref(0)
const loading = ref(false)

// 表单相关
const dialogVisible = ref(false)
const dialogTitle = ref('')
const userFormRef = ref<FormInstance>()
const userForm = reactive<User.UserForm>({
  username: '',
  password: '',
  email: '',
  phoneNumber: '',
  isLocked: 1
})

// 表单校验规则
const rules = {
  username: [
    { required: true, message: "请输入用户名", trigger: "blur" },
    { min: 3, max: 20, message: "长度在 3 到 20 个字符", trigger: "blur" }
  ] as FormItemRule[],
  password: [
    { required: true, message: "请输入密码", trigger: "blur" },
    { min: 6, max: 20, message: "长度在 6 到 20 个字符", trigger: "blur" }
  ] as FormItemRule[],
  email: [
    { required: true, message: "请输入邮箱", trigger: "blur" },
    { type: "email", message: "请输入正确的邮箱地址", trigger: "blur" }
  ] as FormItemRule[],
  phoneNumber: [
    { required: true, message: "请输入手机号", trigger: "blur" },
    { pattern: /^1[3-9]\d{9}$/, message: "请输入正确的手机号", trigger: "blur" }
  ] as FormItemRule[],
  isLocked: [
    { required: true, message: "请选择是否锁定用户", trigger: "change" }
  ] as FormItemRule[]
}

// 获取用户列表
const getList = async () => {
  loading.value = true
  try {
    const res = await getUserListApi(queryParams)
    if (res.code === 200) {
      userList.value = res.data.records
      total.value = res.data.total
    }
  } catch (error) {
    console.error("获取用户列表失败:", error)
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
  queryParams.username = ""
  queryParams.email = ""
  queryParams.phoneNumber = ""
  queryParams.isLocked = undefined
  handleQuery()
}

// 新增按钮
const handleAdd = () => {
  dialogTitle.value = '新增用户'
  dialogVisible.value = true
  userForm.id = undefined
  userForm.username = ''
  userForm.password = ''
  userForm.email = ''
  userForm.phoneNumber = ''
  userForm.isLocked = 1
}

// 修改按钮
const handleUpdate = (row: User.UserInfo) => {
  dialogTitle.value = '修改用户'
  dialogVisible.value = true
  Object.assign(userForm, row)
}

// 删除按钮
const handleDelete = (row: User.UserInfo) => {
  ElMessageBox.confirm("确认要删除该用户吗？", "警告", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning",
    draggable: true,
    closeOnClickModal: false,
    closeOnPressEscape: false,
    showClose: true,
    center: true
  }).then(async () => {
    try {
      const res = await deleteUserApi(Number(row.id))
      if (res.code === 200) {
        ElMessage.success(res.message)
        getList()
      }
    } catch (error) {
      console.error("删除用户失败:", error)
    }
  }).catch(() => {
    ElMessage.info("已取消删除")
  })
}

// 提交表单
const submitForm = async () => {
  if (!userFormRef.value) return
  await userFormRef.value.validate(async (valid: boolean) => {
    if (valid) {
      try {
        if (userForm.id) {
          const res = await updateUserApi(Number(userForm.id), userForm)
          if (res.code === 200) {
            ElMessage.success(res.message)
            dialogVisible.value = false
            getList()
          }
        } else {
          const res = await createUserApi(userForm)
          if (res.code === 200) {
            ElMessage.success(res.message)
            dialogVisible.value = false
            getList()
          }
        }
      } catch (error) {
        console.error("提交表单失败:", error)
      }
    }
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
.user-manage {
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

.dialog-footer {
  text-align: right;
}
</style>
