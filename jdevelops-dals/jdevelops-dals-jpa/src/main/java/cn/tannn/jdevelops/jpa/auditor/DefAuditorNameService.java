package cn.tannn.jdevelops.jpa.auditor;

import cn.tannn.jdevelops.jpa.utils.IpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
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

    private static final Logger LOG = LoggerFactory.getLogger(DefAuditorNameService.class);

    @Resource
    private HttpServletRequest request;


    @Override
    public Optional<String> settingAuditorName() {
        String author = "admin";
        try {
            author = IpUtil.httpRequestIp(request);
        }catch (Exception e){
            LOG.error("settingAuditorName get request ip error : {}", e.getMessage());
        }
        return Optional.of(author);
    }
}
