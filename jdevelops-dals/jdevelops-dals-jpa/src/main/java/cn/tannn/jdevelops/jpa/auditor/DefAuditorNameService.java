package cn.tannn.jdevelops.jpa.auditor;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * 重新实现
 * 审计字段设置更新和新增的人名
 * @author tnnn
 * @version V1.0
 * @date 2023-03-19 18:46
 */
@ConditionalOnMissingBean(AuditorNameService.class)
public class DefAuditorNameService implements AuditorNameService {

    // TODO 后面默认改造成从 HttpServletRequest 里拿IP
    @Override
    public Optional<String> settingAuditorName() {
        return Optional.of("admin");
    }
}
