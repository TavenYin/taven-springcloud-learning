package com.github.taven.order.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


@RestController
@Slf4j
public class OrderController {

    private final RestTemplate restTemplate;

    public OrderController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/order/withoutItem")
    public String orderWithout() {
        String rpcResponse = restTemplate.getForObject("http://localhost:9091/product/withoutItem", String.class);
        log.info("rpcResponse: {}", rpcResponse);
        return "success";
    }

}
