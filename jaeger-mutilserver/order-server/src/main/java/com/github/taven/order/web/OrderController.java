package com.github.taven.order.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@RestController
@Slf4j
public class OrderController {

    private final RestTemplate restTemplate;

    public OrderController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @PostMapping("order/create")
    public String createOrder() {
        try {
            String response = restTemplate.postForObject("localhost:9091/product/reduce", null, String.class);
            log.info(response);
        } catch (RestClientException e) {
            log.error(e.getMessage(), e);
            return "fail";
        }
        return "success";
    }

}
