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

### 三、Docker配置

需在本地安装`docker`，并获取最新的BeeFarming镜像，同时开放2375端口

### 四、Redis配置

需在本地安装`redis`，并开放6380端口（可以在`application.yml`中修改）

### 五、运行

找到`BeeFarmingBeApplication.java`文件，点击`Run`即可
