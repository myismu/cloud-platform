# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## 仓库定位

平台仓库：为 AI-PPT 与 QuestionBank 两个业务系统提供共用的基础设施（用户中心、文件服务、统一网关、公共库与契约库）。

本仓库是这两个组件唯一的源码位置 —— 业务系统通过 **接口调用**（部署的服务）与 **Maven 制品**（common / api）使用它们，不再复制代码。

## 模块结构

| 模块 | 类型 | 职责 |
|---|---|---|
| `common` | 库（发布制品） | 工具类、统一返回、全局异常、登录头过滤器、JwtUtil |
| `api` | 契约库（发布制品） | 用户服务的 Feign 客户端与 DTO |
| `gateway` | 部署件 | 统一网关：两个业务系统的全部路由集中在此 |
| `service/user-hub-service` | 部署件 | 用户中心：认证、用户/角色/路由管理 |
| `service/file-service` | 部署件 | 文件服务：上传、静态资源（/img、/image、/templates、/projects） |
| `Web/UserHub-Web` | 前端 | 用户中心界面（端口 3333） |

## 配置与依赖

- 运行时配置全部来自 **Nacos**（开发命名空间 `a1e2b3c4-d5e6-7f8a-9b0c-1d2e3f4a5b6c`），本地 `application-dev.yml` 只保留端口与 Nacos 地址。
- 配置模板在 `naocs/nacos-dev/`：`gateway.yaml`、`user-hub-service.yaml`、`file-service.yaml`。仓库内敏感值均为占位符，真实值通过环境变量注入。
- 中间件：MySQL（`user_hub` 库）、Redis、Nacos；SQL 脚本在 `SQL/user_hub.sql`。
- 网关路由是**平台资产**：业务系统新增接口路由时，修改 `naocs/nacos-dev/gateway.yaml` 并同步到 Nacos。

## 构建与启动

```bash
mvn clean install                       # 全量构建（含单元测试）
# 启动顺序：gateway → user-hub-service → file-service
```

- 全量容器化启动：`docker-compose up -d --build`
- 前端：`cd Web/UserHub-Web && pnpm install && pnpm dev`

## 制品发布（common / api）

1. 修改代码后，把所有 pom 中的版本号从 `1.0.0` 升为 `1.0.1`
2. 提交推送
3. 在 GitHub Actions 页面**手动运行 Release 流水线**（GitHub Packages 同版本不可覆盖）
4. 业务仓库按需把依赖版本号升级

## 关键机制

- **网关鉴权**：`gateway` 的 `AuthFilter` 校验 JWT（`common/JwtUtil`），并检查 Redis 在线状态（`online:{systemCode}:{userId}` 必须存在且与 token 一致）。
- **JWT 密钥**：`user-hub-service` 签发、`gateway` 校验，两边共用 Nacos 下发的 `jwt.secret`，修改必须同步。
- **静态资源**：file-service 统一托管 `/img`（用户上传）、`/image`（AI 生成）、`/templates`（PPT 模板素材，随仓库跟踪）、`/projects`（PPT 项目文件）。
- **契约库**：调用方使用用户中心时，应依赖 `api` 模块的 `UserHubClient`，不要自行手写 Feign 接口。

## 安全约定

- 不提交真实密钥、密码；配置走环境变量或 Nacos 私有配置。
- 新增配置项时同步更新 `naocs/nacos-dev/` 模板与 README。
