import { request } from "@/http/axios"
import type { Role } from "./type"

/** 分页获取角色列表 */
export function getRoleListApi(params: Role.RoleQuery) {
  return request<Role.ApiResponse<Role.PageResult<Role.RoleInfo>>>({
    url: "/api/roles/page",
    method: "get",
    params
  })
}

/** 获取所有角色 */
export function getAllRolesApi() {
  return request<Role.ApiResponse<Role.RoleInfo[]>>({
    url: "/api/roles/getAllRoles",
    method: "get"
  })
}

/** 创建角色 */
export function createRoleApi(data: Role.RoleForm) {
  return request<Role.ApiResponse<string>>({
    url: "/api/roles",
    method: "post",
    data
  })
}

/** 更新角色 */
export function updateRoleApi(id: string, data: Role.RoleForm) {
  return request<Role.ApiResponse<string>>({
    url: `/api/roles/${id}`,
    method: "put",
    data
  })
}

/** 删除角色 */
export function deleteRoleApi(id: string) {
  return request<Role.ApiResponse<string>>({
    url: `/api/roles/${id}`,
    method: "delete"
  })
}
