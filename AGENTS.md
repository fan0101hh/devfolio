# AGENTS.md

本文件为 Claude Code 子代理提供指引。

## 通用规则

- 代码语言: 后端 Java 17, 前端 JavaScript (Vue 3 SFC)
- 提交信息和代码注释使用中文
- 错误提示面向终端用户，使用中文
- 不要引入新依赖，除非任务明确要求
- 修改后端代码后运行 `mvn compile` 验证
- 修改前端代码后运行 `npm run build` 验证

## 后端开发

- 项目根: `backend/`
- 包结构: `com.devfolio.backend.{module}`，每个模块包含 Entity、Repository、Service、Mapper、dto/、Controller
- 新增实体必须继承 `BaseEntity`
- DTO 用 record，字段校验用 Jakarta Validation 注解
- Service 方法必须加 `@Transactional`
- 唯一性校验用 `existsByXxxAndIdNot()` 而非全表遍历
- 公开 API 放 `publicapi/PublicContentController`，管理 API 放 `admin/` 包
- 新增 API 路径后检查 `SecurityConfig` 的授权规则

## 前端开发

- 项目根: `frontend/`
- 新增页面: 在 `src/views/` 下创建 `.vue` 文件，在 `router/index.js` 注册路由
- 新增 API 调用: 在 `lib/api.js` 中添加函数，使用 `request()` 封装
- 管理页面路由必须加 `meta: { requiresAuth: true }`
- 样式写在 `assets/main.css`，遵循现有命名约定（BEM-like: `.page-section`, `.content-card`, `.form-stack`）
- 不使用 scoped style

## 测试

- 后端测试: `backend/src/test/java/`，用 JUnit 5 + Spring Boot Test
- 运行: `cd backend && mvn test`
- 前端暂无测试框架
