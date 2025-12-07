# 大学生心理健康系统 (Mental Health System)

## 项目简介

大学生心理健康系统是一个基于 Spring Boot + Vue 3 的全栈应用，旨在为大学生提供心理健康支持和咨询服务。系统支持多角色管理，包括管理员、咨询师和学生，提供心理咨询预约、心理测试、树洞分享、咨询师收藏、安全设置等功能。

## 技术栈

### 后端
- **框架**: Spring Boot 3.x
- **鉴权**: Sa-Token
- **数据库**: MySQL + MyBatis-Plus
- **工具**: Hutool, Lombok, FastJson2
- **语言**: Java 17+

### 前端
- **框架**: Vue 3 + Vite
- **UI 组件库**: Element Plus
- **状态管理**: Pinia
- **路由**: Vue Router 4
- **HTTP 客户端**: Axios
- **图标库**: Element Plus Icons

## 项目结构

```
MentalHealthSystem/
├── src/                    # 后端源码
│   ├── main/
│   │   ├── java/           # Java 源代码
│   │   └── resources/      # 资源文件 (配置, Mapper XML)
├── frontend/               # 前端源码
│   ├── src/
│   │   ├── api/            # API 接口
│   │   ├── components/     # 公共组件
│   │   ├── router/         # 路由配置
│   │   ├── stores/         # Pinia 状态管理
│   │   ├── utils/          # 工具类 (Request, etc.)
│   │   └── views/          # 页面视图
│   │       ├── auth/       # 认证页面 (登录)
│   │       ├── main/       # 主布局及通用页面 (首页, 个人中心, 安全设置, 树洞)
│   │       ├── student/    # 学生端页面 (预约, 测试, 收藏, 咨询师列表)
│   │       ├── counselor/  # 咨询师端页面 (预约管理, 问卷管理)
│   │       └── management/ # 管理员页面 (用户, 角色, 咨询师管理)
├── scheme.sql              # 数据库结构脚本
├── data.sql                # 数据库初始化数据脚本
├── pom.xml                 # Maven 依赖配置
└── README.md               # 项目文档
```

## 核心功能

### 1. 用户体系
- **多角色支持**: 管理员、咨询师、学生。
- **登录注册**: 基于 Sa-Token 的认证，支持密码加密存储。
- **个人中心**: 查看和编辑个人基本信息。
- **安全设置**: 
    - **修改头像**: 支持上传预览。
    - **修改密码**: 安全的密码修改流程。

### 2. 心理咨询
- **咨询师列表**: 学生可以浏览咨询师信息（擅长领域、评分、简介）。
- **预约服务**: 学生发起预约（线上/线下），咨询师进行确认或取消。
- **收藏功能**: 学生可以收藏心仪的咨询师，方便快速访问。

### 3. 心理测评
- **问卷系统**: 支持多种心理测试量表（如 SAS 焦虑自评、SDS 抑郁自评）。
- **在线作答**: 学生在线完成测试并自动评分。
- **问卷管理**: 咨询师可以发布和管理心理测试问卷。

### 4. 树洞社区
- **匿名分享**: 用户可以发布树洞帖子分享心情。
- **互动交流**: 支持对帖子进行回复和讨论。
- **内容审核**: 简单的审核机制（数据库字段支持）。

### 5. 系统管理
- **用户管理**: 管理员管理所有用户信息。
- **角色权限**: 基于 RBAC 的权限控制。

## 快速开始

### 环境要求

- JDK 17+
- Maven 3.6+
- MySQL 8.0+
- Node.js 16+
- npm 8+

### 数据库初始化

1. 创建数据库 `mental_health_system`。
2. 执行根目录下的 `scheme.sql` 创建表结构。
3. 执行根目录下的 `data.sql` 导入测试数据（包含默认管理员、咨询师和学生账号）。

### 后端启动

1. 修改 `src/main/resources/application.yml` 中的数据库连接配置（URL, username, password）。
2. 使用 Maven 构建并运行项目：
   ```bash
   mvn spring-boot:run
   ```
   或者在 IDE 中运行 `MentalHealthSystemApplication.java`。

### 前端启动

1. 进入前端目录：
   ```bash
   cd frontend
   ```
2. 安装依赖：
   ```bash
   npm install
   ```
3. 启动开发服务器：
   ```bash
   npm run dev
   ```
