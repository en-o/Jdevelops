package cn.tannn.jdevelops.frameworks.web.starter.util;

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
     *
     * <p>
     *      （1）? 匹配一个字符（除过操作系统默认的文件分隔符）
     *      （2）* 匹配0个或多个字符
     *      （3）**匹配0个或多个目录
     *      （4）{spring:[a-z]+} 将正则表达式[a-z]+匹配到的值,赋值给名为 spring 的路径变量.
     *      (PS:必须是完全匹配才行,在SpringMVC中只有完全匹配才会进入controller层的方法)
     * </p>
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
