package cn.tannn.jdevelops.result.utils;

import org.junit.jupiter.api.Test;

class LayeredIdGeneratorTest {

    @Test
    void testAll() {
        /**
         * 简单演示
         */
        IdGenerator core = IdGenerator.createAuto();
        LayeredIdGenerator layered = new LayeredIdGenerator(core);

        String article = layered.createArticleId();
        String page1 = layered.createPageId(article, 1);
        String para2 = layered.createParagraphId(page1, 2);

        System.out.println("文章: " + article);
        System.out.println("页面1: " + page1);
        System.out.println("段落2: " + para2);
        System.out.println("父级: " + LayeredIdGenerator.getParentId(para2));
        System.out.println("解析: " + LayeredIdGenerator.parseHierarchy(para2));
    }
}
