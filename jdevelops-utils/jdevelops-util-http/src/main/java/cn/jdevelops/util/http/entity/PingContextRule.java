package cn.jdevelops.util.http.entity;

/**
 * ping 有效规则
 * @author tan
 * @date 2023-05-09 13:54:11
 */
public class PingContextRule {


    /**
     * 页面内容规则（根据type和rule判断接口返回值内容数据是否为存在定义的内容，从而验证接口的有效性）
     * 逗号隔开为多个
     */
    private String rule;

    /**
     * 规则类型（1.包含(默认)，2.不包含）
     */
    private Integer ruleType;

    public PingContextRule(String rule, Integer ruleType) {
        this.rule = rule;
        this.ruleType = ruleType;
    }

    @Override
    public String toString() {
        return "PingContextRule{" +
                "rule='" + rule + '\'' +
                ", ruleType=" + ruleType +
                '}';
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public Integer getRuleType() {
        return ruleType;
    }

    public void setRuleType(Integer ruleType) {
        this.ruleType = ruleType;
    }
}
