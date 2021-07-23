package com.github.taven.route;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.GatewayFilterSpec;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.gateway.route.builder.UriSpec;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

/**
 * @author tianwen.yin
 */
@Configuration
public class CodeDefinedRoute {

    @Bean
    public RouteLocator myRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(p -> p
                        .path("/hello-gateway")
                        .filters(f -> f.addRequestHeader("Hello", "World")
                                .rewritePath("/hello-gateway", "/get")
                        )
                        .uri("https://httpbin.org"))
                .route(p -> p
                        .path("/modify-body")
                        .filters(this::modifyRequestBody)
                        .uri("https://httpbin.org")
                )
                .build();
    }

    public UriSpec modifyRequestBody(GatewayFilterSpec gatewayFilterSpec) {
        return gatewayFilterSpec.modifyRequestBody(String.class, Map.class, MediaType.APPLICATION_JSON_VALUE,
                        (exchange, oldBody) -> {
                            System.out.println("old request body:" + oldBody);
                            Map<String, Object> map = new HashMap<>();
                            map.put("newRequestBody", "modify");
                            return Mono.just(map);
                        })
                .rewritePath("/modify-body", "/post")
                .modifyResponseBody(String.class, Map.class, MediaType.APPLICATION_JSON_VALUE,
                        (exchange, oldBody) -> {
                            System.out.println("old response body:" + oldBody);
                            Map<String, Object> map = new HashMap<>();
                            map.put("newResponseBody", "modify");
                            return Mono.just(map);
                        });
    }

}
