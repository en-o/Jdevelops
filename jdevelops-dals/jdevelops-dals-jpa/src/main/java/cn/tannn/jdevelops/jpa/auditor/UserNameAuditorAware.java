package cn.tannn.jdevelops.jpa.auditor;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

/**
 *
 *  JPA完成审计功能
 *  <p> 通过 AuditorNameService 获取 当前操作人，默认{@link DefAuditorNameService}
 * @author tn
 * @version 1
 * @date 2020/5/26 22:29
 */
@EnableJpaAuditing
@ConditionalOnMissingBean(UserNameAuditorAware.class)
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
