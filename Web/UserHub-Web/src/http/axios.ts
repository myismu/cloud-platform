import type { AxiosInstance, AxiosRequestConfig } from "axios"
import { useUserStore } from "@/pinia/stores/user"
import { getToken } from "@@/utils/cache/cookies"
import axios from "axios"
import { get, merge } from "lodash-es"

/** 退出登录并强制刷新页面（会重定向到登录页） */
function logout() {
  useUserStore().logout()
  location.reload()
}

/** 创建请求实例 */
function createInstance() {
  // 创建一个 axios 实例命名为 instance
  const instance = axios.create()
  // 请求拦截器
  instance.interceptors.request.use(
    // 发送之前
    config => config,
    // 发送失败
    error => Promise.reject(error)
  )
  // 响应拦截器（可根据具体业务作出相应的调整）
  instance.interceptors.response.use(
    (response) => {
      // apiData 是 api 返回的数据
      const apiData = response.data
      // 二进制数据则直接返回
      const responseType = response.request?.responseType
      if (responseType === "blob" || responseType === "arraybuffer") return apiData
      // 这个 code 是和后端约定的业务 code
      const code = apiData.code
      console.log("响应拦截器:", apiData.data)
      // 如果没有 code, 代表在过滤层直接返回了，没有进入mvc层
      if (code === undefined) {
        // ElMessage.error("非本系统的接口")
        return Promise.reject(new Error(apiData))
      }
      switch (code) {
        case 200:
          // 本系统采用 code === 200 来表示没有业务错误
          return apiData
        case 401:
          // Token 过期时
            ElMessage({
            message: "登录过期，请重新登录",
            type: 'warning',
          })
          return logout()
        case 444:
          ElMessage({
            message: apiData.message,
            type: 'warning',
          })
          break
        default:
          // 不是正确的 code
          ElMessage.error(apiData.message || "Error")
          return Promise.reject(new Error("Error"))
      }
    },
    (error) => {
      // ==================== 网络异常兜底处理 ====================
      // 当网关崩溃、网络断开或服务器无响应时，error.response 为 undefined
      if (!error.response) {
        error.message = "哎呀，连接不上服务器了，请检查您的网络连接后再试～"
        ElMessage.error(error.message)
        return Promise.reject(error)
      }

      // status 是 HTTP 状态码
      const status = get(error, "response.status")
      const message = get(error, "response.data.message")
      switch (status) {
        case 400:
          error.message = "提交的信息有误，请检查后重试"
          break
        case 401:
          error.message = message || "登录已过期，请重新登录"
          logout()
          break
        case 403:
          error.message = message || "您没有权限执行此操作"
          break
        case 404:
          error.message = "您访问的页面不存在"
          break
        case 408:
          error.message = "请求时间太长了，请稍后再试"
          break
        case 500:
          error.message = "服务器开小差了，请稍后再试"
          break
        case 501:
          error.message = "该功能暂未开放，敬请期待"
          break
        case 502:
          error.message = "服务器正在维护中，请稍后再试"
          break
        case 503:
          error.message = "当前访问人数较多，请稍后再试"
          break
        case 504:
          error.message = "服务器响应超时，请稍后再试"
          break
        default:
          error.message = "出了点问题，请稍后再试"
          break
      }
      ElMessage.error(error.message)
      return Promise.reject(error)
    }
  )
  return instance
}

/** 创建请求方法 */
function createRequest(instance: AxiosInstance) {
  return <T>(config: AxiosRequestConfig): Promise<T> => {
    const token = getToken()
    // 默认配置
    const defaultConfig: AxiosRequestConfig = {
      // 接口地址
      baseURL: import.meta.env.VITE_BASE_URL,
      // 请求头
      headers: {
        // 携带 Token
        "Authorization": token ? `Bearer ${token}` : undefined,
        "Content-Type": "application/json"
      },
      // 请求体
      data: {},
      // 请求超时
      timeout: 5000,
      // 跨域请求时是否携带 Cookies
      withCredentials: false
    }
    // 将默认配置 defaultConfig 和传入的自定义配置 config 进行合并成为 mergeConfig
    const mergeConfig = merge(defaultConfig, config)
    return instance(mergeConfig)
  }
}

/** 用于请求的实例 */
const instance = createInstance()

/** 用于请求的方法 */
export const request = createRequest(instance)
