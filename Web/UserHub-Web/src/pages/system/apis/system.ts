import { request } from "@/http/axios"
import type { System } from "./type"

/** 分页获取系统列表 */
export function getSystemListApi(params: System.SystemQuery) {
  return request<System.ApiResponse<System.PageResult<System.SystemInfo>>>({
    url: "/api/systemInfo/page",
    method: "get",
    params
  })
}

/** 获取所有系统 */
export function getAllSystemsApi() {
  return request<System.ApiResponse<System.SystemInfo[]>>({
    url: "/api/systemInfo/list",
    method: "get"
  })
}

/** 获取系统详情 */
export function getSystemByIdApi(id: number) {
  return request<System.ApiResponse<System.SystemInfo>>({
    url: `/api/systemInfo/${id}`,
    method: "get"
  })
}

/** 创建系统 */
export function createSystemApi(data: System.SystemForm) {
  return request<System.ApiResponse<System.SystemInfo>>({
    url: "/api/systemInfo",
    method: "post",
    data
  })
}

/** 更新系统 */
export function updateSystemApi(data: System.SystemForm) {
  return request<System.ApiResponse<System.SystemInfo>>({
    url: "/api/systemInfo",
    method: "put",
    data
  })
}

/** 删除系统 */
export function deleteSystemApi(id: string) {
  return request<System.ApiResponse<string>>({
    url: `/api/systemInfo/${id}`,
    method: "delete"
  })
}
