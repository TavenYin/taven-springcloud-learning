package com.github.taven.limit;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.cluster.client.config.ClusterClientAssignConfig;
import com.alibaba.csp.sentinel.cluster.client.config.ClusterClientConfig;
import com.alibaba.csp.sentinel.cluster.client.config.ClusterClientConfigManager;
import com.alibaba.csp.sentinel.cluster.flow.rule.ClusterFlowRuleManager;
import com.alibaba.csp.sentinel.datasource.Converter;
import com.alibaba.csp.sentinel.datasource.ReadableDataSource;
import com.alibaba.csp.sentinel.datasource.nacos.NacosDataSource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class SentinelClusterEmbedded {
    public static void main(String[] args) {
        initFlowRules();
        Random random = new Random();

        while (true) {
            // 1.5.0 版本开始可以直接利用 try-with-resources 特性
            try (Entry entry = SphU.entry("cluster-resource")) {
                // 被保护的逻辑
                System.out.println("hello world");
                TimeUnit.MILLISECONDS.sleep(random.nextInt(50));
            } catch (BlockException ex) {
                // 处理被流控的逻辑
                System.out.println("blocked!");
                ex.printStackTrace(System.out);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static void initFlowRules() {
//        ClusterClientConfig clientConfig = new ClusterClientConfig();
//        ClusterClientConfigManager.applyNewConfig(clientConfig);

//        ClusterClientAssignConfig assignConfig = new ClusterClientAssignConfig();
//        assignConfig.setServerHost("127.0.0.1");
//        assignConfig.setServerPort(11111);
//        ClusterClientConfigManager.applyNewAssignConfig(assignConfig);

        String REMOTE_ADDRESS = "localhost:8848";
        String GROUP_ID = "SENTINEL_GROUP";
        String APP_NAME = "appA";
        String FLOW_POSTFIX = "-flow-rules";
        // 使用 Nacos 数据源作为配置中心，需要在 REMOTE_ADDRESS 上启动一个 Nacos 的服务
        ReadableDataSource<String, List<FlowRule>> ds =
                new NacosDataSource<>(REMOTE_ADDRESS, GROUP_ID, APP_NAME+FLOW_POSTFIX,
                        new Converter<String, List<FlowRule>>() {
                            @Override
                            public List<FlowRule> convert(String source) {
                                List<FlowRule> flowRules = JSON.parseObject(source, new TypeReference<List<FlowRule>>() {
                                });
//                                flowRules.forEach(flowRule -> {
//                                    flowRule.setLimitApp("com.github.taven.limit.SentinelCluster");
//                                });
                                return flowRules;
                            }
                        })
                ;
        // 为集群客户端注册动态规则源
        FlowRuleManager.register2Property(ds.getProperty());
    }
}
