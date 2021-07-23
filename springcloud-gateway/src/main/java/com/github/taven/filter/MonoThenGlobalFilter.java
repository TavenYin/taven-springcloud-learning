package com.github.taven.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author tianwen.yin
 */
@Component
@Slf4j
public class MonoThenGlobalFilter implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("before routing");
        return chain.filter(exchange)
                .then(Mono.fromRunnable(() -> log.info("after routing")));
    }

    @Override
    public int getOrder() {
        return -2;
    }
}
