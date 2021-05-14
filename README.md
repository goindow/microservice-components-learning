## 目录说明
> 所有项目均为多模块聚合项目，方便后期添加具有相同功能的其他组件 demo，推荐按照顺序学习

* [/microservice-discovery，注册中心](https://github.com/goindow/microservice-components-learning/tree/main/microservice-discovery)
  * /microservice-discovery-nacos
    * nacos 实现，服务注册与发现
* [/microservice-config，配置中心](https://github.com/goindow/microservice-components-learning/tree/main/microservice-config)
  * /microservice-config-nacos
    * nacos 实现，动态获取外部配置
* [/microservice-gateway，网关](https://github.com/goindow/microservice-components-learning/tree/main/microservice-gateway)
  * /microservice-gateway-springcloud-gateway
    * springcloud-gateway 实现，整合 nacos discovery 实现默认的动态路由、静态路由
  * /microservice-gateway-springcloud-gateway2
    * springcloud-gateway 实现，整合 nacos discovery/config 实现可配置的动态路由
  * /microservice-gateway-user-service
    * user 测试服务
* /COMPONENTS，依赖组件
  * /nacos，注册中心、配置中心
