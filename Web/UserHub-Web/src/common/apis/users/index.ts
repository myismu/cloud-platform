import type * as Users from "./type"
import { request } from "@/http/axios"

/** 获取当前登录用户详情 */
export function getCurrentUserApi() {
  return request<Users.CurrentUserResponseData>({
    url: "api/auth/getUserInfo",
    method: "get"
  })
}
//退出登录
export function OutLogin() {
  return request<Users.OutLogin>({
    url: "api/auth/OutLogin",
    method: "get",
  })
}
