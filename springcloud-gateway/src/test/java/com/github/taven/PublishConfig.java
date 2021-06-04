package com.github.taven;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.PropertyKeyConst;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;

import java.util.Properties;

public class PublishConfig {
    public static void main(String[] args) throws NacosException {
        String serverAddr = "localhost";
        String dataId = "gateway-route";
        String group = "gateway-demo";
        Properties properties = new Properties();
        properties.put(PropertyKeyConst.SERVER_ADDR, serverAddr);
        ConfigService configService = NacosFactory.createConfigService(properties);

        String content = "[\n" +
                "    {\n" +
                "\t\t\"filters\": [],\n" +
                "\t\t\"id\": \"service-provider-demo\",\n" +
                "\t\t\"order\": -101,\n" +
                "\t\t\"predicates\": [\n" +
                "\t\t\t{\n" +
                "\t\t\t\t\"args\": {\n" +
                "\t\t\t\t\t\"pattern\": \"/echo/**\"\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"name\": \"Path\"\n" +
                "\t\t\t}\n" +
                "\t\t],\n" +
                "\t\t\"uri\": \"lb://service-provider\"\n" +
                "\t}\n" +
                "]";

        boolean isPublishOk = configService.publishConfig(dataId, group, content);
        System.out.println(isPublishOk);
    }
}
