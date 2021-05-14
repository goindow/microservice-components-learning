## 目录说明
> 所有项目均为多模块聚合项目，方便后期添加具有相同功能的其他组件 demo，推荐按照顺序学习

* [/microservice-discovery](https://github.com/goindow/microservice-components-learning/tree/main/microservice-discovery)
  * /microservice-discovery-nacos
    * nacos 注册中心
* [/microservice-config](https://github.com/goindow/microservice-components-learning/tree/main/microservice-config)
  * /microservice-config-nacos
    * nacos 配置中心
* [/microservice-gateway](https://github.com/goindow/microservice-components-learning/tree/main/microservice-gateway)
  * /microservice-gateway-springcloud-gateway
    * springcloud-gateway 网关，整合 nacos discovery，实现默认的动态路由、静态路由
  * /microservice-gateway-springcloud-gateway2
    * springcloud-gateway 网关，整合 nacos discovery/config，实现可配置的动态路由
  * /microservice-gateway-user-service
    * User 服务
