package com.github.taven;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;

import java.util.Properties;

public class PublishConfigExample {
    public static void main(String[] args) throws NacosException {
        // 初始化配置服务，控制台通过示例代码自动获取下面参数
        String serverAddr = "localhost:8848";
        // 配置 ID，采用类似 package.class（如 com.taobao.tc.refund.log.level）的命名规则保证全局唯一性。
        // 建议根据配置的业务含义来定义 class 部分。
        // 全部字符均为小写。
        // 只允许英文字符和 4 种特殊字符（“.”、“:”、“-”、“_”），不超过 256 字节
        String dataId = "com.github.taven.example";
        // 配置分组，建议填写产品名:模块名（如 Nacos:Test）来保证唯一性。
        // 只允许英文字符和 4 种特殊字符（“.”、“:”、“-”、“_”），不超过 128 字节。
        String group = "example:java-sdk";
        Properties properties = new Properties();
        properties.put("serverAddr", serverAddr);
        properties.put("namespace", "c6048dd2-fc02-4a8a-9cc1-28ca2f337db7"); // 可选，默认为public
        ConfigService configService = NacosFactory.createConfigService(properties);
        boolean isPublishOk = configService.publishConfig(dataId, group, "this is config222");
        System.out.println(isPublishOk);
    }
}
