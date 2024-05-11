package cn.tannn.jdevelops.jpa.spel;

import cn.tannn.jdevelops.jpa.constant.CommonStateExpressionRoot;
import org.springframework.data.spel.spi.EvaluationContextExtension;
import org.springframework.stereotype.Component;

/**
 * 常用状态
 * @see https://www.yuque.com/tanning/mbquef/cec6kl44g1axru20?singleDoc# 《扩展 SpEL（Spring Expression Language）表达式的上下文》
 *
 * @see  https://github.com/spring-projects/spring-security/blob/main/data/src/main/java/org/springframework/security/data/repository/query/SecurityEvaluationContextExtension.java
 * @see jpa repository https://github.com/spring-projects/spring-data-examples/blob/main/jpa/security/src/main/java/example/springdata/jpa/security/SecureBusinessObjectRepository.java
 *
 * @see EvaluationContextExtension
 * @author <a href="https://tannn.cn/">tan</a>
 * @date 2023/11/16 13:56
 */
public class CommonStateEvaluationContextExtension implements EvaluationContextExtension {

    /**
     * 当前扩展的名字
     */
    @Override
    public String getExtensionId() {
        return "common_state";
    }

    /**
     * Return the root object to be exposed by the extension. It's strongly recommended to declare the most concrete type
     * possible as return type of the implementation method. This will allow us to obtain the necessary metadata once and
     * not for every evaluation.
     *
     * @return the root object to be exposed by the extension.
     */
    @Override
    public CommonStateExpressionRoot getRootObject() {
        // 构建自己一个 上下文对象， 后面的 Repository 可以从这里取值
        return new CommonStateExpressionRoot();
    }
}
