<template>
  <div class="route-manage">
    <!-- 搜索表单 -->
    <el-card class="search-card">
      <el-form :model="queryParams" ref="queryForm" :inline="true">
        <el-form-item label="路由路径" prop="path">
          <el-input v-model="queryParams.path" placeholder="请输入路由路径" clearable />
        </el-form-item>
        <el-form-item label="路由名称" prop="name">
          <el-input v-model="queryParams.name" placeholder="请输入路由名称" clearable />
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
        <el-button type="primary" @click="handleAdd">新增路由</el-button>
      </template>

      <!-- 路由列表 -->
      <el-table v-loading="loading" :data="routeList">
        <el-table-column type="index" label="序号" width="50" />
        <el-table-column prop="systemId" label="系统Id" />
        <el-table-column prop="path" label="路由路径" />
        <el-table-column prop="name" label="路由名称" />
        <el-table-column prop="component" label="组件路径" />
        <el-table-column prop="icon" label="图标" />
        <el-table-column prop="sort" label="排序" width="80" />
        <el-table-column prop="status" label="状态" width="80">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'">
              {{ row.status === 1 ? '正常' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" />
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

    <!-- 路由表单对话框 -->
    <el-dialog :title="dialogTitle" v-model="dialogVisible" width="600px" append-to-body>
      <el-form ref="routeFormRef" :model="routeForm" :rules="rules" label-width="100px">
        <el-form-item label="路由路径" prop="path">
          <el-input v-model="routeForm.path" placeholder="请输入路由路径" />
        </el-form-item>
        <el-form-item label="路由名称" prop="name">
          <el-input v-model="routeForm.name" placeholder="请输入路由名称" />
        </el-form-item>
        <el-form-item label="组件路径" prop="component">
          <el-input v-model="routeForm.component" placeholder="请输入组件路径" />
        </el-form-item>
        <el-form-item label="父级路由" prop="parentId">
          <el-select v-model="routeForm.parentId" placeholder="请选择父级路由" clearable>
            <el-option v-for="item in parentRoutes" :key="item.id" :label="item.name" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="图标" prop="icon">
          <el-input v-model="routeForm.icon" placeholder="请输入图标名称" />
        </el-form-item>
        <el-form-item label="排序" prop="sort">
          <el-input-number v-model="routeForm.sort" :min="0" :max="999" />
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input v-model="routeForm.description" type="textarea" placeholder="请输入描述" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-switch v-model="routeForm.status" :active-value="1" :inactive-value="0" active-text="正常"
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
import { ref, reactive, onMounted } from "vue"
import { ElMessage, ElMessageBox } from "element-plus"
import type { FormInstance, FormItemRule } from "element-plus"
import { getRouteListApi, getAllRoutesApi, createRouteApi, updateRouteApi, deleteRouteApi } from "./apis/route"
import type { Route} from "./apis/type"

// 查询参数
const queryParams = reactive<Route.RouteQuery>({
  current: 1,
  size: 10,
  path: "",
  name: "",
  status: undefined
})

// 路由列表数据
const routeList = ref<Route.RouteInfo[]>([])
const total = ref(0)
const loading = ref(false)
const parentRoutes = ref<Route.RouteInfo[]>([])

// 表单相关
const dialogVisible = ref(false)
const dialogTitle = ref("")
const routeFormRef = ref<FormInstance>()
const routeForm = reactive<Route.RouteForm>({
  path: "",
  name: "",
  component: "",
  parentId: undefined,
  icon: "",
  sort: 0,
  status: 1,
  description: ""
})

// 表单校验规则
const rules = {
  path: [
    { required: true, message: "请输入路由路径", trigger: "blur" },
    { min: 2, max: 100, message: "长度在 2 到 100 个字符", trigger: "blur" }
  ] as FormItemRule[],
  name: [
    { required: true, message: "请输入路由名称", trigger: "blur" },
    { min: 2, max: 50, message: "长度在 2 到 50 个字符", trigger: "blur" }
  ] as FormItemRule[],
  component: [
    { required: true, message: "请输入组件路径", trigger: "blur" }
  ] as FormItemRule[],
  status: [
    { required: true, message: "请选择状态", trigger: "change" }
  ] as FormItemRule[]
}

// 获取路由列表
const getList = async () => {
  loading.value = true
  try {
    const res = await getRouteListApi(queryParams)
    if (res.code === 200) {
      routeList.value = res.data.records
      console.log("返回",res.data.records)
      total.value = res.data.total
    }
  } catch (error) {
    console.error("获取路由列表失败:", error)
  } finally {
    loading.value = false
  }
}

// 获取父级路由列表
const getParentRoutes = async () => {
  try {
    const res = await getAllRoutesApi()
    if (res.code === 200) {
      parentRoutes.value = res.data
    }
  } catch (error) {
    console.error("获取父级路由列表失败:", error)
  }
}

// 查询按钮
const handleQuery = () => {
  queryParams.current = 1
  getList()
}

// 重置按钮
const resetQuery = () => {
  queryParams.path = ""
  queryParams.name = ""
  queryParams.status = undefined
  handleQuery()
}

// 新增按钮
const handleAdd = () => {
  dialogTitle.value = "新增路由"
  dialogVisible.value = true
  routeForm.id = undefined
  routeForm.path = ""
  routeForm.name = ""
  routeForm.component = ""
  routeForm.parentId = undefined
  routeForm.icon = ""
  routeForm.sort = 0
  routeForm.status = 1
  routeForm.description = ""
}

// 修改按钮
const handleUpdate = (row: Route.RouteInfo) => {
  dialogTitle.value = "修改路由"
  dialogVisible.value = true
  Object.assign(routeForm, row)
}

// 删除按钮
const handleDelete = (row: Route.RouteInfo) => {
  ElMessageBox.confirm("确认要删除该路由吗？", "警告", {
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
      const res = await deleteRouteApi(String(row.id))
      if (res.code === 200) {
        ElMessage.success(res.message)
        getList()
      }
    } catch (error) {
      console.error("删除路由失败:", error)
    }
  }).catch(() => {
    ElMessage.info("已取消删除")
  })
}

// 提交表单
const submitForm = async () => {
  if (!routeFormRef.value) return
  await routeFormRef.value.validate(async (valid: boolean) => {
    if (valid) {
      try {
        if (routeForm.id) {
          alert(routeForm.id)
          const res = await updateRouteApi(String(routeForm.id), routeForm)
          if (res.code === 200) {
            ElMessage.success(res.message)
            dialogVisible.value = false
            getList()
          }
        } else {
          const res = await createRouteApi(routeForm)
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
  getParentRoutes()
})
</script>

<style scoped>
.route-manage {
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
