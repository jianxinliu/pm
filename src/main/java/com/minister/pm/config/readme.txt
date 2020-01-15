默认配置

读取配置，类似SpringBoot的配置读取。

1.启动时：
    读取配置文件，若没有，读取默认配置文件，将配置文件解析成 Context 中的数据,configObjects
2.扫描注解时：
    若是 @Configuration ,则以此根据属性上的 path 解析，去 Context 中取对应值赋值给对应属性(注意类型转换)。