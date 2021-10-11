package com.github.taven.order.web;

import io.opentracing.Tracer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


@RestController
@Slf4j
public class OrderController {
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private Tracer tracer;

    @GetMapping("/order/withoutItem")
    public String withoutItem() {
        String url = "http://localhost:9091/product/withoutItem";
        String rpcResponse = restTemplate.getForObject(url, String.class);
        log.info("rpcResponse: {}", rpcResponse);
        return "success";
    }

    @GetMapping("/order/withItem")
    public String withItem() {
        tracer.activeSpan().setBaggageItem("global_data", "666");
        String url = "http://localhost:9091/product/withItem";
        return restTemplate.getForObject(url, String.class);
    }

}
