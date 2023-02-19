# BeeFarming_BE

## Intro

BF的后端

**框架**： Springboot + Mybatis-Plus

## 开发

### 〇、环境配置

项目JAVA版本统一采用JDK17，IDE采用IDEA

### 一、依赖安装

打开根目录下的`pom.xml`文件，点击`Maven`标签，点击`Install`

### 二、MySQL配置

找到`application.yml`文件，修改数据库配置，主要修改`url`中的数据库名称和端口

推荐在本地使用`docker`安装`mysql`，并使用`Navicat`连接数据库

配置文件示例：

```yml
server:
  port: ${port}

spring:
  application:
    name: BeeFarming_BE
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    name: bee_farming
    url: jdbc:mysql://${ip}:${port}/${dbName}?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull&allowMultiQueries=true
    username: ${username}
    password: "${password}"
  redis:
    database: 1
    host: 127.0.0.1
    port: 6380

mybatis-plus:
  mapper-locations: classpath:/mapper/**/*.xml
  global-config:
    db-config:
      id-type: auto
  configuration:
    auto-mapping-behavior: partial
    map-underscore-to-camel-case: true
  type-enums-package: com.bf

jwt:
  tokenHeader: Authorization
  secret: ${secret}

docker:
  max-containers-num: 45
  max-pids-num: 100
  # ms
  heart-beat: 10000
  # container for server and bot
  container:
    image-name: beefarming_game
    cpu-count: 2
    # MB
    memory: 512
```

### 三、Docker配置

需在本地安装`docker`，并获取最新的BeeFarming镜像，同时开放2375端口

### 四、Redis配置

需在本地安装`redis`，并开放6380端口（可以在`application.yml`中修改）

### 五、运行

找到`BeeFarmingBeApplication.java`文件，点击`Run`即可
