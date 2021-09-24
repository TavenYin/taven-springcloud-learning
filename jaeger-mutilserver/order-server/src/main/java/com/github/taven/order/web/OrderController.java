package com.github.taven.order.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Controller
@Slf4j
public class OrderController {

    @Autowired
    private RestTemplate restTemplate;

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
