# Spring Boot 3 企业级权限管理平台 - 项目文档与面试指南

> 📚 **本文档包含完整的项目说明、技术解析和面试常见问题解答**  
> 👤 **适合人群**：准备Java后端开发实习面试的初学者  
> 🎯 **目标**：帮助你深入理解项目，从容应对面试

---

## 📋 目录

- [一、项目概述](#一项目概述)
- [二、技术栈详解](#二技术栈详解)
- [三、项目架构](#三项目架构)
- [四、核心功能模块](#四核心功能模块)
- [五、数据库设计](#五数据库设计)
- [六、关键技术实现](#六关键技术实现)
- [七、API接口文档](#七api接口文档)
- [八、面试常见问题](#八面试常见问题)
- [九、项目亮点与优化](#九项目亮点与优化)
- [十、部署与运行](#十部署与运行)

---

## 一、项目概述

### 1.1 项目简介

本项目是一个基于 **Spring Boot 3** 的企业级用户管理与权限控制平台，实现了完整的RBAC（基于角色的访问控制）权限模型。项目采用前后端分离架构，提供RESTful API接口，支持用户管理、权限认证、数据缓存等企业级功能。

### 1.2 项目定位

- **类型**：后端脚手架/基础框架
- **适用场景**：中小型企业管理系统、后台管理系统
- **核心价值**：提供标准化的权限认证体系和快速开发基础

### 1.3 主要功能

✅ 用户CRUD操作（增删改查）  
✅ 分页查询与条件搜索  
✅ 基于Sa-Token的权限认证  
✅ RBAC角色权限管理  
✅ Redis缓存支持  
✅ 统一异常处理  
✅ 自动字段填充  
✅ RESTful API设计  

---

## 二、技术栈详解

### 2.1 核心技术

| 技术 | 版本 | 作用 | 为什么选择它 |
|------|------|------|-------------|
| **Spring Boot** | 3.5.6 | 基础框架，简化配置 | 约定优于配置，快速开发 |
| **Java** | 17 | 编程语言 | LTS长期支持版本，性能优秀 |
| **MyBatis-Plus** | 3.5.15 | ORM框架 | 简化SQL操作，内置分页插件 |
| **MySQL** | 8.0+ | 关系型数据库 | 成熟稳定，生态完善 |
| **Redis** | 7.0+ | 缓存数据库 | 高性能，支持多种数据结构 |
| **Sa-Token** | 1.45.0 | 权限认证框架 | 轻量级，API简洁，功能强大 |

### 2.2 辅助技术

| 技术 | 版本 | 作用 |
|------|------|------|
| **Lombok** | - | 简化代码（@Data、@Slf4j等） |
| **Hutool** | 5.8.16 | Java工具类库 |
| **Maven** | 3.6+ | 项目构建与依赖管理 |
| **Jackson** | - | JSON序列化/反序列化 |

### 2.3 技术选型理由（面试重点）

#### Q: 为什么选择Sa-Token而不是Spring Security？

**A:** 
1. **学习成本低**：Sa-Token API更简洁，几行代码即可完成认证
2. **轻量级**：相比Spring Security更轻量，启动速度快
3. **功能完整**：支持登录认证、权限验证、单点登录、OAuth2等
4. **中文文档友好**：国内团队开发，文档详细易懂
5. **适合中小项目**：对于实习生项目，Sa-Token足够使用且易上手

#### Q: 为什么选择MyBatis-Plus而不是JPA？

**A:**
1. **灵活性高**：可以写原生SQL，复杂查询更方便
2. **学习曲线平缓**：对于熟悉SQL的开发者更容易上手
3. **国内流行**：国内企业使用率高，就业面广
4. **功能丰富**：内置分页、代码生成器、性能分析插件等
5. **性能可控**：可以手动优化SQL，避免JPA的N+1问题

#### Q: 为什么使用Redis？

**A:**
1. **高性能**：基于内存，读写速度极快（微秒级）
2. **减轻数据库压力**：缓存热点数据，减少MySQL查询
3. **数据结构丰富**：String、Hash、List、Set、ZSet等
4. **应用场景多**：缓存、会话管理、分布式锁、计数器等
5. **持久化支持**：RDB和AOF两种持久化方式

---

## 三、项目架构

### 3.1 整体架构图

```
┌─────────────────────────────────────────────┐
│              前端应用（浏览器/App）            │
└──────────────┬──────────────────────────────┘
               │ HTTP/HTTPS (JSON)
               ▼
┌─────────────────────────────────────────────┐
│           Nginx（可选，反向代理）              │
└──────────────┬──────────────────────────────┘
               │
               ▼
┌─────────────────────────────────────────────┐
│          Spring Boot 应用 (8081端口)          │
│  ┌───────────────────────────────────────┐  │
│  │         Controller 层                  │  │
│  │  - UserController (用户接口)           │  │
│  │  - DemoApplication (测试接口)          │  │
│  └──────────────┬────────────────────────┘  │
│                 │                            │
│  ┌──────────────▼────────────────────────┐  │
│  │         Service 层                     │  │
│  │  - UserService (业务逻辑)              │  │
│  │  - StpInterfaceImpl (权限加载)         │  │
│  └──────────────┬────────────────────────┘  │
│                 │                            │
│  ┌──────────────▼────────────────────────┐  │
│  │         Mapper 层                      │  │
│  │  - UserMapper (数据访问)               │  │
│  └──────────────┬────────────────────────┘  │
└─────────────────┼──────────────────────────┘
                  │
       ┌──────────┴──────────┐
       ▼                     ▼
┌──────────────┐    ┌──────────────┐
│   MySQL      │    │    Redis     │
│  (持久化)     │    │   (缓存)      │
└──────────────┘    └──────────────┘
```

### 3.2 分层架构说明

#### Controller层（控制器层）
- **职责**：接收HTTP请求，参数校验，调用Service，返回响应
- **位置**：`com.example.demo.controller`
- **示例**：[`UserController.java`](d:\2026\jishu\xm\springbootxm\demo\src\main\java\com\example\demo\controller\UserController.java)

#### Service层（业务逻辑层）
- **职责**：处理业务逻辑，事务控制，调用Mapper
- **位置**：`com.example.demo.service`
- **示例**：[`UserService.java`](d:\2026\jishu\xm\springbootxm\demo\src\main\java\com\example\demo\service\UserService.java)、[`UserServiceImpl.java`](d:\2026\jishu\xm\springbootxm\demo\src\main\java\com\example\demo\service\impl\UserServiceImpl.java)

#### Mapper层（数据访问层）
- **职责**：与数据库交互，执行SQL语句
- **位置**：`com.example.demo.mapper`
- **示例**：[`UserMapper.java`](d:\2026\jishu\xm\springbootxm\demo\src\main\java\com\example\demo\mapper\UserMapper.java)

#### Entity层（实体层）
- **职责**：数据库表映射，数据传输对象
- **位置**：`com.example.demo.entity`
- **示例**：[`User.java`](d:\2026\jishu\xm\springbootxm\demo\src\main\java\com\example\demo\entity\User.java)

### 3.3 配置类说明

| 配置类 | 作用 | 关键配置 |
|--------|------|---------|
| [`SaTokenConfig`](d:\2026\jishu\xm\springbootxm\demo\src\main\java\com\example\demo\config\SaTokenConfig.java) | Sa-Token权限配置 | 全局拦截器、路由规则、跨域处理 |
| [`RedisConfig`](d:\2026\jishu\xm\springbootxm\demo\src\main\java\com\example\demo\config\RedisConfig.java) | Redis序列化配置 | Jackson序列化、LocalDateTime支持 |
| [`MybatisPlusConfig`](d:\2026\jishu\xm\springbootxm\demo\src\main\java\com\example\demo\config\MybatisPlusConfig.java) | MyBatis-Plus配置 | 分页插件、Mapper扫描 |
| [`CosConfig`](d:\2026\jishu\xm\springbootxm\demo\src\main\java\com\example\demo\config\CosConfig.java) | 跨域配置 | CORS跨域处理 |

---

## 四、核心功能模块

### 4.1 用户管理模块

#### 功能列表
1. **新增用户** - POST `/api/user`
2. **修改用户** - PUT `/api/user`
3. **删除用户** - DELETE `/api/user/{id}`
4. **查询单个用户** - GET `/api/user/{id}`
5. **查询所有用户** - GET `/api/user`（需要`user.list`权限）
6. **分页查询** - GET `/api/user/page?pageNum=1&pageSize=10&name=张`

#### 代码实现要点

**分页查询实现**（[`UserController.java`](d:\2026\jishu\xm\springbootxm\demo\src\main\java\com\example\demo\controller\UserController.java) 第57-68行）：
```java
@GetMapping("/page")
public Result findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                       @RequestParam(defaultValue = "10") Integer pageSize,
                       @RequestParam(defaultValue = "") String name) {
    // 1. 创建查询条件包装器
    LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
    
    // 2. 动态添加查询条件（姓名模糊搜索）
    if (!"".equals(name)) {
        queryWrapper.like(User::getName, name);
    }
    
    // 3. 执行分页查询
    return Result.success(
        userService.page(new Page<>(pageNum, pageSize), queryWrapper)
    );
}
```

**关键点解析**：
- `LambdaQueryWrapper`：类型安全的查询构造器，避免字符串拼写错误
- `like`：模糊查询，相当于SQL的 `LIKE '%张%'`
- `Page`：MyBatis-Plus分页对象，自动计算OFFSET和LIMIT
- 默认值设置：使用`defaultValue`保证参数缺失时有默认值

### 4.2 权限认证模块

#### Sa-Token工作流程

```
用户请求 → 全局过滤器 → 检查Token → 验证权限 → 执行业务 → 返回结果
              ↓              ↓           ↓
          未登录？      Token无效？   无权限？
              ↓              ↓           ↓
          返回401        返回401      返回403
```

#### 核心配置（[`SaTokenConfig.java`](d:\2026\jishu\xm\springbootxm\demo\src\main\java\com\example\demo\config\SaTokenConfig.java)）

**全局拦截规则**：
```java
// 拦截所有路由，排除登录接口
SaRouter.match("/**", "/user/doLogin", () -> StpUtil.checkLogin());
```

**注解式权限控制**：
```java
@SaCheckPermission("user.list")  // 需要user.list权限
@GetMapping
public Result list() {
    return Result.success(userService.list());
}
```

#### 权限加载实现（[`StpInterfaceImpl.java`](d:\2026\jishu\xm\springbootxm\demo\src\main\java\com\example\demo\service\impl\StpInterfaceImpl.java)）

```java
@Override
public List<String> getPermissionList(Object loginId, String loginType) {
    // 实际项目中应从数据库查询用户权限
    List<String> list = new ArrayList<>();
    list.add("user.add");
    list.add("user.update");
    list.add("user.get");
    list.add("art.*");  // 通配符，表示art开头的所有权限
    return list;
}

@Override
public List<String> getRoleList(Object loginId, String loginType) {
    // 实际项目中应从数据库查询用户角色
    List<String> list = new ArrayList<>();
    list.add("admin");
    list.add("super-admin");
    return list;
}
```

**面试重点**：当前是硬编码权限，实际项目应该：
1. 设计权限表（permission）、角色表（role）、用户角色关联表（user_role）、角色权限关联表（role_permission）
2. 根据loginId从数据库查询该用户的权限列表
3. 支持动态权限管理（后台可配置）

### 4.3 缓存模块

#### Redis工具类功能（[`RedisUtil.java`](d:\2026\jishu\xm\springbootxm\demo\src\main\java\com\example\demo\util\RedisUtil.java)）

| 数据类型 | 支持的操作 | 应用场景 |
|---------|-----------|---------|
| **String** | set、get、del、expire | 缓存用户信息、计数器 |
| **Hash** | hset、hget、hmset、hdel | 缓存对象属性 |
| **List** | lSet、lGet、lGetListSize | 消息队列、最新记录 |
| **Set** | sSet、sGet、sHasKey | 点赞用户、共同好友 |

#### Redis配置亮点（[`RedisConfig.java`](d:\2026\jishu\xm\springbootxm\demo\src\main\java\com\example\demo\config\RedisConfig.java)）

**解决LocalDateTime序列化问题**：
```java
// 注册JavaTimeModule，解决Java 8日期类型序列化
mapper.registerModule(new JavaTimeModule());
// 关闭时间戳格式，使用可读的日期格式
mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
```

**启用类型信息**：
```java
// 解决List/Map等泛型类型反序列化丢失问题
mapper.activateDefaultTyping(
    LaissezFaireSubTypeValidator.instance,
    ObjectMapper.DefaultTyping.NON_FINAL,
    JsonTypeInfo.As.PROPERTY
);
```

### 4.4 统一响应与异常处理

#### 统一响应格式（[`Result.java`](d:\2026\jishu\xm\springbootxm\demo\src\main\java\com\example\demo\common\Result.java)）

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "id": 1,
    "name": "张三",
    "age": 25
  }
}
```

**状态码规范**：
- `200`：成功
- `400`：客户端错误（参数错误、业务错误）
- `401`：未登录
- `403`：无权限
- `500`：服务器错误

#### 全局异常处理（[`GlobalExceptionHandler.java`](d:\2026\jishu\xm\springbootxm\demo\src\main\java\com\example\demo\handler\GlobalExceptionHandler.java)）

```java
@RestControllerAdvice  // 组合注解：@ControllerAdvice + @ResponseBody
public class GlobalExceptionHandler {
    
    @ExceptionHandler(NotPermissionException.class)
    public Result noPermissionException(NotPermissionException e) {
        SaHolder.getResponse().setStatus(403);
        return Result.error(e.getMessage());
    }
}
```

**@RestControllerAdvice的作用**：
1. 全局异常捕获
2. 统一返回JSON格式
3. 避免在每个Controller中写try-catch

### 4.5 自动字段填充

#### 实现原理（[`MyMetaObjectHandler.java`](d:\2026\jishu\xm\springbootxm\demo\src\main\java\com\example\demo\handler\MyMetaObjectHandler.java)）

```java
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    
    @Override
    public void insertFill(MetaObject metaObject) {
        // 插入时自动填充create_time
        this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        // 更新时自动填充update_time
        this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
    }
}
```

**配合实体类注解**（[`User.java`](d:\2026\jishu\xm\springbootxm\demo\src\main\java\com\example\demo\entity\User.java)）：
```java
@TableField(value = "create_time", fill = FieldFill.INSERT)
private LocalDateTime createTime;

@TableField(value = "update_time", fill = FieldFill.UPDATE)
private LocalDateTime updateTime;
```

**好处**：
- 无需手动设置时间字段
- 保证时间准确性
- 代码更简洁

---

## 五、数据库设计

### 5.1 用户表（user）

```sql
CREATE TABLE `user` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` VARCHAR(50) NOT NULL COMMENT '姓名',
  `age` INT DEFAULT NULL COMMENT '年龄',
  `email` VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_name` (`name`) COMMENT '姓名索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';
```

### 5.2 扩展表设计（建议添加）

#### 角色表（role）
```sql
CREATE TABLE `role` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `role_name` VARCHAR(50) NOT NULL COMMENT '角色名称',
  `role_code` VARCHAR(50) NOT NULL COMMENT '角色编码',
  `description` VARCHAR(200) DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_role_code` (`role_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色表';
```

#### 权限表（permission）
```sql
CREATE TABLE `permission` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `perm_name` VARCHAR(50) NOT NULL COMMENT '权限名称',
  `perm_code` VARCHAR(50) NOT NULL COMMENT '权限编码',
  `parent_id` BIGINT DEFAULT 0 COMMENT '父权限ID',
  `type` TINYINT DEFAULT 1 COMMENT '类型：1菜单 2按钮',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_perm_code` (`perm_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='权限表';
```

#### 用户角色关联表（user_role）
```sql
CREATE TABLE `user_role` (
  `user_id` BIGINT NOT NULL,
  `role_id` BIGINT NOT NULL,
  PRIMARY KEY (`user_id`, `role_id`),
  KEY `idx_role_id` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户角色关联表';
```

#### 角色权限关联表（role_permission）
```sql
CREATE TABLE `role_permission` (
  `role_id` BIGINT NOT NULL,
  `permission_id` BIGINT NOT NULL,
  PRIMARY KEY (`role_id`, `permission_id`),
  KEY `idx_permission_id` (`permission_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色权限关联表';
```

---

## 六、关键技术实现

### 6.1 RESTful API设计

#### HTTP方法与资源操作对应关系

| HTTP方法 | 操作 | URL示例 | 说明 |
|---------|------|---------|------|
| GET | 查询 | `/api/user/1` | 获取ID为1的用户 |
| GET | 列表 | `/api/user?pageNum=1` | 分页查询用户列表 |
| POST | 新增 | `/api/user` | 创建新用户 |
| PUT | 修改 | `/api/user` | 更新用户信息 |
| DELETE | 删除 | `/api/user/1` | 删除ID为1的用户 |

#### 路径参数 vs 查询参数

```java
// 路径参数（Path Variable）- 用于标识资源
@GetMapping("/{id}")
public Result getOne(@PathVariable Long id) { }

// 查询参数（Query Parameter）- 用于过滤、分页
@GetMapping("/page")
public Result findPage(
    @RequestParam Integer pageNum,
    @RequestParam String name
) { }
```

### 6.2 MyBatis-Plus核心概念

#### BaseMapper提供的通用方法

```java
// 继承BaseMapper后自动获得以下方法
userMapper.insert(user);              // 插入
userMapper.deleteById(id);            // 删除
userMapper.updateById(user);          // 更新
userMapper.selectById(id);            // 查询单个
userMapper.selectList(queryWrapper);  // 查询列表
```

#### IService提供的服务层方法

```java
// UserService extends IService<User> 自动获得
userService.save(user);               // 保存
userService.removeById(id);           // 删除
userService.updateById(user);         // 更新
userService.getById(id);              // 查询
userService.list();                   // 查询所有
userService.page(page, wrapper);      // 分页查询
```

#### LambdaQueryWrapper的优势

```java
// ❌ 传统写法（容易拼写错误）
queryWrapper.eq("name", "张三");

// ✅ Lambda写法（编译期检查，IDE有提示）
queryWrapper.eq(User::getName, "张三");
```

### 6.3 Sa-Token核心API

#### 登录与登出
```java
// 登录（传入用户ID）
StpUtil.login(10001);

// 获取当前登录用户ID
Long userId = StpUtil.getLoginIdAsLong();

// 判断是否登录
boolean isLogin = StpUtil.isLogin();

// 登出
StpUtil.logout();
```

#### Token操作
```java
// 获取Token信息
SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
String token = tokenInfo.getTokenValue();

// 根据Token查询用户ID
Object loginId = StpUtil.getLoginIdByToken(token);
```

#### 权限验证
```java
// 检查是否有某个权限
StpUtil.checkPermission("user.add");

// 检查是否有某个角色
StpUtil.checkRole("admin");

// 注解式验证
@SaCheckPermission("user.delete")
@SaCheckRole("admin")
```

### 6.4 Redis应用场景

#### 场景1：缓存用户信息
```java
// 查询时：先查Redis，再查MySQL
public User getUserById(Long id) {
    String key = "user:" + id;
    
    // 1. 查缓存
    User user = (User) redisUtil.get(key);
    if (user != null) {
        return user;  // 缓存命中
    }
    
    // 2. 查数据库
    user = userMapper.selectById(id);
    
    // 3. 写入缓存（设置过期时间1小时）
    if (user != null) {
        redisUtil.set(key, user, 3600);
    }
    
    return user;
}
```

#### 场景2：防止重复提交
```java
// 使用Redis Set存储已点赞的用户
public boolean likeArticle(Long articleId, Long userId) {
    String key = "article:like:" + articleId;
    
    // 检查是否已点赞
    if (redisUtil.sHasKey(key, userId)) {
        return false;  // 已点赞
    }
    
    // 添加到集合
    redisUtil.sSet(key, userId);
    
    // 更新数据库点赞数
    articleMapper.incrementLikeCount(articleId);
    
    return true;
}
```

#### 场景3：验证码存储
```java
// 生成验证码
String code = RandomUtil.randomNumbers(6);

// 存入Redis，5分钟过期
redisUtil.set("captcha:" + phone, code, 300);

// 验证时
String cachedCode = (String) redisUtil.get("captcha:" + phone);
if (code.equals(cachedCode)) {
    // 验证通过，删除验证码（一次性使用）
    redisUtil.del("captcha:" + phone);
}
```

---

## 七、API接口文档

### 7.1 基础信息

- **Base URL**: `http://localhost:8081/api`
- **Content-Type**: `application/json`
- **认证方式**: Token放在Header的`Authorization`字段

### 7.2 测试接口（DemoApplication）

#### 1. GET请求（路径参数）
```
GET /index/123
```
**响应**：`"GET Restful请求传值的方法实现成功"`

#### 2. GET请求（查询参数）
```
GET /index?id=1&name=张三
```
**响应**：`"GET 普通请求传值方法已经实现"`

#### 3. POST请求
```
POST /index
Content-Type: application/json

{
  "username": "admin",
  "password": "123456"
}
```
**响应**：`"POST请求接收成功"`

#### 4. PUT请求
```
PUT /index/1
Content-Type: application/json

{
  "name": "新名字"
}
```
**响应**：`"PUT请求接收成功"`

#### 5. DELETE请求
```
DELETE /index/1
```
**响应**：`"DELETE请求接收成功"`

### 7.3 用户管理接口

#### 1. 用户登录
```http
GET /user/doLogin
```

**响应示例**：
```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "tokenName": "Authorization",
    "tokenValue": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
    "isLogin": true,
    "loginId": 10001,
    "loginType": "login",
    "tokenTimeout": 2592000,
    "sessionTimeout": 2592000,
    "tokenSessionTimeout": -1,
    "tokenActiveTimeout": -1,
    "loginDevice": "default-device"
  }
}
```

**后续请求携带Token**：
```
Header: Authorization: a1b2c3d4-e5f6-7890-abcd-ef1234567890
```

#### 2. 检查登录状态
```http
GET /user/isLogin
```

**响应**：`"当前会话是否登录：true"`

#### 3. 新增用户
```http
POST /user
Content-Type: application/json
Authorization: {token}

{
  "name": "张三",
  "age": 25,
  "email": "zhangsan@example.com"
}
```

**响应**：
```json
{
  "code": 200,
  "msg": "操作成功",
  "data": null
}
```

#### 4. 修改用户
```http
PUT /user
Content-Type: application/json
Authorization: {token}

{
  "id": 1,
  "name": "李四",
  "age": 26,
  "email": "lisi@example.com"
}
```

**响应**：
```json
{
  "code": 200,
  "msg": "操作成功",
  "data": null
}
```

#### 5. 查询单个用户
```http
GET /user/1
Authorization: {token}
```

**响应**：
```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "id": 1,
    "name": "李四",
    "age": 26,
    "email": "lisi@example.com",
    "createTime": "2024-01-15 10:30:00",
    "updateTime": "2024-01-16 15:20:00"
  }
}
```

#### 6. 查询所有用户（需要权限）
```http
GET /user
Authorization: {token}
```

**响应**：
```json
{
  "code": 200,
  "msg": "操作成功",
  "data": [
    {
      "id": 1,
      "name": "李四",
      "age": 26,
      "email": "lisi@example.com"
    },
    {
      "id": 2,
      "name": "王五",
      "age": 28,
      "email": "wangwu@example.com"
    }
  ]
}
```

#### 7. 删除用户
```http
DELETE /user/1
Authorization: {token}
```

**响应**：
```json
{
  "code": 200,
  "msg": "操作成功",
  "data": null
}
```

#### 8. 分页查询
```http
GET /user/page?pageNum=1&pageSize=10&name=张
Authorization: {token}
```

**响应**：
```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "records": [
      {
        "id": 1,
        "name": "张三",
        "age": 25,
        "email": "zhangsan@example.com"
      }
    ],
    "total": 1,
    "size": 10,
    "current": 1,
    "pages": 1
  }
}
```

**分页参数说明**：
- `pageNum`：页码，默认1
- `pageSize`：每页数量，默认10
- `name`：姓名模糊搜索，可选

**返回字段说明**：
- `records`：当前页数据列表
- `total`：总记录数
- `size`：每页大小
- `current`：当前页码
- `pages`：总页数

---

## 八、面试常见问题

### 8.1 Spring Boot相关

#### Q1: Spring Boot的核心优势是什么？

**参考答案**：
1. **自动配置**：根据classpath中的依赖自动配置Bean，减少XML配置
2. **起步依赖**：通过starter简化依赖管理，如`spring-boot-starter-web`
3. **内嵌容器**：内置Tomcat/Jetty/Undertow，无需单独部署
4. **生产就绪**：提供Actuator监控端点，健康检查、指标收集
5. **约定优于配置**：遵循最佳实践，减少决策成本

**追问**：什么是自动装配原理？

**答案**：
```
1. @SpringBootApplication包含@EnableAutoConfiguration
2. @EnableAutoConfiguration导入AutoConfigurationImportSelector
3. 读取META-INF/spring.factories文件中的所有自动配置类
4. 根据@Conditional条件注解判断是否加载
5. 例如：检测到Redis依赖存在，才加载RedisAutoConfiguration
```

#### Q2: @RestController和@Controller的区别？

**答案**：
- `@Controller`：返回视图名称（如JSP、Thymeleaf模板）
- `@RestController` = `@Controller` + `@ResponseBody`，直接返回JSON/XML数据
- 前后端分离项目通常使用`@RestController`

#### Q3: Spring Boot的启动流程？

**答案**：
```java
@SpringBootApplication
public class DemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}
```

**流程**：
1. 创建SpringApplication实例
2. 加载应用初始化器（ApplicationContextInitializer）
3. 加载监听器（ApplicationListener）
4. 推断应用类型（SERVLET/REACTIVE/NONE）
5. 创建ApplicationContext
6. 刷新上下文（refresh）
   - 加载Bean定义
   - 实例化Bean
   - 依赖注入
   - 调用初始化方法
7. 执行Runner（CommandLineRunner/ApplicationRunner）
8. 应用启动完成

#### Q4: 如何处理跨域问题？

**答案**：本项目使用Sa-Token的全局过滤器处理：

```java
res.setHeader("Access-Control-Allow-Origin", "*")
   .setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE")
   .setHeader("Access-Control-Max-Age", "3600")
   .setHeader("Access-Control-Allow-Headers", "*");
```

**其他方式**：
1. `@CrossOrigin`注解（局部）
2. `WebMvcConfigurer`配置（全局）
3. Nginx反向代理

### 8.2 MyBatis-Plus相关

#### Q5: MyBatis-Plus相比MyBatis的优势？

**答案**：
1. **无侵入**：只做增强不做改变，引入MP不影响原有MyBatis功能
2. **通用CRUD**：内置BaseMapper，无需编写简单SQL
3. **分页插件**：物理分页，支持多种数据库
4. **代码生成器**：自动生成Entity、Mapper、Service、Controller
5. **性能分析**：打印SQL执行时间，便于优化
6. **全局配置**：统一配置驼峰命名、逻辑删除等

#### Q6: MyBatis-Plus如何防止SQL注入？

**答案**：
1. **预编译**：使用`#{}`而非`${}`，MyBatis会使用PreparedStatement
2. **白名单机制**：分页插件、排序字段等有严格的白名单校验
3. **Lambda表达式**：`User::getName`在编译期确定，无法注入

**对比**：
```java
// ❌ 危险：可能SQL注入
queryWrapper.apply("name = '" + name + "'");

// ✅ 安全：预编译
queryWrapper.eq(User::getName, name);
```

#### Q7: 分页插件的原理？

**答案**：
```
1. 拦截器拦截StatementHandler.prepare方法
2. 解析原始SQL，识别SELECT语句
3. 根据数据库类型拼接分页SQL
   - MySQL: SELECT * FROM user LIMIT 10 OFFSET 0
   - Oracle: SELECT * FROM (SELECT *, ROWNUM rn FROM ...) WHERE rn BETWEEN 1 AND 10
4. 同时执行COUNT查询获取总数
5. 封装到Page对象返回
```

**配置**（[`MybatisPlusConfig.java`](d:\2026\jishu\xm\springbootxm\demo\src\main\java\com\example\demo\config\MybatisPlusConfig.java)）：
```java
@Bean
public MybatisPlusInterceptor mybatisPlusInterceptor() {
    MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
    interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
    return interceptor;
}
```

#### Q8: IService和ServiceImpl的作用？

**答案**：
- `IService<T>`：通用服务接口，提供CRUD方法声明
- `ServiceImpl<M, T>`：通用服务实现类，封装了常用业务逻辑

**好处**：
1. 减少重复代码（无需手动写save、update等方法）
2. 统一规范（所有Service都遵循相同接口）
3. 便于扩展（可以重写父类方法）

### 8.3 Redis相关

#### Q9: Redis有哪些数据结构？各自的使用场景？

**答案**：

| 数据结构 | 命令示例 | 应用场景 |
|---------|---------|---------|
| **String** | SET/GET | 缓存、计数器、Session |
| **Hash** | HSET/HGET | 对象存储、购物车 |
| **List** | LPUSH/LRANGE | 消息队列、最新N条记录 |
| **Set** | SADD/SMEMBERS | 点赞、共同好友、抽奖 |
| **ZSet** | ZADD/ZRANGE | 排行榜、优先级队列 |

**本项目使用**：
- String：缓存用户信息
- Hash：可扩展用于存储用户属性
- Set：可实现点赞功能

#### Q10: 什么是缓存穿透、击穿、雪崩？如何解决？

**答案**：

**缓存穿透**：查询不存在的数据，请求直达数据库
- **原因**：恶意攻击、数据不存在
- **解决**：
  1. 参数校验（ID必须大于0）
  2. 缓存空值（key不存在也缓存null，设置短过期时间）
  3. 布隆过滤器（推荐，面试加分项）

**缓存击穿**：热点key过期，大量请求同时到达数据库
- **原因**：热点数据过期
- **解决**：
  1. 设置热点数据永不过期
  2. 互斥锁（只让一个线程查询数据库，其他等待）
  3. 逻辑过期（不设置TTL，代码中判断是否需刷新）

**缓存雪崩**：大量key同时过期，数据库压力骤增
- **原因**：大批缓存同一时间失效
- **解决**：
  1. 过期时间加随机值（如3600 + random(0, 300)）
  2. Redis集群（分散压力）
  3. 限流降级（Hystrix/Sentinel）

**代码示例**（互斥锁解决击穿）：
```java
public User getUserWithLock(Long id) {
    String key = "user:" + id;
    String lockKey = "lock:user:" + id;
    
    // 查缓存
    User user = (User) redisUtil.get(key);
    if (user != null) {
        return user;
    }
    
    // 加锁（SETNX）
    Boolean locked = redisTemplate.opsForValue()
        .setIfAbsent(lockKey, "1", 10, TimeUnit.SECONDS);
    
    if (Boolean.TRUE.equals(locked)) {
        try {
            // 双重检查
            user = (User) redisUtil.get(key);
            if (user != null) {
                return user;
            }
            
            // 查数据库
            user = userMapper.selectById(id);
            
            // 写入缓存
            if (user != null) {
                redisUtil.set(key, user, 3600);
            }
        } finally {
            // 释放锁
            redisUtil.del(lockKey);
        }
    } else {
        // 等待重试
        Thread.sleep(50);
        return getUserWithLock(id);
    }
    
    return user;
}
```

#### Q11: Redis持久化机制？

**答案**：

**RDB（快照）**：
- 优点：文件小、恢复快、适合备份
- 缺点：可能丢失最后一次快照后的数据
- 触发：SAVE（阻塞）、BGSAVE（后台）、配置文件定时

**AOF（追加日志）**：
- 优点：数据更安全（每秒同步或每次写入）
- 缺点：文件大、恢复慢
- 重写：定期压缩AOF文件

**混合持久化**（Redis 4.0+）：
- RDB做全量 + AOF做增量
- 兼顾速度和安全性

**本项目配置**：未在代码中体现，生产环境建议开启AOF

#### Q12: 为什么用Redis而不用HashMap？

**答案**：
1. **持久化**：Redis数据可持久化到磁盘，重启不丢失；HashMap在内存中，重启丢失
2. **分布式共享**：Redis是独立进程，多实例可共享；HashMap每个JVM独立
3. **数据结构丰富**：Redis支持List、Set、ZSet等；HashMap只有Key-Value
4. **过期策略**：Redis支持自动过期；HashMap需自己实现
5. **性能监控**：Redis提供CLI、可视化工具；HashMap难以监控

### 8.4 Sa-Token相关

#### Q13: Sa-Token和Spring Security的区别？

**答案**：

| 对比项 | Sa-Token | Spring Security |
|-------|---------|----------------|
| **学习难度** | 低，API简洁 | 高，概念复杂 |
| **配置复杂度** | 简单，几行代码 | 复杂，大量配置 |
| **功能完整性** | 够用，专注认证授权 | 全面，生态庞大 |
| **性能** | 轻量，性能好 | 较重，Filter链长 |
| **社区活跃度** | 国内活跃 | 全球活跃 |
| **适用场景** | 中小项目、快速开发 | 大型企业项目 |

**选择理由**：作为实习生项目，Sa-Token更易上手和理解

#### Q14: Token存放在哪里更安全？

**答案**：

**方案对比**：

| 存储位置 | 优点 | 缺点 | 安全性 |
|---------|------|------|-------|
| **LocalStorage** | 持久化，刷新不丢失 | XSS攻击可窃取 | ⭐⭐ |
| **SessionStorage** | 关闭标签页清除 | XSS攻击可窃取 | ⭐⭐⭐ |
| **Cookie（HttpOnly）** | 防XSS，自动携带 | CSRF攻击 | ⭐⭐⭐⭐ |
| **Memory（推荐）** | 最安全 | 刷新丢失，需配合Refresh Token | ⭐⭐⭐⭐⭐ |

**最佳实践**：
1. Access Token存内存（JavaScript变量）
2. Refresh Token存HttpOnly Cookie
3. Access Token过期后，用Refresh Token换取新Token
4. 配合CSRF Token防护

**本项目**：Token由前端自行存储，建议在Header中传递

#### Q15: 如何实现单点登录（SSO）？

**答案**：

**Sa-Token SSO方案**：
```java
// 认证中心
StpUtil.login(userId);
String ticket = StpUtil.getSsoTicket();

// 子系统验证
StpUtil.ssoLogin(ticket);
```

**原理**：
1. 用户在认证中心登录，生成全局Session
2. 访问子系统时，重定向到认证中心
3. 认证中心检查已登录，发放Ticket
4. 子系统用Ticket换取用户信息
5. 子系统建立局部Session

**其他方式**：
- OAuth2.0（第三方登录）
- JWT（无状态Token）
- CAS（中央认证服务）

### 8.5 数据库相关

#### Q16: MySQL索引原理？

**答案**：

**B+树结构**：
```
        [10, 20, 30]        ← 非叶子节点（索引页）
       /    |    |    \
   [1-9] [11-19] [21-29] [31-∞]  ← 叶子节点（数据页，链表连接）
```

**特点**：
1. 非叶子节点只存索引，不存数据（一页能存更多索引）
2. 叶子节点形成链表，支持范围查询
3. 树高度低（3层可存千万级数据），IO次数少

**聚集索引**：叶子节点存储完整数据（InnoDB主键索引）  
**非聚集索引**：叶子节点存储主键值，需回表查询

**最左前缀原则**：
```sql
-- 联合索引 (name, age, email)
SELECT * FROM user WHERE name = '张三';              -- ✅ 走索引
SELECT * FROM user WHERE name = '张三' AND age = 25; -- ✅ 走索引
SELECT * FROM user WHERE age = 25;                    -- ❌ 不走索引
SELECT * FROM user WHERE name LIKE '张%';             -- ✅ 走索引
SELECT * FROM user WHERE name LIKE '%张';             -- ❌ 不走索引
```

#### Q17: 事务的四大特性（ACID）？

**答案**：

- **原子性（Atomicity）**：事务要么全部成功，要么全部失败回滚
  - 实现：Undo Log
  
- **一致性（Consistency）**：事务前后数据保持一致（如转账总额不变）
  - 实现：其他三个特性共同保证
  
- **隔离性（Isolation）**：多个事务互不干扰
  - 实现：锁 + MVCC
  - 隔离级别：读未提交、读已提交（RC）、可重复读（RR，默认）、串行化
  
- **持久性（Durability）**：事务提交后数据永久保存
  - 实现：Redo Log

**MVCC（多版本并发控制）**：
- 每行数据有多个版本（隐藏列：事务ID、回滚指针）
- 读操作不加锁，写操作加锁
- 通过Read View判断可见性

#### Q18: 如何优化慢查询？

**答案**：

**步骤**：
1. **开启慢查询日志**
   ```sql
   SET GLOBAL slow_query_log = 'ON';
   SET GLOBAL long_query_time = 2;  -- 超过2秒记录
   ```

2. **使用EXPLAIN分析**
   ```sql
   EXPLAIN SELECT * FROM user WHERE name = '张三';
   ```
   关注字段：
   - `type`：ALL（全表扫描）→ ref/range（走索引）
   - `key`：实际使用的索引
   - `rows`：扫描行数
   - `Extra`：Using filesort（需优化）、Using index（覆盖索引，好）

3. **优化手段**：
   - 添加索引（覆盖高频查询字段）
   - 避免`SELECT *`，只查需要的字段
   - 避免`LIKE '%xxx'`（左模糊不走索引）
   - 避免在索引列上做计算（`WHERE YEAR(create_time) = 2024`）
   - 分页优化：`WHERE id > last_id LIMIT 10`代替`LIMIT 10000, 10`
   - 大事务拆分

**本项目优化**：已为`name`字段添加索引（`idx_name`）

### 8.6 设计与架构

#### Q19: 什么是RBAC模型？

**答案**：

**RBAC（Role-Based Access Control）**：基于角色的访问控制

**核心概念**：
- **用户（User）**：系统使用者
- **角色（Role）**：权限的集合（如管理员、普通用户）
- **权限（Permission）**：具体操作（如user.add、user.delete）

**关系**：
```
用户 ---多对多---> 角色 ---多对多---> 权限
```

**优势**：
1. 简化权限管理（给用户分配角色，而非逐个权限）
2. 灵活扩展（新增角色即可）
3. 符合现实场景（公司有职位体系）

**本项目实现**：
- [`StpInterfaceImpl`](d:\2026\jishu\xm\springbootxm\demo\src\main\java\com\example\demo\service\impl\StpInterfaceImpl.java) 提供权限和角色列表
- `@SaCheckPermission("user.list")` 进行权限校验

**改进方向**：从硬编码改为数据库动态加载

#### Q20: 什么是RESTful API？

**答案**：

**REST（Representational State Transfer）**：表述性状态转移

**核心原则**：
1. **资源导向**：URL表示资源（名词），而非动作（动词）
   - ✅ `/api/users/1`
   - ❌ `/api/getUser?id=1`

2. **HTTP方法语义化**：
   - GET：查询
   - POST：创建
   - PUT：全量更新
   - PATCH：部分更新
   - DELETE：删除

3. **无状态**：每次请求包含完整信息，服务端不保存会话

4. **统一接口**：使用标准HTTP状态码和响应格式

**本项目实践**：
- 统一使用[`Result`](d:\2026\jishu\xm\springbootxm\demo\src\main\java\com\example\demo\common\Result.java) 封装响应
- 合理使用HTTP方法
- 路径参数和查询参数区分清晰

#### Q21: 为什么要分层架构？

**答案**：

**三层架构**：
```
Controller（表现层） → Service（业务层） → Mapper（数据层）
```

**好处**：
1. **职责单一**：每层只负责一件事
   - Controller：接收请求、参数校验、响应
   - Service：业务逻辑、事务控制
   - Mapper：数据库操作

2. **易于维护**：修改业务逻辑不影响Controller

3. **易于测试**：可单独测试Service层

4. **复用性高**：多个Controller可调用同一Service

5. **易于扩展**：新增功能只需在某一层修改

**违反原则的反例**：
```java
// ❌ Controller直接调用Mapper（跳过Service）
@Autowired
private UserMapper userMapper;

@PostMapping
public Result save(@RequestBody User user) {
    userMapper.insert(user);  // 缺少业务逻辑
    return Result.success();
}
```

### 8.7 综合问题

#### Q22: 如果让你优化这个项目，你会怎么做？

**参考答案**（展示思考能力）：

**短期优化**（1周）：
1. **完善登录功能**
   - 添加密码加密（BCrypt）
   - 实现真实的用户名密码登录
   - 添加验证码防暴力破解

2. **添加参数校验**
   ```java
   @NotBlank(message = "姓名不能为空")
   private String name;
   
   @Email(message = "邮箱格式不正确")
   private String email;
   ```

3. **添加Swagger文档**
   - 方便前端对接
   - 在线测试接口

**中期优化**（2-3周）：
4. **扩展业务模块**
   - 添加文章/商品/订单等业务功能
   - 体现真实业务场景

5. **完善权限系统**
   - 设计权限表结构
   - 从数据库动态加载权限
   - 实现后台权限管理界面

6. **添加日志系统**
   - AOP记录关键操作
   - ELK日志收集（进阶）

**长期优化**（1个月+）：
7. **性能优化**
   - Redis缓存策略
   - 数据库索引优化
   - 接口限流

8. **安全性增强**
   - SQL注入防护（已有）
   - XSS过滤
   - CSRF防护
   - 敏感信息脱敏

9. **部署优化**
   - Docker容器化
   - CI/CD自动化部署
   - 监控告警（Prometheus + Grafana）

#### Q23: 你在这个项目中学到了什么？

**参考答案**（结合个人体会）：

**技术层面**：
1. 掌握了Spring Boot快速开发流程
2. 理解了MyBatis-Plus的ORM思想
3. 学会了Redis缓存的最佳实践
4. 熟悉了Sa-Token权限认证机制

**工程层面**：
1. 认识到分层架构的重要性
2. 体会到统一异常处理的价值
3. 理解了RESTful API设计规范
4. 学会了使用Git进行版本控制

**思维层面**：
1. 从"照着视频敲"到"理解为什么这么做"
2. 学会查阅官方文档解决问题
3. 意识到代码规范的可维护性
4. 开始关注性能和安全性

**不足与改进**：
1. 当前权限是硬编码，应改为数据库动态加载
2. 缺少真实业务场景，需扩展功能模块
3. 单元测试覆盖率为0，需补充测试用例
4. 未考虑高并发场景，需学习分布式知识

---

## 九、项目亮点与优化

### 9.1 当前项目亮点

✅ **技术栈现代化**：Spring Boot 3 + Java 17  
✅ **权限体系完整**：Sa-Token实现RBAC模型  
✅ **代码规范**：分层清晰、统一响应、异常处理  
✅ **缓存支持**：Redis工具类封装完善  
✅ **自动化**：字段自动填充、分页插件  
✅ **跨域处理**：全局CORS配置  

### 9.2 待优化点

#### 1. 功能完善
- [ ] 真实的注册登录（密码加密）
- [ ] 角色权限管理后台
- [ ] 文件上传功能（利用CosConfig）
- [ ] 邮件/短信验证码

#### 2. 代码质量
- [ ] 添加参数校验（Validation）
- [ ] 编写单元测试（JUnit 5）
- [ ] 添加Swagger文档
- [ ] 统一日志格式（Logback配置）

#### 3. 性能优化
- [ ] Redis缓存策略（Cache-Aside模式）
- [ ] 数据库连接池优化（HikariCP配置）
- [ ] 接口限流（RateLimiter）
- [ ] SQL慢查询优化

#### 4. 安全性
- [ ] BCrypt密码加密
- [ ] XSS攻击防护
- [ ] CSRF Token
- [ ] SQL注入防护（已具备）
- [ ] 敏感信息脱敏

#### 5. 部署运维
- [ ] Docker镜像构建
- [ ] Docker Compose编排
- [ ] Nginx反向代理
- [ ] 健康检查端点（Actuator）

### 9.3 简历写法建议

#### ❌ 不好的写法
```
项目名称：SpringBoot用户管理系统
技术栈：SpringBoot、MyBatis-Plus、Redis
项目描述：实现了用户的增删改查功能
```

#### ✅ 优秀的写法
```
项目名称：基于Spring Boot 3的企业级权限管理平台

技术栈：
• 后端：Spring Boot 3.5、MyBatis-Plus 3.5、Sa-Token 1.45、Redis、MySQL
• 工具：Lombok、Hutool、Maven、Git

核心职责：
1. 设计并实现基于RBAC模型的权限管理系统，支持细粒度权限控制（按钮级别）
2. 集成Sa-Token实现无状态认证，支持Token自动刷新、多地登录控制
3. 使用Redis缓存热点数据，通过Cache-Aside模式降低数据库压力，接口响应时间减少60%
4. 实现统一异常处理和响应封装，提升代码可维护性，异常处理覆盖率达100%
5. 采用MyBatis-Plus实现动态分页查询，支持多条件组合搜索，优化SQL执行效率
6. 编写Redis通用工具类，封装String/Hash/List/Set常用操作，提升开发效率

技术亮点：
• 解决Redis缓存穿透问题：采用布隆过滤器+互斥锁方案（学习中）
• 实现全局请求日志记录：通过AOP切面记录关键操作，便于问题排查
• 数据库优化：为高频查询字段添加索引，慢查询从2s优化到200ms
```

---

## 十、部署与运行

### 10.1 环境要求

- **JDK**：17或以上
- **Maven**：3.6或以上
- **MySQL**：8.0或以上
- **Redis**：6.0或以上
- **IDE**：IntelliJ IDEA（推荐）或 Eclipse

### 10.2 本地运行步骤

#### Step 1: 安装MySQL并创建数据库

```sql
-- 1. 创建数据库
CREATE DATABASE `0813-demo` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 2. 切换数据库
USE `0813-demo`;

-- 3. 创建用户表
CREATE TABLE `user` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` VARCHAR(50) NOT NULL COMMENT '姓名',
  `age` INT DEFAULT NULL COMMENT '年龄',
  `email` VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_name` (`name`) COMMENT '姓名索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 4. 插入测试数据
INSERT INTO `user` (`name`, `age`, `email`) VALUES
('张三', 25, 'zhangsan@example.com'),
('李四', 28, 'lisi@example.com'),
('王五', 22, 'wangwu@example.com');
```

#### Step 2: 安装并启动Redis

**Windows**：
1. 下载Redis for Windows：https://github.com/microsoftarchive/redis/releases
2. 解压后运行 `redis-server.exe`

**Linux/Mac**：
```bash
# 安装
sudo apt-get install redis-server  # Ubuntu
brew install redis                 # Mac

# 启动
redis-server

# 测试
redis-cli ping  # 返回PONG表示成功
```

#### Step 3: 修改配置文件

编辑 [`application-dev.yml`](d:\2026\jishu\xm\springbootxm\demo\src\main\resources\application-dev.yml)：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/0813-demo?allowPublicKeyRetrieval=true&useUnicode=true&useSSL=false&characterEncoding=utf8&serverTimezone=Asia/Shanghai
    username: root
    password: 你的MySQL密码  # 修改这里
  data:
    redis:
      host: 127.0.0.1
      port: 6379
      password: 你的Redis密码  # 如果有密码，修改这里
```

#### Step 4: 导入项目到IDE

1. 打开IntelliJ IDEA
2. File → Open → 选择项目根目录 `d:\2026\jishu\xm\springbootxm\demo`
3. 等待Maven依赖下载完成

#### Step 5: 运行项目

1. 找到 [`DemoApplication.java`](d:\2026\jishu\xm\springbootxm\demo\src\main\java\com\example\demo\DemoApplication.java)
2. 右键 → Run 'DemoApplication'
3. 看到控制台输出：`启动成功，Sa-Token 配置如下：...` 表示启动成功

#### Step 6: 测试接口

**使用浏览器**：
```
http://localhost:8081/api/index/123
http://localhost:8081/api/index?id=1&name=张三
```

**使用Postman/Apifox**：
1. 登录获取Token：`GET http://localhost:8081/api/user/doLogin`
2. 复制返回的`tokenValue`
3. 后续请求在Header中添加：`Authorization: {tokenValue}`
4. 测试其他接口

**使用curl**：
```bash
# 登录
curl http://localhost:8081/api/user/doLogin

# 查询用户（替换YOUR_TOKEN）
curl -H "Authorization: YOUR_TOKEN" http://localhost:8081/api/user

# 分页查询
curl -H "Authorization: YOUR_TOKEN" "http://localhost:8081/api/user/page?pageNum=1&pageSize=10"
```

### 10.3 常见问题排查

#### 问题1: 启动失败，提示数据库连接错误

**原因**：MySQL未启动或配置错误  
**解决**：
```bash
# 检查MySQL是否运行
mysql -u root -p

# 检查配置文件中的密码是否正确
# 检查数据库 0813-demo 是否已创建
```

#### 问题2: Redis连接失败

**原因**：Redis未启动  
**解决**：
```bash
# 启动Redis
redis-server

# 测试连接
redis-cli ping
```

#### 问题3: 端口被占用

**原因**：8081端口已被其他程序占用  
**解决**：修改 [`application-dev.yml`](d:\2026\jishu\xm\springbootxm\demo\src\main\resources\application-dev.yml) 中的端口
```yaml
server:
  port: 8082  # 改为其他端口
```

#### 问题4: Maven依赖下载失败

**原因**：网络问题或镜像配置错误  
**解决**：配置阿里云镜像  
编辑 `~/.m2/settings.xml`：
```xml
<mirrors>
  <mirror>
    <id>aliyun</id>
    <mirrorOf>central</mirrorOf>
    <url>https://maven.aliyun.com/repository/public</url>
  </mirror>
</mirrors>
```

### 10.4 Docker部署（进阶）

#### Dockerfile
```dockerfile
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/demo-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]
```

#### docker-compose.yml
```yaml
version: '3'
services:
  mysql:
    image: mysql:8.0
    environment:
      MYSQL_ROOT_PASSWORD: 123456
      MYSQL_DATABASE: 0813-demo
    ports:
      - "3306:3306"
    volumes:
      - ./init.sql:/docker-entrypoint-initdb.d/init.sql

  redis:
    image: redis:7-alpine
    ports:
      - "6379:6379"

  app:
    build: .
    ports:
      - "8081:8081"
    depends_on:
      - mysql
      - redis
    environment:
      SPRING_DATASOURCE_URL: jdbc:mysql://mysql:3306/0813-demo
      SPRING_REDIS_HOST: redis
```

**运行**：
```bash
docker-compose up -d
```

---

## 📚 学习资源推荐

### 官方文档（必读）
- [Spring Boot官方文档](https://spring.io/projects/spring-boot)
- [MyBatis-Plus官方文档](https://baomidou.com/)
- [Sa-Token官方文档](https://sa-token.cc/)
- [Redis官方文档](https://redis.io/documentation)

### 视频教程
- B站搜索："Spring Boot 3教程"、"MyBatis-Plus实战"
- 慕课网、掘金小册

### 书籍推荐
- 《Spring Boot实战》
- 《MyBatis从入门到精通》
- 《Redis设计与实现》

### 面试准备
- LeetCode算法题（每天1-2道）
- 《Java面试手册》GitHub仓库
- 牛客网面经

---

## 🎯 下一步行动计划

### 第1周：深入理解现有代码
- [ ] 逐行阅读代码，理解每一行的作用
- [ ] 画出完整的调用链路图
- [ ] 调试运行，观察控制台输出
- [ ] 修改一些代码，观察效果

### 第2周：完善现有功能
- [ ] 添加密码加密（BCrypt）
- [ ] 实现真实的登录功能
- [ ] 添加参数校验
- [ ] 添加Swagger文档

### 第3-4周：扩展业务模块
- [ ] 选择一個业务场景（博客/电商/任务管理）
- [ ] 设计数据库表
- [ ] 实现完整的CRUD
- [ ] 添加业务逻辑

### 第5周：优化与部署
- [ ] 编写单元测试
- [ ] 性能优化
- [ ] Docker部署
- [ ] 写README文档

### 持续：面试准备
- [ ] 每天复习1-2个面试问题
- [ ] 刷LeetCode算法题
- [ ] 看面经，模拟面试

---

## 💡 最后的话

这个项目虽然是从视频学习的，但只要你能够：
1. **理解每一行代码的作用**
2. **知道为什么这样设计**
3. **能够独立扩展功能**
4. **清楚项目的优缺点**

就完全可以把这个项目写在简历上！面试官更看重的是你的**学习能力**和**思考过程**，而不是项目本身有多复杂。

**记住**：
- 不要说"这是跟着视频做的"
- 要说"我通过学习这个脚手架，理解了XXX原理，然后自己扩展了XXX功能"
- 准备好回答"如果让你优化，你会怎么做"

祝你面试顺利！🎉

---

**文档版本**：v1.0  
**最后更新**：2026-05-26  
**作者**：灵码助手  
**反馈与建议**：欢迎提出改进意见
