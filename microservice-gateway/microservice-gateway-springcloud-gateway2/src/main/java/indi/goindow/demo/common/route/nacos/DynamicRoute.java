package indi.goindow.demo.common.route.nacos;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.PropertyKeyConst;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import indi.goindow.demo.common.route.nacos.entity.FilterEntity;
import indi.goindow.demo.common.route.nacos.entity.PredicateEntity;
import indi.goindow.demo.common.route.nacos.entity.RouteEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Executor;

@Component
public class DynamicRoute implements ApplicationEventPublisherAware {

    @Value("${dynamic.route.server-addr}")
    private String serverAddr;

    @Value("${dynamic.route.namespace}")
    private String namespace;

    @Value("${dynamic.route.group}")
    private String group;

    @Value("${dynamic.route.data-id}")
    private String dataId;

    @Autowired
    private RouteDefinitionWriter routeDefinitionWriter;

    private ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher)  {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    /**
     * 刷新路由
     * */
    @Bean
    public void refresh() throws NacosException {
        Properties properties = new Properties();
        properties.put(PropertyKeyConst.SERVER_ADDR, serverAddr);
        properties.put(PropertyKeyConst.NAMESPACE, namespace);
        ConfigService configService = NacosFactory.createConfigService(properties);
        // 网关服务启动时，更新一次路由
        init(configService);
        // 监听配置中心路由变化
        configService.addListener(dataId, group, new Listener() {
            @Override
            public Executor getExecutor() {
                return null;
            }
            @Override
            public void receiveConfigInfo(String routeConfig) {
                boolean refreshGatewayRoute = JSONObject.parseObject(routeConfig).getBoolean("refreshGatewayRoute");
                if (refreshGatewayRoute) {
                    List<RouteEntity> list = JSON.parseArray(JSONObject.parseObject(routeConfig).getString("routeList")).toJavaList(RouteEntity.class);
                    for (RouteEntity route : list) {
                        update(parseRouteDefinition(route));
                    }
                }
            }
        });
    }

    /**
     * 初始化路由
     * */
    private void init(ConfigService configService) throws NacosException {
        String routeConfig = configService.getConfig(dataId, group, 60000);
        List<RouteEntity> list = JSON.parseArray(JSONObject.parseObject(routeConfig).getString("routeList")).toJavaList(RouteEntity.class);
        for (RouteEntity route : list) {
            save(parseRouteDefinition(route));
        }
    }

    /**
     * 更新路由
     * */
    private void update(RouteDefinition routeDefinition) {
        delete(routeDefinition);
        save(routeDefinition);
    }

    /**
     * 删除路由
     * */
    private void delete(RouteDefinition routeDefinition) {
        try {
            routeDefinitionWriter.delete(Mono.just(routeDefinition.getId()));
        }catch (Exception e){
            // todo: handle e
        }
    }

    /**
     * 保存路由
     * */
    private void save(RouteDefinition routeDefinition) {
        try {
            routeDefinitionWriter.save(Mono.just(routeDefinition)).subscribe();
            applicationEventPublisher.publishEvent(new RefreshRoutesEvent(this));
        }catch (Exception e){
            // todo: handle e
        }
    }

    /**
     * 路由解析
     * */
    private RouteDefinition parseRouteDefinition(RouteEntity routeEntity) {
        RouteDefinition routeDefinition = new RouteDefinition();
        // id
        routeDefinition.setId(routeEntity.getId());
        // 断言
        List<PredicateDefinition> predicateDefinitionList = new ArrayList<>();
        for (PredicateEntity predicateEntity: routeEntity.getPredicates()) {
            PredicateDefinition predicateDefinition = new PredicateDefinition();
            predicateDefinition.setArgs(predicateEntity.getArgs());
            predicateDefinition.setName(predicateEntity.getName());
            predicateDefinitionList.add(predicateDefinition);
        }
        routeDefinition.setPredicates(predicateDefinitionList);
        // 过滤器
        List<FilterDefinition> filterDefinitionList = new ArrayList<>();
        for (FilterEntity filterEntity: routeEntity.getFilters()) {
            FilterDefinition filterDefinition = new FilterDefinition();
            filterDefinition.setArgs(filterEntity.getArgs());
            filterDefinition.setName(filterEntity.getName());
            filterDefinitionList.add(filterDefinition);
        }
        routeDefinition.setFilters(filterDefinitionList);
        // 目标 uri
        URI uri = UriComponentsBuilder.fromUriString(routeEntity.getUri()).build().toUri();
        routeDefinition.setUri(uri);
        // 执行顺序
        routeDefinition.setOrder(routeEntity.getOrder());
        return routeDefinition;
    }
}
