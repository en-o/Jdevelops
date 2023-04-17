package cn.jdevelops.sboot.jpa.core.auditor;

import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * 重新实现并设置：@Primary
 * 审计字段设置更新和新增的人名
 * @author tnnn
 * @version V1.0
 * @date 2023-03-19 18:46
 */
@Service
public class AuditorNameServiceImpl implements AuditorNameService {
    @Override
    public Optional<String> settingAuditorName() {
        return Optional.of("admin");
    }
}
