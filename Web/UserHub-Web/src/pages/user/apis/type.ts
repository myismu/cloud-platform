export namespace User {
  export interface UserInfo {
    id: number
    username: string
    email: string
    phoneNumber: string
    isLocked: number
    createdAt: string
    updatedAt: string
  }

  export interface UserQuery {
    current: number
    size: number
    username?: string
    email?: string
    phoneNumber?: string
    isLocked?: number
  }

  export interface UserForm {
    id?: number
    username: string
    password?: string
    email: string
    phoneNumber: string
    isLocked: number
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
