package com.github.taven;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("openFeign")
@EnableFeignClients
public class OpenFeignController {
    private final OpenFeignService openFeignService;

    @Autowired
    public OpenFeignController(OpenFeignService openFeignService) {
        this.openFeignService = openFeignService;
    }

    @RequestMapping(value = "/echo/{str}", method = RequestMethod.GET)
    public String echo(@PathVariable String str) {
        return openFeignService.echo(str);
    }

    @FeignClient("service-provider")
    interface OpenFeignService {
        @RequestMapping("/echo/{str}")
        String echo(@PathVariable(value = "str") String str);
    }

}
