import { request } from "@/http/axios"
import type { User } from "./type"

/** 分页获取用户列表 */
export function getUserListApi(params: User.UserQuery) {
  return request<User.ApiResponse<User.PageResult<User.UserInfo>>>({
    url: "/api/users/page",
    method: "get",
    params
  })
}

/** 获取所有用户 */
export function getAllUsersApi() {
  return request<User.ApiResponse<User.UserInfo[]>>({
    url: "/api/users/getAllUsers",
    method: "get"
  })
}

/** 创建用户 */
export function createUserApi(data: User.UserForm) {
  return request<User.ApiResponse<string>>({
    url: "/api/users/",
    method: "post",
    data
  })
}

/** 更新用户 */
export function updateUserApi(id: number, data: User.UserForm) {
  return request<User.ApiResponse<string>>({
    url: `/api/users/${id}`,
    method: "put",
    data
  })
}

/** 删除用户 */
export function deleteUserApi(id: number) {
  return request<User.ApiResponse<string>>({
    url: `/api/users/${id}`,
    method: "delete"
  })
} 