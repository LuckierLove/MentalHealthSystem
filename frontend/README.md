# 大学生心理健康系统 - 前端

## 项目简介

本项目是大学生心理健康系统的前端部分，基于 Vue 3 + Vite + Element Plus 构建。系统提供心理咨询预约、心理测试、树洞分享等功能，支持管理员、咨询师、学生三种角色。

## 技术栈

- **框架**: Vue 3 (Composition API)
- **构建工具**: Vite
- **UI 组件库**: Element Plus
- **状态管理**: Pinia
- **路由**: Vue Router 4
- **HTTP 客户端**: Axios
- **语言**: JavaScript

## 项目结构

```
frontend/
├── src/
│   ├── api/              # API 接口封装
│   │   ├── auth.js       # 认证相关接口
│   │   ├── user.js       # 用户相关接口
│   │   ├── role.js       # 角色相关接口
│   │   ├── permission.js # 权限相关接口
│   │   └── file.js       # 文件上传接口
│   ├── components/       # 公共组件
│   │   └── common/       # 通用组件
│   │       └── RegisterDialog.vue  # 注册对话框
│   ├── router/           # 路由配置
│   │   └── index.js      # 路由主文件
│   ├── stores/           # Pinia 状态管理
│   │   └── user.js       # 用户状态管理
│   ├── utils/            # 工具函数
│   │   └── request.js    # Axios 封装
│   ├── views/            # 页面组件
│   │   ├── auth/         # 认证相关页面
│   │   │   └── LoginView.vue  # 登录页面
│   │   └── main/         # 主页面
│   │       ├── MainLayout.vue # 主布局
│   │       └── Home.vue       # 首页
│   ├── App.vue           # 根组件
│   └── main.js           # 应用入口
├── public/               # 静态资源
├── index.html            # HTML 模板
├── vite.config.js        # Vite 配置
├── package.json          # 依赖配置
└── README.md            # 项目文档
```

## 核心功能

### 1. 用户认证
- **登录**: 用户名/密码登录，成功后获取 token
- **注册**: 支持用户信息填写和头像上传
- **Token 管理**: 自动在请求头添加 Authorization token

### 2. 角色权限
系统支持三种角色，各角色菜单动态显示：

- **管理员**: 
  - 用户管理
  - 角色管理
  - 权限管理

- **咨询师**:
  - 预约管理
  - 咨询记录

- **学生**:
  - 我的预约
  - 心理测试
  - 心情树洞

### 3. 主界面布局
- 左侧导航栏：根据角色动态渲染菜单
- 顶部栏：用户信息和操作菜单
- 内容区：页面内容展示

## 环境要求

- Node.js >= 16.0.0
- npm >= 8.0.0

## 安装依赖

```bash
cd frontend
npm install
```

## 开发运行

```bash
npm run dev
```

访问地址: http://localhost:5173

## 构建生产

```bash
npm run build
```

## 预览生产构建

```bash
npm run preview
```

## 后端对接说明

### 后端地址配置

后端接口基础地址配置在 `src/utils/request.js` 中：

```javascript
baseURL: 'http://localhost:8080'
```

### 主要接口列表

#### 认证接口
- `POST /auth/login` - 用户登录
- `POST /auth/register` - 用户注册

#### 用户接口
- `GET /user/info` - 获取当前用户信息（角色、ID）
- `GET /user/{user_id}` - 获取用户详细信息

#### 角色接口
- `GET /role/list` - 获取角色列表
- `GET /role/{id}` - 获取角色详情

#### 权限接口
- `GET /ps/current` - 获取当前用户权限列表
- `GET /ps/{role_id}` - 获取指定角色权限

#### 文件接口
- `POST /file/upload-avatar` - 上传头像

### Token 机制

1. 登录成功后，从响应中获取 token
2. Token 自动存储在 localStorage 和 Pinia store 中
3. 每次请求自动在 Authorization Header 中携带 token
4. Token 失效（401）时自动跳转到登录页

### 响应格式

后端接口统一返回格式：

```json
{
  "code": 200,
  "msg": "success",
  "data": {}
}
```

- `code`: 状态码，200 表示成功
- `msg`: 提示信息
- `data`: 返回数据

## 状态管理

使用 Pinia 进行状态管理，主要状态：

```javascript
// 用户状态
{
  token: '',          // 认证 token
  userId: '',         // 用户 ID
  roles: [],          // 用户角色列表
  permissions: []     // 用户权限列表
}
```

## 路由守卫

路由守卫实现了：

1. 未登录用户访问受保护页面自动跳转到登录页
2. 已登录用户访问登录页自动跳转到首页
3. 页面加载前自动获取用户信息

## 开发注意事项

1. **API 调用**: 所有 API 调用使用封装好的 axios 实例（`@/utils/request.js`）
2. **路径别名**: 使用 `@` 表示 `src` 目录
3. **组件命名**: 使用 PascalCase 命名组件
4. **代码注释**: 关键函数和复杂逻辑需添加中文注释
5. **错误处理**: 统一在 axios 拦截器中处理错误提示

## 扩展指南

### 添加新页面

1. 在 `src/views` 下创建页面组件
2. 在 `src/router/index.js` 中添加路由配置
3. 如需权限控制，在路由 meta 中添加 `requiresAuth` 和角色信息

### 添加新接口

1. 在 `src/api` 目录下创建或编辑对应的接口文件
2. 使用统一的 request 实例发起请求
3. 接口函数需添加 JSDoc 注释

### 添加新角色菜单

在 `src/views/main/MainLayout.vue` 中的导航菜单部分添加角色判断和菜单项

## 常见问题

### 1. 跨域问题
开发环境已在 `vite.config.js` 中配置代理，如仍有问题请检查后端 CORS 配置

### 2. Token 失效
Token 存储在 localStorage，清除浏览器缓存可重置登录状态

### 3. 权限问题
确保后端正确配置了 Sa-Token 权限，前端会根据后端返回的角色和权限动态显示菜单

## 联系方式

如有问题，请联系开发团队或提交 Issue。
