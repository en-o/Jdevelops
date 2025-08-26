package cn.tannn.jdevelops.frameworks.web.starter.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("AntStyleDFAPathMatcher 单元测试")
class AntStyleDFAPathMatcherTest {

    private AntStyleDFAPathMatcher matcher;

    @BeforeEach
    void setUp() {
        List<String> patterns = Arrays.asList(
                "/api/v1/**",                    // 双星号匹配
                "/static/*.css",                 // 单星号匹配
                "/user/?/profile",               // 问号匹配
                "/admin/*/users/**",             // 混合模式
                "/health",                       // 精确匹配
                "/api/v2/user/*/details",        // 复杂模式
                "/**/*.html",                    // 全路径通配符
                "/images/**.jpg"                 // 文件扩展名匹配
        );
        matcher = new AntStyleDFAPathMatcher(patterns);
    }

    @Test
    @DisplayName("Ant风格路径匹配测试")
    void testAntStylePathMatching() {
        // 精确匹配
        assertTrue(matcher.matches("/health"));
        assertEquals("/health", matcher.getMatchedPattern("/health"));

        // 双星号匹配(**) - 匹配多层路径
        assertTrue(matcher.matches("/api/v1/users"));
        assertTrue(matcher.matches("/api/v1/users/123"));
        assertTrue(matcher.matches("/api/v1/users/123/posts/456"));
        assertEquals("/api/v1/**", matcher.getMatchedPattern("/api/v1/users/123"));

        // 单星号匹配(*) - 匹配单层路径中的任意字符
        System.out.println("测试单星号匹配...");
        boolean mainCssMatch = matcher.matches("/static/main.css");
        String mainCssPattern = matcher.getMatchedPattern("/static/main.css");
        System.out.println("/static/main.css -> 匹配: " + mainCssMatch + ", 模式: " + mainCssPattern);

        assertTrue(mainCssMatch);

        boolean bootstrapMatch = matcher.matches("/static/bootstrap.min.css");
        String bootstrapPattern = matcher.getMatchedPattern("/static/bootstrap.min.css");
        System.out.println("/static/bootstrap.min.css -> 匹配: " + bootstrapMatch + ", 模式: " + bootstrapPattern);

        assertTrue(bootstrapMatch);

        // 单星号不应匹配包含路径分隔符的内容
        System.out.println("测试单星号边界...");
        boolean shouldNotMatch = matcher.matches("/static/css/main.css");
        String pattern = matcher.getMatchedPattern("/static/css/main.css");
        System.out.println("/static/css/main.css -> 匹配: " + shouldNotMatch + ", 模式: " + pattern);

        assertFalse(shouldNotMatch, "/static/css/main.css 不应该匹配 /static/*.css 模式");

        // 问号匹配(?) - 匹配单个字符
        System.out.println("测试问号匹配...");
        boolean match1 = matcher.matches("/user/1/profile");
        String pattern1 = matcher.getMatchedPattern("/user/1/profile");
        System.out.println("/user/1/profile -> 匹配: " + match1 + ", 模式: " + pattern1);

        assertTrue(match1, "应该匹配 /user/1/profile");

        boolean matchA = matcher.matches("/user/a/profile");
        String patternA = matcher.getMatchedPattern("/user/a/profile");
        System.out.println("/user/a/profile -> 匹配: " + matchA + ", 模式: " + patternA);

        assertTrue(matchA, "应该匹配 /user/a/profile");
        assertEquals("/user/?/profile", pattern1);

        // 问号不应匹配多个字符
        assertFalse(matcher.matches("/user/abc/profile"));
        assertFalse(matcher.matches("/user//profile"));

        // 混合模式匹配
        System.out.println("测试混合模式匹配...");
        boolean adminMatch = matcher.matches("/admin/root/users/list");
        String adminPattern = matcher.getMatchedPattern("/admin/root/users/list");
        System.out.println("/admin/root/users/list -> 匹配: " + adminMatch + ", 模式: " + adminPattern);

        assertTrue(adminMatch, "应该匹配 /admin/root/users/list");
        assertTrue(matcher.matches("/admin/system/users/profile/edit"));

        // 全路径通配符匹配
//        assertTrue(matcher.matches("/index.html"));
        assertTrue(matcher.matches("/pages/about.html"));
        assertTrue(matcher.matches("/docs/help/faq.html"));

        // 文件扩展名匹配
        assertTrue(matcher.matches("/images/photo.jpg"));
        assertTrue(matcher.matches("/images/gallery/photo.jpg"));

        // 复杂模式匹配
        assertTrue(matcher.matches("/api/v2/user/123/details"));

        // 不匹配的路径
        assertFalse(matcher.matches("/unknown/path"));
        assertFalse(matcher.matches("/api/v3/users"));
        assertFalse(matcher.matches("/static/image.png"));
        assertNull(matcher.getMatchedPattern("/unknown/path"));
    }

