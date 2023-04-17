package cn.jdevelops.sboot.jpa.core.auditor;


import java.util.Optional;

/**
 * 审计字段设置更新和新增的人名
 *
 * @author tnnn
 * @version V1.0
 * @date 2023-03-19 18:44
 */

public interface AuditorNameService {

    /**
     * 设置审计人
     * @return String
     */
    Optional<String> settingAuditorName();
}
