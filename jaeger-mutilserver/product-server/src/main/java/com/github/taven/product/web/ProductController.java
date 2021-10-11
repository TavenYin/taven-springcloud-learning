package com.github.taven.product.web;

import io.opentracing.Tracer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author tianwen.yin
 */
@RestController
public class ProductController {
    @Autowired
    private Tracer tracer;

    @GetMapping("/product/withoutItem")
    public String withoutItem() {
        return "success";
    }

    @GetMapping("/product/withItem")
    public String withItem() {
        return tracer.activeSpan().getBaggageItem("global_data");
    }

}
