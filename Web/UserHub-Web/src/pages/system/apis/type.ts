export namespace System {
  export interface SystemInfo {
    id: string
    code: string
    name: string
    description: string
    status: number
    createTime: string
    updateTime: string
  }

  export interface SystemQuery {
    current: number
    size: number
    name?: string
    code?: string
  }

  export interface SystemForm {
    id?: string
    code: string
    name: string
    description: string
    status: number
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
