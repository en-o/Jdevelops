package cn.jdevelops.sboot.web.util;

import org.springframework.util.AntPathMatcher;

import java.util.Set;

/**
 * url util
 *
 * @author tnnn
 * @version V1.0
 * @date 2022-07-23 23:59
 */
public class UrlUtil {

    /**
     * 不需要记录的url
     * 默认有了  /error /swagger /v2/api-docs /v3/api-docs
     * @param url 需要判断的url
     * @param excludePathPatterns 不记录的url集合
     * @return boolean 真不记录
     */
    public static boolean noRecordUrl(String url,Set<String> excludePathPatterns ) {
        excludePathPatterns.add("/error");
        excludePathPatterns.add("/swagger");
        excludePathPatterns.add("/swagger-resources/**");
        excludePathPatterns.add("/v2/api-docs");
        excludePathPatterns.add("/v3/api-docs");
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        return excludePathPatterns.stream().anyMatch(e ->antPathMatcher.match(e,url));

    }
}
