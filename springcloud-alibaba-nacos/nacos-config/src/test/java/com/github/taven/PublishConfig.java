package com.github.taven;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.PropertyKeyConst;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;

import java.util.Properties;

public class PublishConfig {
    public static void main(String[] args) throws NacosException {
        String serverAddr = "localhost";
        String dataId = "nacos-config-dev.yaml";
        String group = "nacos-config-group";
        Properties properties = new Properties();
        properties.put(PropertyKeyConst.SERVER_ADDR, serverAddr);
        ConfigService configService = NacosFactory.createConfigService(properties);

        String content = "demo:\n" +
                "    username: taven\n" +
                "    password: abcd\n" +
                "    age: 20";

        boolean isPublishOk = configService.publishConfig(dataId, group, content);
        System.out.println(isPublishOk);
    }
}
