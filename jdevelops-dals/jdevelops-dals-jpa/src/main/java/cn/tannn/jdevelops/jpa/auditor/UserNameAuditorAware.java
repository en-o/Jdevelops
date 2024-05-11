package cn.tannn.jdevelops.jpa.auditor;

import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 *
 *  JPA完成审计功能
 *  <p> 通过 AuditorNameService 获取 当前操作人，默认{@link DefAuditorNameService}
 * @author tn
 * @version 1
 * @date 2020/5/26 22:29
 */
@Component
@EnableJpaAuditing
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
