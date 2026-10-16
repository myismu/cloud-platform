export namespace Route {
  // 路由查询参数
  export interface RouteQuery {
    current: number
    size: number
    path?: string
    name?: string
    status?: number
  }

  // 路由信息
  export interface RouteInfo {
    id: string
    path: string
    name: string
    component: string
    parentId?: string
    icon?: string
    sort?: number
    status: number
    description?: string
    createTime: string
    updateTime: string
    children?: RouteInfo[]
  }

  // 路由表单
  export interface RouteForm {
    id?: string
    path: string
    name: string
    component: string
    parentId?: string
    icon?: string
    sort?: number
    status: number
    description?: string
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

