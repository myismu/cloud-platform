export namespace PasswordHistory {
  // 密码历史查询参数
  export interface PasswordHistoryQuery {
    current: number
    size: number
    userId?: string
    changeReason?: string
    changeTime?: [string, string]
  }

  // 密码历史信息
  export interface PasswordHistoryInfo {
    id: string
    userId: string
    password: string
    changeTime: string
    changeReason: string
    createTime: string
    updateTime: string
  }

  // 密码历史表单
  export interface PasswordHistoryForm {
    id?: string
    userId: string
    password: string
    changeTime: string
    changeReason: string
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