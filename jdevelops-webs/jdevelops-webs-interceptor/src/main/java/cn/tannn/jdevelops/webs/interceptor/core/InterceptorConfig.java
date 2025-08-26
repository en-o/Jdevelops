package cn.tannn.jdevelops.webs.interceptor.core;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * web拦截器包装配置类
 * @author tn
 * @version 1
 * @date 2020/6/19 9:56
 */
@ConfigurationProperties(prefix = "jdevelops.interceptor.core")
public class InterceptorConfig {
    // H2 控制台路径排除列表 - 默认的排除路径
    private static final List<String> DEF_EXCLUDE_PATHS = Arrays.asList(
            "/h2-console",
            "/h2-console/*",
            "/h2/*",
            "/favicon.ico"
    );
    /**
     * 排除拦截的路径
     * <p> 不支持 ant 风格的路径配置
     * <p> 支持一个 * 的方式 最好写绝对路径
     * <p> /h2/* 表示匹配路径 前缀等于 /h2/ 的所有路径
     */
   List<String> excludePaths;

    /**
     * 是否覆盖默认排除路径。
     * <p> 如果为 true，则 getFinalExcludePaths() 只返回用户配置的 excludePaths。
     * <p> 如果为 false，则 getFinalExcludePaths() 返回 默认路径 + 用户配置的路径。
     */
    private boolean overrideDefaultExcludePaths = false;

    public List<String> getExcludePaths() {
        return excludePaths;
    }

    public void setExcludePaths(List<String> excludePaths) {
        this.excludePaths = excludePaths;
    }


    /**
     * 获取最终用于拦截排除的路径列表。
     * <p>根据配置决定是覆盖还是追加。
     *
     * @return 最终的排除路径列表
     */
    public List<String> gainFinalExcludePaths() {
        // 如果用户选择覆盖默认配置，或者用户根本没有配置任何排除路径，则直接返回用户配置的路径（可能为null或空）
        if (overrideDefaultExcludePaths) {
            // 如果用户配置为空，返回空列表，否则返回用户配置的列表
            return excludePaths != null ? new ArrayList<>(excludePaths) : new ArrayList<>();
        }

        // 用户选择追加模式（默认模式）
        // 创建一个新的列表，首先加入所有默认的排除路径
        List<String> allExcludePaths = new ArrayList<>(DEF_EXCLUDE_PATHS);
        // 如果用户配置了额外的路径，则将它们追加到默认路径后面
        if (excludePaths != null && !excludePaths.isEmpty()) {
            allExcludePaths.addAll(excludePaths);
        }
        return allExcludePaths;
    }


    public boolean isOverrideDefaultExcludePaths() {
        return overrideDefaultExcludePaths;
    }

    public void setOverrideDefaultExcludePaths(boolean overrideDefaultExcludePaths) {
        this.overrideDefaultExcludePaths = overrideDefaultExcludePaths;
    }

    @Override
    public String toString() {
        return "InterceptorConfig{" +
                "excludePaths=" + excludePaths +
                ", overrideDefaultExcludePaths=" + overrideDefaultExcludePaths +
                '}';
    }
}
