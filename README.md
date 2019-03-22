# Prime Minister

这是一个用于学习的简易 Web 开发框架，使用类似 Spring Boot 的思想，尽量基于 JDK 实现，最少依赖。

## 功能：

- 内置简易 Web 服务器,基于 NIO 实现。（待完善）（Server）
- 内置内存数据库，也可能是对象存储（待实现）（Ship,DSO（data store object））
- 前端 Mock Server,基于配置的路由和响应

### 简易 Web 服务器

基于 NIO 实现，实现了自己的路由，自己的注解。


### 内存数据库

类似 redis ，支持持久化，也可能是一个对象存储器，将对象直接持久化。分文件存放


### 前端 Mock Server

- 基于配置的路由和响应，给前后端提供统一的交互接口。方便的切换到后端 Server.
- 是前后端交互的协议（基于映射），能为前端和后端提供支持，为前段提供响应能力，为后端提供请求能力
- 提供 Web 界面
- 也可以作为微服务之间的 Mock Server.

## TODOs

### Prime Minister

- [ ] IoC 的实现
- [ ] 注解扫描，全包扫描注解，分类存放到 Context
- [ ] Context 的完善

### Server

- [ ] 请求对象的封装

### Ship

- [ ] 对象序列化
- [ ] 统一的存储接口
- [ ] 存储文件的加密与解密