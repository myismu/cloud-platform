import type { Route } from "./type"
import { request } from "@/http/axios"

/** 分页获取路由列表 */
export function getRouteListApi(params: Route.RouteQuery) {
  return request<Route.ApiResponse<Route.PageResult<Route.RouteInfo>>>({
    url: "/api/routes/page",
    method: "get",
    params
  })
}

/** 获取所有路由 */
export function getAllRoutesApi() {
  return request<Route.ApiResponse<Route.RouteInfo[]>>({
    url: "/api/routes",
    method: "get"
  })
}

/** 获取路由树 */
export function getRouteTreeApi() {
  return request<Route.ApiResponse<Route.RouteInfo[]>>({
    url: "/api/routes/tree",
    method: "get"
  })
}

/** 获取路由详情 */
export function getRouteByIdApi(id: string) {
  return request<Route.ApiResponse<Route.RouteInfo>>({
    url: `/api/routes/${id}`,
    method: "get"
  })
}

/** 创建路由 */
export function createRouteApi(data: Route.RouteForm) {
  return request<Route.ApiResponse<string>>({
    url: "/api/routes",
    method: "post",
    data
  })
}

/** 更新路由 */
export function updateRouteApi(id: string, data: Route.RouteForm) {
  return request<Route.ApiResponse<string>>({
    url: `/api/routes/${id}`,
    method: "put",
    data
  })
}

/** 删除路由 */
export function deleteRouteApi(id: string) {
  return request<Route.ApiResponse<string>>({
    url: `/api/routes/${id}`,
    method: "delete"
  })
}
