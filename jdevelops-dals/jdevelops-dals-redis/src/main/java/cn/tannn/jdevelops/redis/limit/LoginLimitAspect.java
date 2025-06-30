package cn.tannn.jdevelops.redis.limit;

import cn.tannn.jdevelops.exception.built.LoginLimitException;
import cn.tannn.jdevelops.exception.built.UserException;
import jakarta.annotation.Resource;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import javax.security.auth.login.LoginException;
import java.util.Objects;

/**
 * 等解决了加密传输的问题在继续
 */
//@Aspect
//@Component
//@Order(1)
public class LoginLimitAspect {
    private static final Logger LOG = LoggerFactory.getLogger(LoginLimitAspect.class);

    @Resource
    private LoginLimitService loginLimitService;


    @Around("@annotation(loginLimit)")
    public Object around(ProceedingJoinPoint point, LoginLimit loginLimit) throws Throwable {
        String spel = loginLimit.spel();
        if (Objects.nonNull(spel)) {
            return point.proceed();
        }
        // todo如果是加密的数据，需要先解密
        // 获取SpEL表达式的值
        String username = getSpelValue(point, spel);
        if (Objects.isNull(username)) {
            throw new LoginException("请正确设置账户名！");
        }

        // 验证登录限制
        if (loginLimit.limit() == -1) {
            loginLimitService.verify(username, loginLimit.responseStatus());
        } else {
            loginLimitService.verify(username, loginLimit.responseStatus(), loginLimit.limit());
        }
        try {
            return point.proceed();
        } catch (Exception e) {
            // 记录错误
            if (loginLimit.expireTime() == -1) {
                loginLimitService.limit(username);
            } else {
                loginLimitService.limit(username, loginLimit.expireTime());
            }
            throw e;
        }
    }

    private String getSpelValue(ProceedingJoinPoint point, String spel) {
        EvaluationContext context = new StandardEvaluationContext();
        MethodSignature signature = (MethodSignature) point.getSignature();
        String[] paramNames = signature.getParameterNames();
        Object[] args = point.getArgs();
        for (int i = 0; i < paramNames.length; i++) {
            context.setVariable(paramNames[i], args[i]);
        }
        Expression expression = new SpelExpressionParser().parseExpression(spel);
        return expression.getValue(context, String.class);
    }

}
