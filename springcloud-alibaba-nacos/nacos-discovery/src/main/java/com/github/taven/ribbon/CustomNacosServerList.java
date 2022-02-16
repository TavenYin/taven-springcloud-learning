package com.github.taven.ribbon;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.ribbon.NacosServer;
import com.alibaba.cloud.nacos.ribbon.NacosServerList;

import java.util.List;

/**
 * @author tianwen.yin
 */
public class CustomNacosServerList extends NacosServerList {
    public CustomNacosServerList(NacosDiscoveryProperties discoveryProperties) {
        super(discoveryProperties);
    }

    @Override
    public List<NacosServer> getUpdatedListOfServers() {
        List<NacosServer> servers = super.getUpdatedListOfServers();
        if (servers != null && !servers.isEmpty()) {
            for (NacosServer server : servers) {
                server.setZone(server.getInstance().getClusterName());
            }
        }
        return servers;
    }
}
