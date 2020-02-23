# Prime Minister

这是一个用于学习的简易 MVC 框架，尽量基于 JDK 实现，最少依赖。

玩具系列之一。

## 功能：

- [x] 内置简易 Web 服务器,基于 NIO 实现。（待完善）（Server）
- [ ] 内置内存数据库，也可能是对象存储（待实现）（Ship,DSO（data store object））
- [ ] 前端 Mock Server,基于配置的路由和响应（待实现）
- [ ] json 解析器（待实现，拟使用类似 yml 解析的方式实现）
- [x] 日志打印(Almost Done)
- [x] 类 yml 格式的配置文件的读取及应用

### 简易 Web 服务器

基于 NIO 实现，实现了路由，注解。

也可考虑函数式的路由定义：

```java
router.reg('/index',(req,resp) -> {})
```

### 内存数据库（待实现）

类似 redis ，支持持久化，也可能是一个对象存储器，将对象直接持久化。分文件存放

json 解析器完成之后，使用 json 格式持久化数据。

拟直接通过 [goredis](https://github.com/jianxinliu/goredis ) 的编程语言客户端连接 goredis，不再重新基于 Java 实现。


### 前端 Mock Server（待实现）

- 基于配置的路由和响应，给前后端提供统一的交互接口。方便的切换到后端 Server.
- 是前后端交互的协议（基于映射），能为前端和后端提供支持，为前端提供响应能力，为后端提供请求能力
- 提供 Web 界面
- 也可以作为微服务之间的 Mock Server.

### 日志打印（Almost Done）

- 支持多种日志级别
- 支持模板解析和占位符解析
- 支持短类名

todo:
- [ ] 日志输出地的选择，可以支持在配置文件中配置
- [ ] 日志级别的选择与禁止（如正式运行时禁止 debug 级别的日志输出）

### Config（Done）

- 默认部分配置
- 类 yml 格式文件解析支持
- 配置注入

## How to Start

将代码编译后打成 jar 包（mvn install 到本地），导入作为依赖，之后便可像 Spring Boot 一样进行 Web 开发。

- 测试工程 POM 文件

```xml
<dependency>
    <groupId>com.minister</groupId>
    <artifactId>pm</artifactId>
    <version>0.0.1</version>
</dependency>
```

* 项目根目录的启动类。

```java
@PmApplication
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

1. 控制台输出(另一个测试工程的输出，略微有出入。此处的输出是修改过后的，以此为准。)

```
          _____                    _____          
         /\    \                  /\    \         
        /::\    \                /::\____\        
       /::::\    \              /::::|   |        
      /::::::\    \            /:::::|   |        
     /:::/\:::\    \          /::::::|   |        
    /:::/__\:::\    \        /:::/|::|   |        
   /::::\   \:::\    \      /:::/ |::|   |        
  /::::::\   \:::\    \    /:::/  |::|___|______  
 /:::/\:::\   \:::\____\  /:::/   |::::::::\    \ 
/:::/  \:::\   \:::|    |/:::/    |:::::::::\____\
\::/    \:::\  /:::|____|\::/    / ~~~~~/:::/    /
 \/_____/\:::\/:::/    /  \/____/      /:::/    / 
          \::::::/    /               /:::/    /  
           \::::/    /               /:::/    /   
            \::/____/               /:::/    /    
             ~~                    /:::/    /     
                                  /:::/    /      
                                 /:::/    /       
                                 \::/    /        
                                  \/____/       

                                  
2020-02-13 23:03:26.197   INFO     --- [c.m.p.c.ConfigReader          ] : Config file not found,apply default config!
2020-02-13 23:03:26.211   INFO     --- [c.m.p.c.PrimeMinister         ] : 1. Config ready!
2020-02-13 23:03:26.296   DEBUG    --- [c.m.p.c.Scanner               ] : com.pm.usepm.controller.HelloController
2020-02-13 23:03:26.313   INFO     --- [c.m.p.c.Scanner               ] : Mapped url:[/hello] to :com.pm.usepm.controller.HelloController.hello()
2020-02-13 23:03:26.321   DEBUG    --- [c.m.p.c.Scanner               ] : com.pm.usepm.service.HelloService
2020-02-13 23:03:26.326   DEBUG    --- [c.m.p.c.Scanner               ] : com.pm.usepm.UsePm
2020-02-13 23:03:26.327   INFO     --- [c.m.p.c.PrimeMinister         ] : 2. Context ready!
2020-02-13 23:03:26.336   INFO     --- [c.m.p.s.HttpServer            ] : 3. Server listening on 127.0.0.1:8079....
```

2. 访问

```shell
~$ curl http://localhost:8080/pm/index
/html/index.html
```

3. 错误打印

```
2019-03-29 21:24:17.809   INFO     --- [c.m.p.c.Context               ] : URL:/pm/hello
2019-03-29 21:24:17.811   ERROR    --- [c.m.p.c.Context               ] : Cause by: java.lang.reflect.InvocationTargetException


2019-03-29 21:24:17.811   ERROR    --- [c.m.p.c.Context               ] : 
2019-03-29 21:24:17.812   ERROR    --- [c.m.p.c.Context               ] : 

    sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
    sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
    sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
    java.lang.reflect.Method.invoke(Method.java:498)
    com.minister.pm.core.Context.letHandlerInvoke(Context.java:139)
    com.minister.pm.server.DispatchRequest.urlMapper(DispatchRequest.java:39)
    com.minister.pm.server.HttpServer.dispatchRequest(HttpServer.java:84)
    com.minister.pm.server.HttpServer.run(HttpServer.java:58)
    com.minister.pm.core.PrimeMinister.run(PrimeMinister.java:31)
    pmtest.PmTestApplication.main(PmTestApplication.java:16)

```

## Samples

### log

```java
Logger logger = Logger.getLogger(XXClass.class);
logger.info("hello,{}","world"); 
// output like : 2019-03-29 21:24:17.809   INFO     --- [c.m.p.c.Context               ] : hello,world
```

### config

支持类似 yml 格式的配置文件。配置读取例子：

`src/main/resources`下有 `application.yml` , `bootstrap.yml` 或 `config.yml` 三者之一都可被读取，优先级同上列出顺序。
若没有，则应用 pm 内置配置。

```java
@Configuration
public class PMConfig {

    @Value(path = "server.port")
    public static String port;
    
}

// call
PMConfig.port;
```






```

```