package cn.tannn.jdevelops.result.utils;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class IdGeneratorTest {

    @Test
    void testAll() {

        long exampleEpoch = Instant.parse("2024-01-01T00:00:00Z").toEpochMilli();
        IdGenerator g = new IdGenerator(1, 1, exampleEpoch);

        System.out.println("=== 创建文章 ===");
        UUID articleId = g.nextId(null, IdGenerator.Type.ARTICLE);
        System.out.println("文章UUID: " + articleId);
        System.out.println("短链接: " + IdGenerator.toBase64Url(articleId));
        System.out.println("解析结果: " + g.parse(articleId));

        System.out.println("\n=== 创建页面 ===");
        UUID pageId1 = g.nextId(articleId, IdGenerator.Type.PAGE);
        UUID pageId2 = g.nextId(articleId, IdGenerator.Type.PAGE);
        System.out.println("页面1 UUID: " + pageId1);
        System.out.println("页面2 UUID: " + pageId2);
        System.out.println("页面1解析: " + g.parse(pageId1));

        System.out.println("\n=== 创建段落 ===");
        UUID paraId1 = g.nextId(pageId1, IdGenerator.Type.PARAGRAPH);
        UUID paraId2 = g.nextId(pageId1, IdGenerator.Type.PARAGRAPH);
        System.out.println("段落1 UUID: " + paraId1);
        System.out.println("段落2 UUID: " + paraId2);
        System.out.println("段落1解析: " + g.parse(paraId1));

        System.out.println("\n=== 父子关系验证 ===");
        System.out.println("文章 -> 页面1: " + g.verifyParentChild(articleId, pageId1));
        System.out.println("文章 -> 页面2: " + g.verifyParentChild(articleId, pageId2));
        System.out.println("页面1 -> 段落1: " + g.verifyParentChild(pageId1, paraId1));
        System.out.println("页面1 -> 段落2: " + g.verifyParentChild(pageId1, paraId2));
        System.out.println("页面2 -> 段落1 (应该false): " + g.verifyParentChild(pageId2, paraId1));

        System.out.println("\n=== 类型层级验证 ===");
        System.out.println("文章 -> 页面1 (类型正确): " + g.verifyTypeHierarchy(articleId, pageId1));
        System.out.println("页面1 -> 段落1 (类型正确): " + g.verifyTypeHierarchy(pageId1, paraId1));

        System.out.println("\n=== 快速提取信息 ===");
        System.out.println("段落1类型: " + IdGenerator.extractType(paraId1));
        System.out.println("段落1父级哈希: 0x" + Integer.toHexString(IdGenerator.extractParentHash(paraId1)));

        System.out.println("\n=== Base64URL往返测试 ===");
        String encoded = IdGenerator.toBase64Url(paraId1);
        UUID decoded = IdGenerator.fromBase64Url(encoded);
        System.out.println("原始: " + paraId1);
        System.out.println("编码: " + encoded + " (长度: " + encoded.length() + ")");
        System.out.println("解码: " + decoded);
        System.out.println("匹配: " + paraId1.equals(decoded));
    }
}
