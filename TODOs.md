# TODOs

## Next

json 解析器的实现


## log

- [x] 日志对齐输出。修改 `/pm/src/main/java/com/minister/pm/log/LogEntity.java` 的 `toString()`
- [x] 日志中类名输出是否输出短路径名 `/pm/src/main/java/com/minister/pm/log/Logger.java` Line 79。若使用日志实体进行输出，则在日志实体中修改
- [ ] 日志输出流的选择。
- [ ] 级别选择，可以禁止 Debug 级别的打印

## Prime Minister

- [ ] IoC 的实现
- [x] 注解扫描，全包扫描注解，分类存放到 Context
- [ ] Context 的完善
- [ ] 注解扫描&注入逻辑优化

## Server

- [x] 请求对象的封装
- [ ] Session & Cookie
- [ ] 简易静态文件的支持

## Ship

- [ ] 对象序列化
- [ ] 统一的存储接口
- [ ] 存储文件的加密与解密


## 整体

- 代码整理
- 文档更新



整体框架搭建起来之后，就可以在此基础上进行各种造轮子实验了。如：

- [ ] 连接池
- [x] MQ