package com.github.taven.ribbon;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.ServerList;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.netflix.ribbon.RibbonClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author tianwen.yin
 */
// 解开注释，会替换 Ribbon 的默认Bean
//@Configuration
//@RibbonClients(defaultConfiguration = DefaultRibbonConfig.class)
public class RibbonClientConfiguration {

}

class DefaultRibbonConfig {
    @Bean
    @ConditionalOnMissingBean
    public ServerList<?> ribbonServerList(IClientConfig config,
                                                    NacosDiscoveryProperties nacosDiscoveryProperties) {
        CustomNacosServerList serverList = new CustomNacosServerList(nacosDiscoveryProperties);
        serverList.initWithNiwsConfig(config);
        return serverList;
    }
}
