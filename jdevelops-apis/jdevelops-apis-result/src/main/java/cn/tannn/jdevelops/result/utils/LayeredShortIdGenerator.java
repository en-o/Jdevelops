package cn.tannn.jdevelops.result.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 层级短ID生成工具
 * 依赖现有 IdGenerator 生成基础短ID，再扩展层级语义。
 * <p>
 * 示例：
 * 文章: A1290WBHYAAAAAAACCG6ZA
 * 页面1: A1290WBHYAAAAAAACCG6ZA-P1
 * 段落2: A1290WBHYAAAAAAACCG6ZA-P1-SEC2
 * 父级: A1290WBHYAAAAAAACCG6ZA-P1
 * 解析: [A1290WBHYAAAAAAACCG6ZA, P1, SEC2]
 */
public class LayeredShortIdGenerator {

    private static final Pattern PATH_PATTERN = Pattern.compile("^([a-zA-Z0-9\\-]+?)(?:-(P\\d+|SEC\\d+)(?:-(SEC\\d+))*)?$");


    private final IdGenerator idGenerator;

    public LayeredShortIdGenerator(IdGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }

    /**
     * 创建文章短ID（根级）
     */
    public String createArticleId() {
        return idGenerator.nextId(null, IdGenerator.Type.ARTICLE).toString();
    }

    /**
     * 基于文章ID创建页面ID
     */
    public String createPageId(String articleId, int pageIndex) {
        return articleId + "-P" + pageIndex;
    }

    /**
     * 基于页面ID创建段落ID
     */
    public String createParagraphId(String pageId, int paragraphIndex) {
        return pageId + "-SEC" + paragraphIndex;
    }

    /**
     * 获取父级ID
     */
    public static String getParentId(String id) {
        int idx = id.lastIndexOf("-");
        if (idx == -1) return null;
        return id.substring(0, idx);
    }

    public static List<String> parseHierarchy(String id) {
        List<String> parts = new ArrayList<>();
        if (id == null || id.isEmpty()) {
            return parts;
        }

        // 直接基于 - 拆分
        String[] split = id.split("-");
        StringBuilder rootBuilder = new StringBuilder();
        for (String seg : split) {
            // 如果遇到带字母的分段（例如 P1、SEC2），说明层级从此开始
            if (seg.matches("P\\d+|SEC\\d+")) {
                // 把之前拼好的 root 作为第一层
                if (!rootBuilder.isEmpty() && parts.isEmpty()) {
                    parts.add(rootBuilder.toString());
                }
                parts.add(seg);
            } else if (parts.isEmpty()) {
                // 根层UUID部分拼起来
                if (!rootBuilder.isEmpty()) rootBuilder.append("-");
                rootBuilder.append(seg);
            } else {
                // 继续追加非标准段
                parts.add(seg);
            }
        }

        if (parts.isEmpty() && !rootBuilder.isEmpty()) {
            parts.add(rootBuilder.toString());
        }
        return parts;
    }


}
