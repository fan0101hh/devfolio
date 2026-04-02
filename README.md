# DevFolio

`DevFolio` 是一个前后端分离的个人主页与技术博客项目，技术栈为 `Spring Boot 3 + Vue 3`。

## 项目结构

```text
devfolio/
├── backend/      # Spring Boot 3 API
├── frontend/     # Vue 3 + Vite 前端
└── docker-compose.yml
```

## 当前规划

- 首页：个人简介、技术栈、精选项目
- 项目页：项目列表与详情
- 文章页：技术文章列表与详情
- 关于页：个人经历、联系方式、外链
- 管理端能力：通过后端 API 支持登录、文章/项目/标签管理

## 本地启动

### 1. 启动后端

```bash
cd backend
mvn spring-boot:run
```

默认地址：`http://localhost:8080/api/health`

后端公开接口：

- `GET /api/public/articles`
- `GET /api/public/articles/{slug}`
- `GET /api/public/projects`
- `GET /api/public/projects/{slug}`
- `GET /api/public/tags`

后端管理接口：

- `POST /api/admin/auth/login`
- `GET/POST/PUT/DELETE /api/admin/articles`
- `GET/POST/PUT/DELETE /api/admin/projects`
- `GET/POST/PUT/DELETE /api/admin/tags`

### 2. 启动前端

```bash
cd frontend
npm install
npm run dev
```

默认地址：`http://localhost:5173`

## Docker Compose

当前仓库已包含：

- `mysql:8.4`
- `Spring Boot 3 backend`
- `Vue 3 frontend`

启动前请先根据需要修改 `docker-compose.yml` 中的数据库和管理员默认密码。

## 下一步建议

1. 为前端接入公开接口和管理员登录流程
2. 增加后台管理页，支持文章、项目、标签的新增与编辑
3. 为公开列表补充分页、筛选和搜索
4. 增加初始化数据和接口测试
