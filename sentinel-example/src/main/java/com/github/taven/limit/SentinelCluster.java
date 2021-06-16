package com.github.taven.limit;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.cluster.client.config.ClusterClientAssignConfig;
import com.alibaba.csp.sentinel.cluster.client.config.ClusterClientConfig;
import com.alibaba.csp.sentinel.cluster.client.config.ClusterClientConfigManager;
import com.alibaba.csp.sentinel.slots.block.BlockException;

public class SentinelCluster {
    public static void main(String[] args) {
        initFlowRules();

        while (true) {
            // 1.5.0 版本开始可以直接利用 try-with-resources 特性
            try (Entry entry = SphU.entry("HelloWorld")) {
                // 被保护的逻辑
                System.out.println("hello world");
            } catch (BlockException ex) {
                // 处理被流控的逻辑
                System.out.println("blocked!");
                ex.printStackTrace(System.out);
            }
        }
    }

    private static void initFlowRules() {
        ClusterClientConfig clientConfig = new ClusterClientConfig();
        ClusterClientConfigManager.applyNewConfig(clientConfig);

        ClusterClientAssignConfig assignConfig = new ClusterClientAssignConfig();
        assignConfig.setServerHost("127.0.0.1");
        assignConfig.setServerPort(11111);
        ClusterClientConfigManager.applyNewAssignConfig(assignConfig);

    }
}
