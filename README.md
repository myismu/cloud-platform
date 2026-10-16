# cloud-platform（平台仓库）

[![CI](https://github.com/myismu/cloud-platform/actions/workflows/ci.yml/badge.svg)](https://github.com/myismu/cloud-platform/actions/workflows/ci.yml)

AI-PPT 与 QuestionBank 两个业务系统共用的平台基础设施：用户中心、文件服务、统一网关、公共库与契约库。

业务系统通过**接口调用**使用本仓库部署的服务，通过**Maven 制品**使用本仓库的库 —— 不再复制代码。

## 模块与发布物

| 模块 | 类型 | 说明 | 制品 |
|---|---|---|---|
| `common` | 库 | 工具类、统一返回、异常处理、登录头过滤器 | 发布到 GitHub Packages |
| `api` | 契约库 | 用户服务的 Feign 客户端与 DTO | 发布到 GitHub Packages |
| `gateway` | 部署件 | 统一网关（两个业务系统的路由都在这里） | 容器镜像 |
| `service/user-hub-service` | 部署件 | 用户中心：认证、用户/角色管理 | 容器镜像 |
| `service/file-service` | 部署件 | 文件服务：上传、静态资源（/img、/image、/templates、/projects） | 容器镜像 |
| `Web/UserHub-Web` | 前端 | 用户中心界面 | 静态站点镜像 |

## 业务系统如何依赖本项目

业务仓库的 `pom.xml`（父工程 + 库依赖，都从 GitHub Packages 拉取）：

```xml
<parent>
    <groupId>com.exambaby</groupId>
    <artifactId>cloud</artifactId>
    <version>1.0.0</version>
</parent>

<dependency>
    <groupId>com.exambaby</groupId>
    <artifactId>common</artifactId>
    <version>1.0.0</version>
</dependency>
```

本地开发需要在 `~/.m2/settings.xml` 声明制品仓库与凭据（GitHub 的 Maven 注册表**始终要求认证**；用户名填 GitHub 账号，密码填具备 `read:packages` 权限的令牌）：

```xml
<servers>
    <server>
        <id>github</id>
        <username>你的GitHub用户名</username>
        <password>你的Token（需要 read:packages 权限）</password>
    </server>
</servers>

<profiles>
    <profile>
        <id>github-packages</id>
        <repositories>
            <repository>
                <id>github</id>
                <url>https://maven.pkg.github.com/myismu/cloud-platform</url>
            </repository>
        </repositories>
    </profile>
</profiles>
<activeProfiles>
    <activeProfile>github-packages</activeProfile>
</activeProfiles>
```

## 发布新版本

1. 修改 `common` / `api` 代码
2. 把所有 pom 中的版本号从 `1.0.0` 升为 `1.0.1`
3. 提交推送
4. 到仓库 Actions 页面手动运行 **Release** 流水线（GitHub Packages 同版本不可覆盖，必须先升版本号）
5. 需要升级的业务仓库：把 `pom.xml` 中的版本号改为 `1.0.1`（不升级的项目继续用旧版，不受影响）

## 服务端口与本地启动

| 服务 | 端口 |
|---|---|
| gateway | 8080 |
| user-hub-service | 8001 |
| file-service | 8003 |
| UserHub-Web（本地 dev） | 3333 |

- 全量启动：`docker-compose up -d --build`（中间件 + 网关 + 用户中心 + 文件服务 + 前端）
- 本地开发：先起中间件（MySQL/Redis/Nacos），再按 网关 → 用户中心 → 文件服务 顺序启动各启动类
- 构建：`mvn clean install`

## 配置说明

- 运行时配置来自 Nacos（`naocs/nacos-dev/` 下为导入模板：`gateway.yaml`、`user-hub-service.yaml`、`file-service.yaml`）
- 仓库内敏感值均为占位符（`${MYSQL_PWD:}`、`${JWT_SECRET:}`），真实值通过环境变量注入
- 数据库脚本：`SQL/user_hub.sql`

## 内置模板素材

`service/file-service/upload/templates/` 为 PPT 模板库（AI-PPT 业务使用），作为文件服务的内置素材随本仓库跟踪；其余上传目录（img/image/projects）为运行时产物，不进入版本控制。
