## 配置中心
包含常用的配置中心示例

### 外部依赖
* nacos

### 模块说明
* /microservice-config-nacos
  * nacos 实现
* *todo: 其他实现...*

### 用例
* 【**CASE 1**】运行 ../COMPONENTS/nacos & ./microservice-config-nacos 服务
  * 点击 [localhost:8848/nacos/#/configurationManagement](http://localhost:8848/nacos/#/configurationManagement) 进入 nacos 管理后台，添加如下配置
    * microservice-config-nacos.yaml 
      ```yaml
      config:
        info: "user service from nacos-config@[microservice-config-nacos.yaml]"
      ```
    * microservice-config-nacos-dev.yaml
      ```yaml
      config:
        info: "user service from nacos-config@[microservice-config-nacos-dev.yaml]"
      ```
  * 点击 [localhost:8080/user/info](http://localhost:8080/user/info)，查看返回值是否是 *"user service from nacos-config@[microservice-config-nacos.yaml]"*
  * 修改 spring.profiles.active=dev，查看配置是否自动更新到 dev 环境值
    * application.yml
      ```yaml
      server:
        port: 8080
      spring:
        application:
          name: microservice-config-nacos
        profiles:
          active: dev
      ```
  * 点击 [localhost:8080/user/info](http://localhost:8080/user/info)，查看返回值是否是 *"user service from nacos-config@[microservice-config-nacos-dev.yaml]"*

