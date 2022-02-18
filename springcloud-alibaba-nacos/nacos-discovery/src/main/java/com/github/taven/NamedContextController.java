package com.github.taven;

import com.github.taven.namedcontext.NamedHttpClient;
import com.github.taven.namedcontext.NamedHttpClientFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author tianwen.yin
 */
@RestController
@RequestMapping("namedContext")
public class NamedContextController {

    @Autowired
    private NamedHttpClientFactory namedHttpClientFactory;

    @RequestMapping("/test")
    public String namedContextTest(String name) {
        NamedHttpClient namedHttpClient = namedHttpClientFactory.getInstance(name, NamedHttpClient.class);
        return "name:" + namedHttpClient.getServiceName()
                + ", socketTimeout:" + namedHttpClient.getRequestConfig().getSocketTimeout();
    }

}
