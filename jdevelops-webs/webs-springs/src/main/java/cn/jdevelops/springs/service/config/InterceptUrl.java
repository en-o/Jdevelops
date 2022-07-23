package cn.jdevelops.springs.service.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * UrlService 用到的bean
 * @author tn
 * @date 2020-09-30 16:58
 */
@ConfigurationProperties(prefix = "jdevelops.web.geturl")
@Component
@Getter
@Setter
@ToString
public class InterceptUrl {

    /**
     * 不需要记录的url
     */
    private Set<String> excludePathPatterns = new HashSet<>();

}