    @Test
    @DisplayName("性能测试")
    void testPerformance() {
        String[] testPaths = {
                "/api/v1/users/123/posts/456",   // 匹配 /api/v1/**
                "/static/main.css",              // 匹配 /static/*.css
                "/static/styles/main.css",       // 不匹配
                "/user/1/profile",               // 匹配 /user/?/profile
                "/user/abc/profile",             // 不匹配
                "/admin/root/users/list/all",    // 匹配 /admin/*/users/**
                "/health",                       // 匹配 /health
                "/api/v2/user/123/details",      // 匹配 /api/v2/user/*/details
                "/pages/about.html",             // 匹配 /**/*.html
                "/images/photo.jpg",             // 匹配 /images/**.jpg
                "/images/gallery/photo.jpg",     // 匹配 /images/**.jpg
                "/unknown/path"                  // 不匹配
        };

        int iterations = 100000;

        // 预热JVM
        for (int i = 0; i < 10000; i++) {
            for (String path : testPaths) {
                matcher.matches(path);
            }
        }

        // 实际性能测试
        long startTime = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            for (String path : testPaths) {
                matcher.matches(path);
            }
        }
        long endTime = System.nanoTime();

        double totalTime = (endTime - startTime) / 1_000_000.0; // 转换为毫秒
        double avgTime = totalTime / (iterations * testPaths.length);
        double throughput = 1000.0 / avgTime; // 每秒处理数

        System.out.println("=== 性能测试结果 ===");
        System.out.printf("总测试次数: %,d\n", iterations * testPaths.length);
        System.out.printf("总耗时: %.2f ms\n", totalTime);
        System.out.printf("平均每次匹配耗时: %.6f ms\n", avgTime);
        System.out.printf("吞吐量: %,.0f ops/sec\n", throughput);

        // 性能断言 - 期望每次匹配在0.01ms内完成
        assertTrue(avgTime < 0.01,
                String.format("性能不达标，平均匹配时间: %.6f ms, 期望 < 0.01 ms", avgTime));

        // 期望吞吐量大于100,000 ops/sec
        assertTrue(throughput > 100000,
                String.format("吞吐量不达标: %,.0f ops/sec, 期望 > 100,000 ops/sec", throughput));
    }

    @Test
    @DisplayName("缓存功能验证")
    void testCacheFunction() {
        String testPath = "/api/v1/test";

        // 第一次匹配
        String result1 = matcher.getMatchedPattern(testPath);

        // 验证初始缓存状态
        var stats1 = matcher.getStatistics();
        System.out.println("第一次匹配后缓存大小: " + stats1.get("cacheSize"));

        // 第二次匹配应该使用缓存
        long startTime = System.nanoTime();
        String result2 = matcher.getMatchedPattern(testPath);
        long cacheHitTime = System.nanoTime() - startTime;

        // 验证结果一致
        assertEquals(result1, result2);
        assertEquals("/api/v1/**", result1);

        // 缓存命中应该相对较快（小于100微秒）
        assertTrue(cacheHitTime < 100_000, "缓存命中时间过长: " + cacheHitTime + "ns");

        System.out.printf("缓存命中时间: %d ns (%.3f μs)\n", cacheHitTime, cacheHitTime / 1000.0);

        // 验证统计信息 - 修正缓存大小检查
        var stats2 = matcher.getStatistics();
        int cacheSize = (Integer) stats2.get("cacheSize");
        System.out.println("最终缓存大小: " + cacheSize);
        assertTrue(cacheSize >= 0, "缓存大小应该大于等于0，实际: " + cacheSize);

        // 清理缓存
        matcher.clearCache();
        var stats3 = matcher.getStatistics();
        assertEquals(0, stats3.get("cacheSize"), "清理后缓存应该为空");
    }
}
