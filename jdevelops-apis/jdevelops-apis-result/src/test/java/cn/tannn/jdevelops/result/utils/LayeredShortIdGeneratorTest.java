package cn.tannn.jdevelops.result.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LayeredShortIdGeneratorTest {

    @Test
    void testAll() {
        /**
         * 简单演示
         */
        IdGenerator core = IdGenerator.createAuto();
        LayeredShortIdGenerator layered = new LayeredShortIdGenerator(core);

        String article = layered.createArticleId();
        String page1 = layered.createPageId(article, 1);
        String para2 = layered.createParagraphId(page1, 2);

        System.out.println("文章: " + article);
        System.out.println("页面1: " + page1);
        System.out.println("段落2: " + para2);
        System.out.println("父级: " + LayeredShortIdGenerator.getParentId(para2));
        System.out.println("解析: " + LayeredShortIdGenerator.parseHierarchy(para2));
    }
}
