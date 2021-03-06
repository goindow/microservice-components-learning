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
     * ????????????
     * */
    @Bean
    public void refresh() throws NacosException {
        Properties properties = new Properties();
        properties.put(PropertyKeyConst.SERVER_ADDR, serverAddr);
        properties.put(PropertyKeyConst.NAMESPACE, namespace);
        ConfigService configService = NacosFactory.createConfigService(properties);
        // ??????????????????????????????????????????
        init(configService);
        // ??????????????????????????????
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
     * ???????????????
     * */
    private void init(ConfigService configService) throws NacosException {
        String routeConfig = configService.getConfig(dataId, group, 60000);
        List<RouteEntity> list = JSON.parseArray(JSONObject.parseObject(routeConfig).getString("routeList")).toJavaList(RouteEntity.class);
        for (RouteEntity route : list) {
            save(parseRouteDefinition(route));
        }
    }

    /**
     * ????????????
     * */
    private void update(RouteDefinition routeDefinition) {
        delete(routeDefinition);
        save(routeDefinition);
    }

    /**
     * ????????????
     * */
    private void delete(RouteDefinition routeDefinition) {
        try {
            routeDefinitionWriter.delete(Mono.just(routeDefinition.getId()));
        }catch (Exception e){
            // todo: handle e
        }
    }

    /**
     * ????????????
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
     * ????????????
     * */
    private RouteDefinition parseRouteDefinition(RouteEntity routeEntity) {
        RouteDefinition routeDefinition = new RouteDefinition();
        // id
        routeDefinition.setId(routeEntity.getId());
        // ??????
        List<PredicateDefinition> predicateDefinitionList = new ArrayList<>();
        for (PredicateEntity predicateEntity: routeEntity.getPredicates()) {
            PredicateDefinition predicateDefinition = new PredicateDefinition();
            predicateDefinition.setArgs(predicateEntity.getArgs());
            predicateDefinition.setName(predicateEntity.getName());
            predicateDefinitionList.add(predicateDefinition);
        }
        routeDefinition.setPredicates(predicateDefinitionList);
        // ?????????
        List<FilterDefinition> filterDefinitionList = new ArrayList<>();
        for (FilterEntity filterEntity: routeEntity.getFilters()) {
            FilterDefinition filterDefinition = new FilterDefinition();
            filterDefinition.setArgs(filterEntity.getArgs());
            filterDefinition.setName(filterEntity.getName());
            filterDefinitionList.add(filterDefinition);
        }
        routeDefinition.setFilters(filterDefinitionList);
        // ?????? uri
        URI uri = UriComponentsBuilder.fromUriString(routeEntity.getUri()).build().toUri();
        routeDefinition.setUri(uri);
        // ????????????
        routeDefinition.setOrder(routeEntity.getOrder());
        return routeDefinition;
    }
}
