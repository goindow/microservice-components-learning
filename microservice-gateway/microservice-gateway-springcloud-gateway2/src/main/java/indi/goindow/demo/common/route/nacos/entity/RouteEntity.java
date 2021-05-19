package indi.goindow.demo.common.route.nacos.entity;

import java.util.ArrayList;
import java.util.List;

public class RouteEntity {
    // id
    private String id;
    // 断言集合
    private List<PredicateEntity> predicates = new ArrayList<>();
    // 过滤器集合
    private List<FilterEntity> filters = new ArrayList<>();
    // 目标 uri
    private String uri;
    // 执行的顺序
    private int order = 0;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<PredicateEntity> getPredicates() {
        return predicates;
    }

    public void setPredicates(List<PredicateEntity> predicates) {
        this.predicates = predicates;
    }

    public List<FilterEntity> getFilters() {
        return filters;
    }

    public void setFilters(List<FilterEntity> filters) {
        this.filters = filters;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}
