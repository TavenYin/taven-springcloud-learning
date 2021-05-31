package com.github.taven;

import java.util.Properties;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;

public class NamingRegister {

    public static void main(String[] args) throws NacosException {

        Properties properties = new Properties();
        properties.setProperty("serverAddr", "localhost:8848");

        NamingService naming = NamingFactory.createNamingService(properties);

        naming.registerInstance("productService", "2.2.2.2", 9999, "DEFAULT");

    }
}
