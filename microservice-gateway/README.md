## 网关
包含常用的网关示例

### 外部依赖
* nacos

### 模块说明
* /microservice-gateway-springcloud-gateway
  * springcloud-gateway 实现，整合 nacos discovery 实现默认的动态路由（将服务自动注册到路由）、静态路由，`使用 8080 端口`
* /microservice-gateway-springcloud-gateway2
  * springcloud-gateway 实现，整合 nacos discovery & config 实现可配置的动态路由，`使用 8080 端口`
* /microservice-gateway-user-service
  * user 测试服务，`使用 8081 端口`
* *todo: 其他实现...*

### 用例
* 【**CASE 1**】整合 discovery 实现默认的动态路由（将服务自动注册到路由）、静态路由
  * 运行 ../COMPONENTS/nacos & /microservice-gateway-user-service 及 ./microservice-gateway-springcloud-gateway 服务
  * 点击 [localhost:8081/user/info](http://localhost:8081/user/info)，查看 user 服务是否启动成功 
  * 点击 [localhost:8848/nacos/#/serviceManagement](http://localhost:8848/nacos/#/serviceManagement) 进入 nacos 管理后台，查看服务列表是否包含如下服务 *microservice-gateway-user-service*、*microservice-gateway-springcloud-gateway*
  * 点击，如下地址，查看网关是否路由成功
    * [localhost:8080/user/info](http://localhost:8080/user/info)，**静态路由**
    * [localhost:8080/microservice-gateway-user-service/user/info](http://localhost:8080/microservice-gateway-user-service/user/info)，**默认的动态路由（将服务自动注册到路由）**
    * [localhost:8080/actuator/gateway/routes](http://localhost:8080/actuator/gateway/routes)，**网关端点路由表**
> 如果需要测试 gateway 的负载均衡功能，将 microservice-gateway-user-service 编译打包后，使用不同端口运行多个服务，`java -jar user-service.jar --server.port=[8081|8091]`
* 【**CASE 2**】整合 discovery & config 实现动态路由（可配置）
  * 运行 ../COMPONENTS/nacos
  * 点击 [localhost:8848/nacos/#/configurationManagement](http://localhost:8848/nacos/#/configurationManagement) 进入 nacos 管理后台，添加如下配置（空路由）
      * microservice-gateway-springcloud-gateway2-dynamic-route.json
        ```json
        {
            "refreshGatewayRoute":false,
            "routeList":[]
        }
        ```
  * 运行 /microservice-gateway-user-service 及 ./microservice-gateway-springcloud-gateway2 服务
  * 点击 [localhost:8081/user/info](http://localhost:8081/user/info)，查看 user 服务是否启动成功
  * 点击 [localhost:8848/nacos/#/serviceManagement](http://localhost:8848/nacos/#/serviceManagement) 进入 nacos 管理后台，查看服务列表是否包含如下服务 *microservice-gateway-user-service*、*microservice-gateway-springcloud-gateway2*
  * 点击 [localhost:8080/user/info](http://localhost:8080/user/info)，**此处显示错误页(Whitelabel Error Page) - 路由还未配置**，接下来在 config 中配置路由
  * 点击 [localhost:8848/nacos/#/configurationManagement](http://localhost:8848/nacos/#/configurationManagement) 进入 nacos 管理后台，添加如下配置（动态配置路由）
    * microservice-gateway-springcloud-gateway2-dynamic-route.json
      ```json
      {
          "refreshGatewayRoute":true,
          "routeList":[
              {
                  "id":"user-serice",
                  "predicates":[
                      {
                          "name":"Path",
                          "args":{
                              "pattern":"/user/**"
                          }
                      }
                  ],
                  "filters":[],
                  "uri":"lb://microservice-gateway-user-service",
                  "order":0
              }
          ]
      }
      ```
  * 点击，如下地址，查看网关是否路由成功
      * [localhost:8080/user/info](http://localhost:8080/user/info)，**监听配置中心，动态更新路由**
      * [localhost:8080/actuator/gateway/routes](http://localhost:8080/actuator/gateway/routes)，**网关端点路由表**