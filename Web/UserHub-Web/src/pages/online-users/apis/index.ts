import {request} from "@/http/axios"
import type { OnlineUser, ApiResponse } from "./type"

// 获取在线用户列表
export function getOnlineUsers() {
  return request<ApiResponse<OnlineUser[]>>({
    url: '/api/auth/online-users',
    method: 'get'
  })
}

// 踢出用户
export function kickOutUser(userId: string) {
  return request<ApiResponse<string>>({
    url: `/api/auth/logout`,
    method: 'get',
    params: {
      userId
    }
  })
}