4. 访问 `http://localhost:5173`。

## 默认账号

| 角色 | 用户名 | 密码 | 说明 |
| --- | --- | --- | --- |
| 管理员 | admin | 12345678 | 系统超级管理员 |
| 咨询师 | counselor1 | 12345678 | 示例咨询师账号 |
| 学生 | student1 | 12345678 | 示例学生账号 |

## 开发日志

- **2025-12-07**: 
    - 完善安全设置功能，支持头像上传和密码修改。
    - 修复收藏夹显示问题，优化咨询师信息展示。
    - 更新数据库初始化脚本，提供完整的测试数据。

## API 接口文档

### 认证接口
- `POST /auth/login` - 用户登录
- `POST /auth/register` - 用户注册

### 用户接口
- `GET /user/info` - 获取当前用户信息
- `GET /user/{user_id}` - 获取用户详情
- `POST /user` - 创建用户（管理员）
- `PUT /user/{user_id}` - 更新用户信息
- `DELETE /user/{user_id}` - 删除用户（管理员）
- `GET /user/list` - 获取用户列表（管理员）

### 角色接口
- `GET /role/list` - 获取角色列表
- `GET /role/{id}` - 获取角色详情
- `POST /role` - 创建角色（管理员）
- `PUT /role/{id}` - 更新角色（管理员）
- `DELETE /role/{id}` - 删除角色（管理员）

### 权限接口
- `GET /ps/current` - 获取当前用户权限
- `GET /ps/{role_id}` - 获取角色权限
- `POST /ps/{role_id}` - 添加角色权限（管理员）
- `DELETE /ps/{role_id}` - 删除角色权限（管理员）
- `PUT /ps/{role_id}` - 更新角色权限（管理员）

### 文件接口
- `POST /file/upload-avatar` - 上传头像

### 咨询师接口
- `GET /counselor/list` - 获取咨询师列表
- `GET /counselor/{id}` - 获取咨询师详情
- `POST /counselor` - 添加咨询师
- `PUT /counselor/{id}` - 更新咨询师信息
- `DELETE /counselor/{id}` - 删除咨询师

### 预约接口
- `GET /appointment/list` - 获取预约列表
- `GET /appointment/{id}` - 获取预约详情
- `POST /appointment` - 创建预约
- `PUT /appointment/{id}` - 更新预约状态
- `DELETE /appointment/{id}` - 取消预约

### 心理测试接口
- `GET /test/list` - 获取测试列表
- `GET /test/{id}` - 获取测试详情
- `POST /test` - 创建测试（管理员）
- `POST /test/answer` - 提交测试答案

### 树洞接口
- `GET /treehole/list` - 获取树洞列表
- `GET /treehole/{id}` - 获取树洞详情
- `POST /treehole` - 发布树洞
- `POST /treehole/{id}/reply` - 回复树洞

### 收藏接口
- `GET /favourite/list` - 获取收藏列表
- `POST /favourite` - 添加收藏
- `DELETE /favourite/{id}` - 取消收藏

## 开发规范

### 代码风格
- 后端遵循阿里巴巴 Java 开发手册
- 前端使用 ESLint 和 Prettier 统一代码风格
- 所有代码需添加适当的中文注释

### 提交规范
- feat: 新功能
- fix: 修复 bug
- docs: 文档更新
- style: 代码格式调整
- refactor: 代码重构
- test: 测试相关
- chore: 构建/工具链相关

## 部署说明

### 后端部署
```bash
mvn clean package
java -jar target/MentalHealthSystem-0.0.1-SNAPSHOT.jar
```

### 前端部署
```bash
cd frontend
npm run build
# 将 dist 目录部署到 Web 服务器
```

## 常见问题

### 1. 数据库连接失败
检查 MySQL 服务是否启动，数据库配置是否正确。

### 2. Redis 连接失败
检查 Redis 服务是否启动，端口是否正确。

### 3. 前端跨域问题
开发环境已配置代理，生产环境需在后端添加 CORS 配置。

### 4. Token 失效
Token 存储在 localStorage，清除浏览器缓存可重置登录状态。

## 贡献指南

欢迎提交 Issue 和 Pull Request！

1. Fork 本仓库
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 开启 Pull Request

## 许可证

本项目采用 MIT 许可证。

## 联系方式

如有问题，请提交 Issue 或联系开发团队。
