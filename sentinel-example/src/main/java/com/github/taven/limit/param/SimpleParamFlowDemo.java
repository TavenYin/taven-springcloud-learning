package com.github.taven.limit.param;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.EntryType;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowItem;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowRuleManager;

import java.util.ArrayList;
import java.util.Collections;

public class SimpleParamFlowDemo {
    private static final String RESOURCE_KEY = "GOODS_DETAIL";

    public static void main(String[] args) {
        if (args == null || args.length == 0) {
            System.err.println("please set args");
            return;
        }

        // 加载限流规则
        loadRules();

        int count = 1_000;
        while (count-- > 0) {
            Entry entry = null;
            try {
                // 调用限流
                entry = SphU.entry(RESOURCE_KEY, EntryType.IN, 1, args[0]);
                // 业务代码...

            } catch (BlockException e) {
                // 当前请求被限流
                e.printStackTrace();
            } catch (Exception ex) {
                // biz exception
                ex.printStackTrace();
            } finally {
                if (entry != null) {
                    entry.exit(1, args[0]);
                }
            }
        }

    }

    private static void loadRules() {
        ParamFlowRule rule = new ParamFlowRule(RESOURCE_KEY)
                .setParamIdx(0) // 指定当前 rule 对应的热点参数索引
                .setGrade(RuleConstant.FLOW_GRADE_QPS) // 限流的维度，该策略针对 QPS 限流
                .setDurationInSec(1) // 限流的单位时间
                .setCount(50) // 未使用指定热点参数时，该资源限流大小为50
                .setParamFlowItemList(new ArrayList<>());

        // item1 设置了对 goods_id = goods_uuid1 的限流，单位时间（DurationInSec）内只能访问10次
        ParamFlowItem item1 = new ParamFlowItem().setObject("goods_uuid1") // 热点参数 value
                .setClassType(String.class.getName()) // 热点参数数据类型
                .setCount(10); // 针对该value的限流值
        // item2 设置了对 goods_id = goods_uuid2 的限流，单位时间（DurationInSec）内只能访问20次
        ParamFlowItem item2 = new ParamFlowItem().setObject("goods_uuid2")
                .setClassType(String.class.getName())
                .setCount(20);
        rule.getParamFlowItemList().add(item1);
        rule.getParamFlowItemList().add(item2);

        ParamFlowRuleManager.loadRules(Collections.singletonList(rule));
    }

}
