# Prime Minister

这是一个用于学习的简易 Web 开发框架，使用类似 Spring Boot 的思想，尽量基于 JDK 实现，最少依赖。

## 功能：

- 内置简易 Web 服务器,基于 NIO 实现。（待完善）（Server）
- 内置内存数据库，也可能是对象存储（待实现）（Ship,DSO（data store object））
- 前端 Mock Server,基于配置的路由和响应（待实现）

### 简易 Web 服务器

基于 NIO 实现，实现了自己的路由，自己的注解。


### 内存数据库（待实现）

类似 redis ，支持持久化，也可能是一个对象存储器，将对象直接持久化。分文件存放


### 前端 Mock Server（待实现）

- 基于配置的路由和响应，给前后端提供统一的交互接口。方便的切换到后端 Server.
- 是前后端交互的协议（基于映射），能为前端和后端提供支持，为前段提供响应能力，为后端提供请求能力
- 提供 Web 界面
- 也可以作为微服务之间的 Mock Server.

## How to Start

将代码编译后打成 jar 包，作为依赖，之后便可像 Spring Boot 一样进行 Web 开发。

* 项目根目录的启动类。

```java
@App
public class PmTestApplication {

	public static void main(String[] args) {
		PrimeMinister.run();
	}
}
```

* Controller 与依赖注入

```java
@URLMapping("/pm")
@RestController()
public class Controller {

	@Autowired
	private Service service;

	@URLMapping("/index")
	public String getIndex() {
		return "/html/index.html";
	}

	@URLMapping("/hello")
	public String hello() {
		return service.hello();
	}
}
```

* 组件

```java
@Component()
public class Service {
	public String hello() {
		return "{code:200,msg:'success',data:{a:3}}";
	}
}
```

### 启动效果

1. 控制台输出

```
2019-03-29 21:08:04.245   INFO     --- [c.m.p.c.Scanner               ] : pmtest.Service
2019-03-29 21:08:04.297   INFO     --- [c.m.p.c.Scanner               ] : pmtest.PmTestApplication
2019-03-29 21:08:04.326   INFO     --- [c.m.p.c.Scanner               ] : pmtest.Controller
2019-03-29 21:08:04.354   INFO     --- [c.m.p.c.Scanner               ] : Mapped url:[/pm/index] to :pmtest.Controller.getIndex()
2019-03-29 21:08:04.356   INFO     --- [c.m.p.c.Scanner               ] : Mapped url:[/pm/hello] to :pmtest.Controller.hello()
2019-03-29 21:08:04.357   INFO     --- [c.m.p.c.Scanner               ] : Mapped url:[/pm/user] to :pmtest.Controller.getUser()
2019-03-29 21:08:04.362   INFO     --- [c.m.p.c.Scanner               ] : Mapped url:[/pm/index] to :pmtest.Controller.getIndex()
2019-03-29 21:08:04.362   INFO     --- [c.m.p.c.Scanner               ] : Mapped url:[/pm/hello] to :pmtest.Controller.hello()
2019-03-29 21:08:04.368   INFO     --- [c.m.p.c.Scanner               ] : Mapped url:[/pm/user] to :pmtest.Controller.getUser()
2019-03-29 21:08:04.400   INFO     --- [c.m.p.s.HttpServer            ] : Server listening on 127.0.0.1:8080....
```

2. 访问

![/pm/index](http://ww1.sinaimg.cn/large/0076TGp9gy1g1kp1p7peej30a102p0sn.jpg)

3. 错误打印

![error](http://ww1.sinaimg.cn/large/0076TGp9gy1g1kp50fhesj30t70aptaz.jpg)
