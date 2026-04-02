# CLAUDE.md

本文件为 Claude Code 提供项目上下文，每次对话自动加载。

## 项目简介

DevFolio — 前后端分离的个人主页 + 技术博客系统。

## 技术栈

- **后端**: Java 17, Spring Boot 3.3, Spring Security, Spring Data JPA, MySQL 8.4, jjwt 0.12
- **前端**: Vue 3, Vue Router 4, Vite 6
- **部署**: Docker Compose (mysql + backend + frontend/nginx)

## 目录结构

```
devfolio/
├── backend/                          # Spring Boot API
│   ├── pom.xml
│   └── src/main/java/com/devfolio/backend/
│       ├── DevfolioBackendApplication.java
│       ├── HealthController.java
│       ├── config/                   # SecurityConfig, JwtAuthenticationFilter, AppSecurityProperties
│       ├── auth/                     # AuthService, JwtService, dto/
│       ├── common/                   # BaseEntity, ContentStatus, exceptions, GlobalExceptionHandler
│       ├── tag/                      # Tag entity, repo, service, mapper, dto/
│       ├── article/                  # Article entity, repo, service, mapper, dto/
│       ├── project/                  # Project entity, repo, service, mapper, dto/
│       ├── publicapi/                # PublicContentController (GET only)
│       └── admin/                    # AdminAuth/Article/Project/TagController
├── frontend/                         # Vue 3 SPA
│   ├── package.json
│   ├── vite.config.js                # dev proxy → localhost:8080
│   ├── nginx.conf                    # production reverse proxy
│   ├── Dockerfile                    # multi-stage: node build → nginx
│   └── src/
│       ├── main.js
│       ├── App.vue
│       ├── router/index.js
│       ├── lib/api.js                # 统一 fetch 封装 + 所有 API 函数
│       ├── lib/auth.js               # JWT token 管理 (localStorage)
│       ├── views/                    # 所有页面组件
│       └── assets/main.css           # 全局样式（无 scoped）
├── docker-compose.yml
├── .env.example
└── CLAUDE.md
```

## API 路径约定

- 公开接口: `GET /api/public/{articles,projects,tags}[/{slug}]`
- 管理接口: `/api/admin/{auth,articles,projects,tags}` (需 JWT Bearer token, ROLE_ADMIN)
- 健康检查: `GET /api/health`

## 开发命令

```bash
# 后端
cd backend && mvn spring-boot:run        # 启动 (需要本地 MySQL)
cd backend && mvn compile                # 编译检查
cd backend && mvn test                   # 运行测试

# 前端
cd frontend && npm install               # 安装依赖
cd frontend && npm run dev               # 开发服务器 (localhost:5173, proxy → 8080)
cd frontend && npm run build             # 生产构建 → dist/

# Docker
docker compose up --build                # 启动全部服务
```

## 编码规范

### 后端
- 实体继承 `BaseEntity`（id, createdAt, updatedAt 自动管理）
- DTO 用 Java record，validation 注解在 record 参数上
- Mapper 为无状态工具类（private constructor + static methods）
- Service 层标注 `@Transactional`，读操作加 `readOnly = true`
- Slug 格式: `^[a-z0-9]+(?:-[a-z0-9]+)*$`，通过 `SlugValidator.requireValid()` 校验
- 唯一性校验用 Repository 的 `existsByXxxAndIdNot()` 方法，不要用 `findAll()` 遍历
- 异常统一由 `GlobalExceptionHandler` 处理，不要在 Controller 里 try-catch
- 密码用 BCrypt（配置文件中存 hash），JWT secret 最少 32 字节

### 前端
- API 调用统一走 `lib/api.js` 的 `request()` 函数
- Token 管理统一走 `lib/auth.js`
- 路由守卫: `meta.requiresAuth` 跳登录, `meta.guestOnly` 跳管理页
- 401 响应时自动清 token 并跳转登录页
- 全局样式在 `main.css`，不使用 scoped style
- 日期格式化用 `Intl.DateTimeFormat('zh-CN', ...)`

## 注意事项

- `application.yml` 中的 `admin-password` 是 BCrypt hash，默认对应明文 `change-me-now`
- `docker-compose.yml` 中 `$` 需要写成 `$$` 来转义
- 前端 Dockerfile 是 nginx 生产构建，不是 dev server
- `ddl-auto: update` 仅用于开发，生产应改为 `validate` 配合 Flyway/Liquibase
