package cn.tannn.jdevelops.renewpwd.util;

import org.springframework.context.ApplicationContext;

public class RenewPwdApplicationContextHolder {

    private static ApplicationContext context;

    public RenewPwdApplicationContextHolder(ApplicationContext applicationContext) {
        RenewPwdApplicationContextHolder.context = applicationContext;
    }

    public static ApplicationContext getContext() {
        return context;
    }
}
