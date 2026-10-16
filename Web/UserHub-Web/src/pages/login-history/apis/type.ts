export namespace LoginHistory {
  // 登录历史查询参数
  export interface LoginHistoryQuery {
    current: number
    size: number
    userId?: string
    username?: string
    ipAddress?: string
    clientType?: string
    timeRange?: [string, string]
  }

  // 登录历史信息
  export interface LoginHistoryInfo {
    id: string
    userId: string
    username: string
    loginTime: string
    ipAddress: string
    userAgent: string
    clientType: string
    createTime: string
    updateTime: string
  }

  // 登录历史表单
  export interface LoginHistoryForm {
    id?: string
    userId: string
    username: string
    loginTime: string
    ipAddress: string
    userAgent: string
    clientType: string
  }

  export interface ApiResponse<T> {
    code: number
    message: string
    data: T
  }

  export interface PageResult<T> {
    records: T[]
    total: number
    size: number
    current: number
  }
} 