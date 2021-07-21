package com.github.taven.balance;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.gateway.config.LoadBalancerProperties;
import org.springframework.cloud.gateway.filter.LoadBalancerClientFilter;
import org.springframework.cloud.netflix.ribbon.RibbonLoadBalancerClient;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import java.net.URI;
import java.util.Objects;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR;

@Component
public class MyLoadBalancerClientFilter extends LoadBalancerClientFilter {
    private static final Logger log = LoggerFactory.getLogger(MyLoadBalancerClientFilter.class);
    private static final String BALANCE_CLUSTER_HEADER = "balance_cluster";
    private static final String BALANCE_CLUSTER_HEADER_DEFAULT = "DEFAULT";

    private RibbonLoadBalancerClient ribbonLoadBalancerClient;

    @Autowired(required = false)
    public void setRibbonLoadBalancerClient(RibbonLoadBalancerClient ribbonLoadBalancerClient) {
        this.ribbonLoadBalancerClient = ribbonLoadBalancerClient;
    }

    @Autowired
    public MyLoadBalancerClientFilter(LoadBalancerClient loadBalancer, LoadBalancerProperties properties) {
        super(loadBalancer, properties);
    }

    /**
     * 通过重写该方法和自定义 IRule, 可以定制负载均衡策略
     *
     * @param exchange
     * @return
     */
    @Override
    protected ServiceInstance choose(ServerWebExchange exchange) {
        if (ribbonLoadBalancerClient == null) {
            return super.choose(exchange);
        } else {
            String serviceId = getServiceId(exchange);
            BalanceRequestInfo balanceRequestInfo = new BalanceRequestInfo();
            balanceRequestInfo.setServiceId(serviceId);
            balanceRequestInfo.setTargetCluster(
                    exchange
                            .getRequest()
                            .getHeaders()
                            .toSingleValueMap()
                            .getOrDefault(BALANCE_CLUSTER_HEADER, BALANCE_CLUSTER_HEADER_DEFAULT)
            );
            return ribbonLoadBalancerClient.choose(serviceId, balanceRequestInfo);
        }
    }

    private String getServiceId(ServerWebExchange exchange) {
        Object url = exchange.getAttribute(GATEWAY_REQUEST_URL_ATTR);
        if (Objects.nonNull(url)) {
            return ((URI) url).getHost();
        } else {
            throw new RuntimeException("cannot get serviceId");
        }
    }

}
