package cn.tannn.jdevelops.utils.validation;


import org.junit.jupiter.api.BeforeAll;
import org.springframework.validation.beanvalidation.LocaleContextMessageInterpolator;

import javax.validation.*;
import javax.validation.bootstrap.GenericBootstrap;
import java.util.Set;

/**
 * 测试 validator 的基类
 */
public class BeastValidatedTest {

    private static Validator validator;

    @BeforeAll
    public static void init() {
        GenericBootstrap bootstrap = Validation.byDefaultProvider();
        Configuration<?> configure = bootstrap.configure();
        MessageInterpolator targetInterpolator = configure.getDefaultMessageInterpolator();
        configure.messageInterpolator(new LocaleContextMessageInterpolator(targetInterpolator));
        ValidatorFactory validatorFactory = configure.buildValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    protected <T> Set<ConstraintViolation<T>> validate(T object, Class<?>... groups) {
        return validator.validate(object, groups);
    }
}
