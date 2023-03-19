package cn.jdevelops.sboot.jpa.config;

import cn.jdevelops.sboot.jpa.core.AuditorNameService;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author tn
 * @version 1
 * <pre>
 *  @ClassName UserNameAuditorAware
 *  @description   JPA完成审计功能  使用时在自己的像中吧这个复制过去
 *  eg: Application启动类要加注解: @EnableJpaAuditing
 *  监听
 *  @CreatedBy
 *  @LastModifiedBy
 * </pre>
 * 自动注入用户名
 *  其中泛型T可以为String保存用户名，也可以为Long/Integer保存用户ID
 *  正式项目中需要从用户权限模块中获取到当前登录的用户信息
 *
 * @date 2020/5/26 22:29
 */
@Component
public class UserNameAuditorAware implements AuditorAware<String> {


    private final AuditorNameService auditorNameService;

    public UserNameAuditorAware(AuditorNameService auditorNameService) {
        this.auditorNameService = auditorNameService;
    }


    @Override
    public Optional<String> getCurrentAuditor() {
        return auditorNameService.settingAuditorName();
    }

}
