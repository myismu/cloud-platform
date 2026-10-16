import {request} from "@/http/axios"
import type { LoginHistory } from "./type"

// 获取登录历史列表
export const getLoginHistoryListApi = (params: LoginHistory.LoginHistoryQuery) => {
  return request<LoginHistory.ApiResponse<LoginHistory.PageResult<LoginHistory.LoginHistoryInfo>>>({
    url: "/api/login-history/page",
    method: "get",
    params
  })
}

// 查看登录历史详情
export const getLoginHistoryByIdApi = (id: string) => {
  return request<LoginHistory.ApiResponse<LoginHistory.LoginHistoryInfo>>({
    url: `/api/login-history/${id}`,
    method: "get"
  })
}

// 创建登录历史
export const createLoginHistoryApi = (data: LoginHistory.LoginHistoryForm) => {
  return request<LoginHistory.ApiResponse<null>>({
    url: "/api/login-history",
    method: "post",
    data
  })
}

// 更新登录历史
export const updateLoginHistoryApi = (id: string, data: LoginHistory.LoginHistoryForm) => {
  return request<LoginHistory.ApiResponse<null>>({
    url: `/api/login-history/${id}`,
    method: "put",
    data
  })
}

// 删除登录历史
export const deleteLoginHistoryApi = (id: string) => {
  return request<LoginHistory.ApiResponse<null>>({
    url: `/api/login-history/${id}`,
    method: "delete"
  })
}
