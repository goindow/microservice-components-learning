## 注册中心
包含常用的注册中心示例

### 外部依赖
* nacos

### 模块说明
* /microservice-discovery-nacos
  * nacos 实现
* *todo: 其他实现...*

### 用例
* 【**CASE 1**】简单的服务注册
  * 运行 ../COMPONENTS/nacos & ./microservice-discovery-nacos 服务
  * 点击 [localhost:8080/user/info](http://localhost:8080/user/info)，查看服务是否启动成功 
  * 点击 [http://localhost:8848/nacos/#/serviceManagement](http://localhost:8848/nacos/#/serviceManagement) 进入 nacos 管理后台，查看服务列表是否包含名为 *microservice-discovery-nacos* 的服务
  