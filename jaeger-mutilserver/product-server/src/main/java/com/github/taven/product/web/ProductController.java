package com.github.taven.product.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author tianwen.yin
 */
@RestController
public class ProductController {

    @GetMapping("/product/withoutItem")
    public String productWithout() {
        return "success";
    }

}
