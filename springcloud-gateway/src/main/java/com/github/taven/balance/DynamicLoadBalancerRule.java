package com.github.taven.balance;

import com.alibaba.cloud.nacos.ribbon.ExtendBalancer;
import com.alibaba.cloud.nacos.ribbon.NacosServer;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.ribbon.RibbonClientConfiguration;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义 Ribbon 负载均衡策略
 * <br/>
 * 原理：实现 IRule
 * <br/>
 * IRule 何时注册到 Ribbon 中的？
 * <br/>
 * 参考 {@link RibbonClientConfiguration#ribbonLoadBalancer(IClientConfig, ServerList, ServerListFilter, IRule, IPing, ServerListUpdater)}
 *
 */
@Component
public class DynamicLoadBalancerRule extends AbstractLoadBalancerRule {
    @Autowired
    private SpringClientFactory springClientFactory;

    @Override
    public void initWithNiwsConfig(IClientConfig iClientConfig) {
    }

    @Override
    public Server choose(Object key) {
        // 如果请求 Header 中含有 Nacos 服务的集群标志，路由到指定的 Nacos 服务集群
        BalanceRequestInfo balanceRequestInfo = (BalanceRequestInfo) key;
//        List<Server> reachableServers = getLoadBalancer().getReachableServers();
        List<Server> reachableServers = springClientFactory
                .getLoadBalancer(balanceRequestInfo.getServiceId())
                .getReachableServers();
        List<Instance> instances = new ArrayList<>();

        String targetCluster = balanceRequestInfo.getTargetCluster();

        for (Server server : reachableServers) {
            if (server instanceof NacosServer) {
                NacosServer nacosServer = (NacosServer) server;
                if (targetCluster.equals(nacosServer.getInstance().getClusterName())) {
                    instances.add(nacosServer.getInstance());
                }
            }
        }

        Instance instance;
        return (instance = ExtendBalancer.getHostByRandomWeight2(instances)) != null ? new NacosServer(instance) : null;
    }
}
