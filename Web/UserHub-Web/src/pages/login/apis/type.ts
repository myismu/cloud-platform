// 请求前对方法的参数类型定义
export interface LoginRequestData {
  /** admin 或 editor */
  username: string
  /** 密码 */
  password: string
  // 系统代码
  systemCode: string
}
// 请求后对响应结果的类型定义：
export type CaptchaResponseData = ApiResponseData<string>

export type LoginResponseData = ApiResponseData<{ token: string }>


