package com.github.taven.filter;

import com.github.taven.utils.SpringReactiveUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@Component
public class ReactiveRedisGlobalFilter implements GlobalFilter, Ordered {
    private ReactiveRedisTemplate<String, String> reactiveRedisTemplate;

    @Autowired
    public void setReactiveRedisTemplate(ReactiveRedisTemplate<String, String> reactiveRedisTemplate) {
        this.reactiveRedisTemplate = reactiveRedisTemplate;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return reactiveRedisTemplate.opsForValue().get("test_key")
                .switchIfEmpty(Mono.error(new RuntimeException("cannot get test_key")))
                .flatMap(value -> {
                    // 此处可以做校验逻辑
                    if (value.equals("error")) {
                        return Mono.error(new RuntimeException("error"));
                    }
                    return Mono.just(value);
                })
                .publishOn(SpringReactiveUtils.reactorHttpNioScheduler())
                .then(chain.filter(exchange));
    }

    @Override
    public int getOrder() {
        return -3;
    }
}
