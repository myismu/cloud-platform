import type { RouteRecordRaw } from "vue-router"
import { routerConfig } from "@/router/config"
import { registerNavigationGuard } from "@/router/guard"
import { createRouter } from "vue-router"
import { flatMultiLevelRoutes } from "./helper"
import { L } from "vitest/dist/chunks/reporters.d.CfRkRKN2.js"

const Layouts = () => import("@/layouts/index.vue")

/**
 * @name 常驻路由
 * @description 除了 redirect/403/404/login 等隐藏页面，其他页面建议设置唯一的 Name 属性
 */
export const constantRoutes: RouteRecordRaw[] = [
  {
    path: "/redirect",
    component: Layouts,
    meta: {
      hidden: true
    },
    children: [
      {
        path: ":path(.*)",
        component: () => import("@/pages/redirect/index.vue")
      }
    ]
  },
  {
    path: "/403",
    component: () => import("@/pages/error/403.vue"),
    meta: {
      hidden: true
    }
  },
  {
    path: "/404",
    component: () => import("@/pages/error/404.vue"),
    meta: {
      hidden: true
    },
    alias: "/:pathMatch(.*)*"
  },
  {
    path: "/login",
    component: () => import("@/pages/login/index.vue"),
    meta: {
      hidden: true
    }
  },
  {
    path: "/",
    component: Layouts,
    redirect: "/dashboard",
    children: [
      {
        path: "dashboard",
        component: () => import("@/pages/dashboard/index.vue"),
        name: "Dashboard",
        meta: {
          title: "首页",
          svgIcon: "dashboard",
          affix: true
        }
      }
    ]
  },
  {
    path: "/user",
    component: Layouts,
    redirect: "/user",
    children: [
      {
        path: "user",
        component: () => import("@/pages/user/index.vue"),
        name: "User",
        meta: {
          title: "用户管理",
          svgIcon: "user",
          affix: true
        }
      }
    ]
  },
  {
    path: "/roles",
    component: Layouts,
    redirect: "/roles",
    children: [
      {
        path: "roles",
        component: () => import("@/pages/roles/index.vue"),
        name: "Roles",
        meta: {
          title: "角色管理",
          svgIcon: "role",
          affix: true
        }
      }
    ]
  },
  {
    path: "/routes",
    component: Layouts,
    redirect: "/routes",
    children: [
      {
        path: "routes",
        component: () => import("@/pages/routes/index.vue"),
        name: "Routes",
        meta: {
          title: "路由管理",
          svgIcon: "router",
          affix: true
        }
      }
    ]
  },
  {
    path: "/online-users",
    component: Layouts,
    redirect: "/online-users",
    children: [
      {
        path: "online-users",
        component: () => import("@/pages/online-users/index.vue"),
        name: "OnlineUsers",
        meta: {
          title: "在线用户",
          svgIcon: "online",
          affix: true
        }
      }
    ]
  },

  {
    path: "/login-history",
    component: Layouts,
    redirect: "/login-history",
    children: [
      {
        path: "login-history",
        component: () => import("@/pages/login-history/index.vue"),
        name: "LoginHistory",
        meta: {
          title: "登录历史",
          svgIcon: "login-history",
          affix: true
        }
      }
    ]
  },
  {
    path: "/password-history",
    component: Layouts,
    redirect: "/password-history",
    children: [
      {
        path: "password-history",
        component: () => import("@/pages/password-history/index.vue"),
        name: "PasswordHistory",
        meta: {
          title: "密码历史",
          svgIcon: "password",
          affix: true
        }
      }
    ]
  },
  {
  path: '/swagger',
  component:Layouts,
  redirect: '/Swagger',
  children: [{
        path: "swagger",
        component: () => import('@/pages/SwaggerView/index.vue'),
        name: "Swagger",
        meta: {
          title: "Swagger",
          svgIcon: "swagger",
          affix: true
        }
      }
    ]
},
 {
  path: '/systemInfo',
  component:Layouts,
  redirect: '/systemInfo',
  children: [{
        path: "systemInfo",
        component: () => import('@/pages/system/index.vue'),
        name: "SystemInfo",
        meta: {
          title: "系统管理",
          svgIcon: "system",
          affix: true
        }
      }
    ]
},


]

/**
 * @name 动态路由
 * @description 用来放置有权限 (Roles 属性) 的路由
 * @description 必须带有唯一的 Name 属性
 */
export const dynamicRoutes: RouteRecordRaw[] = [

]

/** 路由实例 */
export const router = createRouter({
  history: routerConfig.history,
  routes: routerConfig.thirdLevelRouteCache ? flatMultiLevelRoutes(constantRoutes) : constantRoutes
})

/** 重置路由 */
export function resetRouter() {
  try {
    // 注意：所有动态路由路由必须带有 Name 属性，否则可能会不能完全重置干净
    router.getRoutes().forEach((route) => {
      const { name, meta } = route
      if (name && meta.roles?.length) {
        router.hasRoute(name) && router.removeRoute(name)
      }
    })
  } catch {
    // 强制刷新浏览器也行，只是交互体验不是很好
    location.reload()
  }
}

// 注册路由导航守卫
registerNavigationGuard(router)
