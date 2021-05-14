## 项目说明
> 所有项目均为多模块聚合项目，方便后期添加其他组件的 demo

* /microservice-discovery
  * /microservice-discovery-nacos，nacos 注册中心
* /microservice-config
  * /microservice-config-nacos，nacos 配置中心
* /microservice-gateway
  * /microservice-gateway-springcloud-gateway，springcloud-gateway 网关，实现静态路由、默认的动态路由
  * /microservice-gateway-springcloud-gateway，springcloud-gateway 网关整合 nacos 配置中心，实现可配置的动态路由
  * /microservice-gateway-user-service，User 服务
