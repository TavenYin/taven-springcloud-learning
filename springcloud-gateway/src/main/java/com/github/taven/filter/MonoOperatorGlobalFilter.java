package com.github.taven.filter;

import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscription;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.CoreSubscriber;
import reactor.core.publisher.Mono;
import reactor.core.publisher.MonoOperator;

import java.util.function.Function;

//@Component
@Slf4j
public class MonoOperatorGlobalFilter implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        exchange.getRequest().getHeaders().toSingleValueMap();
        return chain.filter(exchange).transform(new Function<Mono<Void>, Publisher<Void>>() {
            @Override
            public Publisher<Void> apply(Mono<Void> publisher) {
                return new MonoOperator<Void, Void>(publisher) {
                    @Override
                    public void subscribe(CoreSubscriber<? super Void> actual) {
                        source.subscribe(new CoreSubscriber<Void>() {
                            @Override
                            public void onSubscribe(Subscription s) {
                                log.info("before routing");
                                actual.onSubscribe(s);
                            }

                            @Override
                            public void onNext(Void unused) {
                                log.info("onNext");
                                actual.onNext(unused);
                            }

                            @Override
                            public void onError(Throwable t) {
                                actual.onError(t);
                            }

                            @Override
                            public void onComplete() {
                                log.info("after routing");
                                actual.onComplete();
                            }
                        });
                    }
                };
            }
        });
    }

    @Override
    public int getOrder() {
        return Integer.MIN_VALUE;
    }
}
