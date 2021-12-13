package com.github.taven.rest.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

/**
 * @author tianwen.yin
 */
@RestController
public class TestController {
    @Resource(name = "customCloudRestTemplate")
    private RestTemplate customCloudRestTemplate;

    @Resource(name = "customTimeoutRestTemplate")
    private RestTemplate customTimeoutRestTemplate;

    @Resource(name = "restTemplate")
    private RestTemplate restTemplate;

    @GetMapping("custom/normalRequest")
    public String customNormalRequest() {
        return null;
    }

    @GetMapping("custom/setTimeout")
    public String customSetTimeout() {
        return null;
    }

    @GetMapping("cloud/setTimeout")
    public String cloudSetTimeout() {
        return null;
    }

    @GetMapping("rest/normalRequest")
    public String restNormalRequest() {
        return null;
    }

}
