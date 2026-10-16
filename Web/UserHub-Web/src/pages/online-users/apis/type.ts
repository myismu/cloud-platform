export interface OnlineUser {
  userId: string
  username: string
  ip: string
  userAgent: string
  loginTime: string
  clientType: string
  token: string
}

export interface ApiResponse<T> {
  code: number
  data: T
  message: string
}
