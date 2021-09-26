package com.github.taven.order.web;

import io.opentracing.Scope;
import io.opentracing.Span;
import io.opentracing.Tracer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;

@RestController
@Slf4j
public class OrderController {

    private final RestTemplate restTemplate;

    private final Tracer tracer;

    public OrderController(RestTemplate restTemplate, Tracer tracer) {
        this.restTemplate = restTemplate;
        this.tracer = tracer;
    }

    @GetMapping("/order/withoutItem")
    public String orderWithout(HttpServletRequest request) {
        String traceId;

        String operationName = request.getMethod() + ":" + request.getRequestURI();
        Span span = tracer.buildSpan(operationName)
                .start();
        try (Scope scope = tracer.activateSpan(span)) {
            String response = restTemplate.getForObject("http://localhost:9091/product/withoutItem", String.class);
            log.info(response);
        } finally {
            span.finish();
            traceId = span.context().toTraceId();
        }
        return traceId;
    }

}
