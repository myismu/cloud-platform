<template>
  <div class="role-manage">
    <!-- 搜索表单 -->
    <el-card class="search-card">
      <el-form :model="queryParams" ref="queryForm" :inline="true">
        <el-form-item label="角色名称" prop="roleName">
          <el-input v-model="queryParams.roleName" placeholder="请输入角色名称" clearable />
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input v-model="queryParams.description" placeholder="请输入描述" clearable />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="queryParams.status" placeholder="请选择状态" clearable style="width: 140px">
            <el-option label="正常" :value="1" />
            <el-option label="禁用" :value="0" />
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
        <el-button type="primary" @click="handleAdd">新增角色</el-button>
      </template>

      <!-- 角色列表 -->
      <el-table v-loading="loading" :data="roleList">
        <el-table-column type="index" label="序号" width="50" />
        <el-table-column prop="id" label="id" />
        <el-table-column prop="systemId" label="系统Id" />
        <el-table-column prop="roleName" label="角色名称" />
        <el-table-column prop="roleCode" label="角色代码" />
        <el-table-column prop="description" label="描述" />
        <el-table-column prop="status" label="状态">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'">
              {{ row.status === 1 ? '正常' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" />
          <el-table-column prop="updateTime" label="更改时间" />
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

    <!-- 角色表单对话框 -->
    <el-dialog :title="dialogTitle" v-model="dialogVisible" width="500px" append-to-body>
      <el-form ref="roleFormRef" :model="roleForm" :rules="rules" label-width="80px">
        <el-form-item label="角色名称" prop="roleName">
          <el-input v-model="roleForm.roleName" placeholder="请输入角色名称" />
        </el-form-item>
                <el-form-item label="角色代码" >
          <el-input v-model="roleForm.roleCode" placeholder="请输入角色代码（用于权限控制）" />
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input v-model="roleForm.description" type="textarea" placeholder="请输入描述" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-switch v-model="roleForm.status" :active-value="1" :inactive-value="0" active-text="正常"
            inactive-text="禁用" />
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
import { getRoleListApi, createRoleApi, updateRoleApi, deleteRoleApi } from './apis/role'
import type { Role } from './apis/type'

// 查询参数
const queryParams = reactive<Role.RoleQuery>({
  current: 1,
  size: 10,
  roleName: "",
  description: "",
  status: undefined
})

// 角色列表数据
const roleList = ref<Role.RoleInfo[]>([])
const total = ref(0)
const loading = ref(false)

// 表单相关
const dialogVisible = ref(false)
const dialogTitle = ref('')
const roleFormRef = ref<FormInstance>()
const roleForm = reactive<Role.RoleForm>({
  roleName: '',
  roleCode:'',
  description: '',
  status: 1
})

// 表单校验规则
const rules = {
  roleName: [
    { required: true, message: "请输入角色名称", trigger: "blur" },
    { min: 2, max: 20, message: "长度在 2 到 20 个字符", trigger: "blur" }
  ] as FormItemRule[],
  description: [
    { required: true, message: "请输入描述", trigger: "blur" },
    { max: 200, message: "长度不能超过 200 个字符", trigger: "blur" }
  ] as FormItemRule[],
  status: [
    { required: true, message: "请选择状态", trigger: "change" }
  ] as FormItemRule[]
}

// 获取角色列表
const getList = async () => {
  loading.value = true
  try {
    const res = await getRoleListApi(queryParams)
    if (res.code === 200) {
      roleList.value = res.data.records
      total.value = res.data.total
    }
  } catch (error) {
    console.error("获取角色列表失败:", error)
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
  queryParams.roleName = ""
  queryParams.description = ""
  queryParams.status = undefined
  handleQuery()
}

// 新增按钮
const handleAdd = () => {
  dialogTitle.value = '新增角色'
  dialogVisible.value = true
  roleForm.id = undefined
  roleForm.roleName = ''
  roleForm.roleCode = ''
  roleForm.description = ''
  roleForm.status = 1
}

// 修改按钮
const handleUpdate = (row: Role.RoleInfo) => {
  dialogTitle.value = '修改角色'
  dialogVisible.value = true
  Object.assign(roleForm, row)
}

// 删除按钮
const handleDelete = (row: Role.RoleInfo) => {
  ElMessageBox.confirm("确认要删除该角色吗？", "警告", {
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
      const res = await deleteRoleApi(String(row.id))
      if (res.code === 200) {
        ElMessage.success(res.message)
        getList()
      }
    } catch (error) {
      console.error("删除角色失败:", error)
    }
  }).catch(() => {
    ElMessage.info("已取消删除")
  })
}

// 提交表单
const submitForm = async () => {
  if (!roleFormRef.value) return
  await roleFormRef.value.validate(async (valid: boolean) => {
    if (valid) {
      try {
        if (roleForm.id) {
          const res = await updateRoleApi(String(roleForm.id), roleForm)
          if (res.code === 200) {
            ElMessage.success('修改角色成功')
            dialogVisible.value = false
            getList()
          }
        } else {
          const res = await createRoleApi(roleForm)
          if (res.code === 200) {
            ElMessage.success('新增角色成功')
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
.role-manage {
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
