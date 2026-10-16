<template>
  <div class="system-manage">
    <!-- 搜索表单 -->
    <el-card class="search-card">
      <el-form :model="queryParams" ref="queryForm" :inline="true">
        <el-form-item label="系统名称" prop="name">
          <el-input v-model="queryParams.name" placeholder="请输入系统名称" clearable />
        </el-form-item>
        <el-form-item label="系统编码" prop="code">
          <el-input v-model="queryParams.code" placeholder="请输入系统编码" clearable />
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
        <el-button type="primary" @click="handleAdd">新增系统</el-button>
      </template>

      <!-- 系统列表 -->
      <el-table v-loading="loading" :data="systemList">
        <el-table-column type="index" label="序号" width="50" />
         <el-table-column prop="id" label="id" />
        <el-table-column prop="code" label="系统编码" />
        <el-table-column prop="name" label="系统名称" />
        <el-table-column prop="description" label="系统描述" show-overflow-tooltip />
        <el-table-column prop="status" label="状态">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'">
              {{ row.status === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180" />
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

    <!-- 系统表单对话框 -->
    <el-dialog :title="dialogTitle" v-model="dialogVisible" width="500px" append-to-body>
      <el-form ref="systemFormRef" :model="systemForm" :rules="rules" label-width="80px">
        <el-form-item label="系统编码" prop="code">
          <el-input v-model="systemForm.code" placeholder="请输入系统编码" />
        </el-form-item>
        <el-form-item label="系统名称" prop="name">
          <el-input v-model="systemForm.name" placeholder="请输入系统名称" />
        </el-form-item>
        <el-form-item label="系统描述" prop="description">
          <el-input v-model="systemForm.description" type="textarea" placeholder="请输入系统描述" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-switch v-model="systemForm.status" :active-value="1" :inactive-value="0" active-text="启用"
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
import { getSystemListApi, createSystemApi, updateSystemApi, deleteSystemApi } from './apis/system'
import type { System } from './apis/type'

// 查询参数
const queryParams = reactive<System.SystemQuery>({
  current: 1,
  size: 10,
  name: "",
  code: ""
})

// 系统列表数据
const systemList = ref<System.SystemInfo[]>([])
const total = ref(0)
const loading = ref(false)

// 表单相关
const dialogVisible = ref(false)
const dialogTitle = ref('')
const systemFormRef = ref<FormInstance>()
const systemForm = reactive<System.SystemForm>({
  code: '',
  name: '',
  description: '',
  status: 1
})

// 表单校验规则
const rules = {
  code: [
    { required: true, message: "请输入系统编码", trigger: "blur" },
    { min: 2, max: 20, message: "长度在 2 到 20 个字符", trigger: "blur" }
  ] as FormItemRule[],
  name: [
    { required: true, message: "请输入系统名称", trigger: "blur" },
    { min: 2, max: 50, message: "长度在 2 到 50 个字符", trigger: "blur" }
  ] as FormItemRule[],
  description: [
    { max: 200, message: "长度不能超过 200 个字符", trigger: "blur" }
  ] as FormItemRule[],
  status: [
    { required: true, message: "请选择系统状态", trigger: "change" }
  ] as FormItemRule[]
}

// 获取系统列表
const getList = async () => {
  loading.value = true
  try {
    const res = await getSystemListApi(queryParams)
    if (res.code === 200) {
      systemList.value = res.data.records
      total.value = res.data.total
    }
  } catch (error) {
    console.error("获取系统列表失败:", error)
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
  queryParams.name = ""
  queryParams.code = ""
  handleQuery()
}

// 新增按钮
const handleAdd = () => {
  dialogTitle.value = '新增系统'
  dialogVisible.value = true
  systemForm.id = undefined
  systemForm.code = ''
  systemForm.name = ''
  systemForm.description = ''
  systemForm.status = 1
}

// 修改按钮
const handleUpdate = (row: System.SystemInfo) => {
  dialogTitle.value = '修改系统'
  dialogVisible.value = true
  Object.assign(systemForm, row)
}

// 删除按钮
const handleDelete = (row: System.SystemInfo) => {
  ElMessageBox.confirm("确认要删除该系统吗？", "警告", {
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
      const res = await deleteSystemApi(String(row.id))
      if (res.code === 200) {
        ElMessage.success(res.message)
        getList()
      }
    } catch (error) {
      console.error("删除系统失败:", error)
    }
  }).catch(() => {
    ElMessage.info("已取消删除")
  })
}

// 提交表单
const submitForm = async () => {
  if (!systemFormRef.value) return
  await systemFormRef.value.validate(async (valid: boolean) => {
    if (valid) {
      try {
        if (systemForm.id) {
          const res = await updateSystemApi(systemForm)
          if (res.code === 200) {
            ElMessage.success(res.message)
            dialogVisible.value = false
            getList()
          }
        } else {
          const res = await createSystemApi(systemForm)
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
.system-manage {
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
