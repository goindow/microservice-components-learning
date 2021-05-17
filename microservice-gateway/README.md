## 网关
包含常用的网关示例

### 外部依赖
* nacos

### 模块说明
* /microservice-gateway-springcloud-gateway
  * springcloud-gateway 实现，整合 nacos discovery 实现默认的动态路由、静态路由
* /microservice-gateway-springcloud-gateway2
  * springcloud-gateway 实现，整合 nacos discovery/config 实现可配置的动态路由
* /microservice-gateway-user-service
  * user 测试服务
* *todo: 其他实现...*

### 用例
* 【**CASE 1**】运行 ../COMPONENTS/nacos & /microservice-gateway-user-service 及 ./microservice-gateway-springcloud-gateway 服务
  * 点击 [localhost:8081/user/info](http://localhost:8081/user/info)，查看服务是否启动成功 
  * 点击 [http://localhost:8848/nacos/#/serviceManagement](http://localhost:8848/nacos/#/serviceManagement) 进入 nacos 管理后台，查看服务列表是否包含如下服务 *microservice-gateway-user-service*、*microservice-gateway-springcloud-gateway*
  * 点击 [localhost:8080/user/info](http://localhost:8081/user/info)，查看网关是否路由成功
> 如果需要测试 gateway 的负载均衡功能，将 microservice-gateway-user-service 编译打包后，使用不同端口运行多个服务
