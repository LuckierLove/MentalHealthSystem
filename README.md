# 大学生心理健康系统 (Mental Health System)

## 项目简介

大学生心理健康系统是一个基于 Spring Boot + Vue 3 的全栈应用，旨在为大学生提供心理健康支持和咨询服务。系统支持多角色管理，包括管理员、咨询师和学生，提供心理咨询预约、心理测试、树洞分享等功能。

## 技术栈

### 后端
- **框架**: Spring Boot 3.5.8
- **鉴权**: Sa-Token 1.44.0
- **数据库**: MySQL + MyBatis-Plus 3.5.14
- **缓存**: Redis
- **语言**: Java 17

### 前端
- **框架**: Vue 3 + Vite
- **UI 组件库**: Element Plus
- **状态管理**: Pinia
- **路由**: Vue Router 4
- **HTTP 客户端**: Axios

## 项目结构

```
MentalHealthSystem/
├── src/                    # 后端源码
│   ├── main/
│   │   ├── java/
│   │   │   └── dev/huangzutong/mentalhealthsystem/
│   │   │       ├── controller/    # 控制器层
│   │   │       ├── service/       # 服务层
│   │   │       ├── mapper/        # 数据访问层
│   │   │       ├── entity/        # 实体类
│   │   │       ├── common/        # 公共类
│   │   │       └── config/        # 配置类
│   │   └── resources/
│   │       └── application.yml    # 应用配置
│   └── test/                      # 测试代码
├── frontend/                      # 前端源码
│   ├── src/
│   │   ├── api/                   # API 接口封装
│   │   ├── components/            # 公共组件
│   │   ├── router/                # 路由配置
│   │   ├── stores/                # 状态管理
│   │   ├── utils/                 # 工具函数
│   │   └── views/                 # 页面组件
│   ├── package.json               # 前端依赖
│   └── README.md                  # 前端文档
├── pom.xml                        # 后端依赖
└── README.md                      # 项目文档

```

## 核心功能

### 用户角色
- **管理员**: 用户管理、角色管理、权限管理
- **咨询师**: 预约管理、咨询记录管理
- **学生**: 预约咨询、心理测试、树洞分享、收藏咨询师

### 主要模块
1. **用户认证**: 基于 Sa-Token 的登录注册和权限控制
2. **咨询预约**: 学生预约咨询师，咨询师管理预约
3. **心理测试**: 在线心理健康测评系统
4. **树洞功能**: 匿名分享和交流平台
5. **角色权限**: 灵活的角色权限管理系统

## 快速开始

### 环境要求

- JDK 17+
- Maven 3.6+
- MySQL 8.0+
- Redis 6.0+
- Node.js 16+
- npm 8+

### 后端启动

1. 创建数据库
```sql
CREATE DATABASE mental_health_system CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

2. 修改配置文件
```yaml
# src/main/resources/application.yml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/mental_health_system
    username: your_username
    password: your_password
  data:
    redis:
      host: localhost
      port: 6379
```

3. 启动后端服务
```bash
mvn spring-boot:run
```

后端服务将运行在 `http://localhost:8080`

### 前端启动

1. 进入前端目录
```bash
cd frontend
```

2. 安装依赖
```bash
npm install
```

3. 启动开发服务器
```bash
npm run dev
```

前端应用将运行在 `http://localhost:5173`

4. 构建生产版本
```bash
npm run build
```

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

## 前端页面

### 登录页面
![登录页面](https://github.com/user-attachments/assets/c230f76a-9778-4d48-b1a7-733ae54ce55c)

### 注册对话框
![注册对话框](https://github.com/user-attachments/assets/136c97da-9fdd-4384-98d5-ebfd9f4466a9)

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
