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
import java.util.List;

public class MutilParamFlow {
    public static void main(String[] args) {
        initParamFlowRules();
        while (true) {
            Entry entry = null;
            try {
                entry = SphU.entry("RESOURCE_KEY", EntryType.IN, 1, "yin", "han");
                // Add pass for parameter.
                System.out.println("ok");
            } catch (BlockException e) {
                // block.incrementAndGet();
                System.out.println("block");
                break;
            } catch (Exception ex) {
                // biz exception
                ex.printStackTrace();
            } finally {
                // total.incrementAndGet();
                if (entry != null) {
                    entry.exit(1, "yin", "han");
                }
            }
        }
    }


    private static void initParamFlowRules() {
        ParamFlowRule rule = new ParamFlowRule("RESOURCE_KEY")
                .setParamIdx(0)
                .setGrade(RuleConstant.FLOW_GRADE_QPS)
                .setCount(10)
                ;

        ParamFlowItem item = new ParamFlowItem().setObject("yin")
                .setClassType(String.class.getName())
                .setCount(500);
        rule.getParamFlowItemList().add(item);

        ParamFlowRule rule2 = new ParamFlowRule("RESOURCE_KEY")
                .setParamIdx(1)
                .setGrade(RuleConstant.FLOW_GRADE_QPS)
                .setCount(300)
                ;
        ParamFlowItem item2 = new ParamFlowItem().setObject("han")
                .setClassType(String.class.getName())
                .setCount(5);
        rule2.getParamFlowItemList().add(item2);

        List<ParamFlowRule> rules = new ArrayList<>();
        rules.add(rule);
        rules.add(rule2);
        ParamFlowRuleManager.loadRules(rules);
    }
}
