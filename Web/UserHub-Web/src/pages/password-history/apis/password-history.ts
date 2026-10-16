import {request} from "@/http/axios"
import type { PasswordHistory } from "./type"

// 获取密码历史列表
export const getPasswordHistoryListApi = (params: PasswordHistory.PasswordHistoryQuery) => {
  return request<PasswordHistory.ApiResponse<PasswordHistory.PageResult<PasswordHistory.PasswordHistoryInfo>>>({
    url: "/api/password-history/page",
    method: "get",
    params
  })
}

// 获取密码历史详情
export const getPasswordHistoryByIdApi = (id: string) => {
  return request<PasswordHistory.ApiResponse<PasswordHistory.PasswordHistoryInfo>>({
    url: `/api/password-history/${id}`,
    method: "get"
  })
}

// 创建密码历史
export const createPasswordHistoryApi = (data: PasswordHistory.PasswordHistoryForm) => {
  return request<PasswordHistory.ApiResponse<null>>({
    url: "/api/password-history",
    method: "post",
    data
  })
}

// 更新密码历史
export const updatePasswordHistoryApi = (id: string, data: PasswordHistory.PasswordHistoryForm) => {
  return request<PasswordHistory.ApiResponse<null>>({
    url: `/api/password-history/${id}`,
    method: "put",
    data
  })
}

// 删除密码历史
export const deletePasswordHistoryApi = (id: string) => {
  return request<PasswordHistory.ApiResponse<null>>({
    url: `/api/password-history/${id}`,
    method: "delete"
  })
}
