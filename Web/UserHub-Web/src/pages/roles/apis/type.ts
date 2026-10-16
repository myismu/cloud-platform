export namespace Role {
  // 角色查询参数
  export interface RoleQuery {
    current: number
    size: number
    roleName?: string
    description?: string
    status?: number
  }

  // 角色信息
  export interface RoleInfo {
    id: string
    roleName: string
    roleCode: string
    description: string
    status: number
    createdAt: string
    updatedAt: string
  }

  // 角色表单
  export interface RoleForm {
    id?: string
    roleName: string
    roleCode: string
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
