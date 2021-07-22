package com.github.taven.route;

import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.cloud.nacos.NacosConfigProperties;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.Executor;

/**
 * 动态路由
 *
 * 原理：监听 Nacos 配置，发生变化时，发布 RefreshRoutesEvent 让 Spring Cloud 更新路由
 */
@Component
public class NacosRouteDefinitionRepository implements RouteDefinitionRepository {
    private final NacosConfigManager nacosConfigManager;
    private List<RouteDefinition> routeDefinitions;
    private final String routeDataId;

    @Autowired
    public NacosRouteDefinitionRepository(NacosConfigProperties nacosConfig,
                                          ApplicationEventPublisher publisher,
                                          Environment env) {
        nacosConfigManager = new NacosConfigManager(nacosConfig);
        routeDataId = env.getProperty("dynamic-route.dataId");
        initRouting(nacosConfig);
        addListener(nacosConfig, publisher);
    }

    private void initRouting(NacosConfigProperties nacosConfig) {
        try {
            String routeText = nacosConfigManager.getConfigService().getConfig(routeDataId, nacosConfig.getGroup(), 1000);
            routeDefinitions = JSONObject.parseArray(routeText, RouteDefinition.class);
        } catch (NacosException e) {
            e.printStackTrace();
        }
    }

    private void addListener(NacosConfigProperties nacosConfig, ApplicationEventPublisher publisher) {
        try {
            nacosConfigManager.getConfigService().addListener(routeDataId, nacosConfig.getGroup(), new Listener() {
                @Override
                public Executor getExecutor() {
                    return null;
                }

                @Override
                public void receiveConfigInfo(String configInfo) {
                    routeDefinitions = JSONObject.parseArray(configInfo, RouteDefinition.class);
                    publisher.publishEvent(new RefreshRoutesEvent(this));
                }
            });
        } catch (NacosException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Flux<RouteDefinition> getRouteDefinitions() {
        return Flux.fromIterable(routeDefinitions);
    }

    @Override
    public Mono<Void> save(Mono<RouteDefinition> route) {
        return null;
    }

    @Override
    public Mono<Void> delete(Mono<String> routeId) {
        return null;
    }
}
