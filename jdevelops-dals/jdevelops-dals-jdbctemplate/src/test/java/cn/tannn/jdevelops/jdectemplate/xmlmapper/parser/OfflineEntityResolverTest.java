package cn.tannn.jdevelops.jdectemplate.xmlmapper.parser;

import cn.tannn.jdevelops.jdectemplate.xmlmapper.model.XmlMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 测试离线 DTD 解析功能
 *
 * @author tnnn
 */
class OfflineEntityResolverTest {

    /**
     * 测试解析包含在线 DTD 引用的 XML 文件
     * 验证在无网环境下也能正常解析
     */
    @Test
    void testParseXmlWithOnlineDtd() throws Exception {
        XmlMapperParser parser = new XmlMapperParser();

        // 解析测试用的 UserMapper.xml，它使用在线 DTD 引用
        String xmlPath = "classpath:jmapper/UserMapper.xml";
        XmlMapper xmlMapper = parser.parse(xmlPath);

        // 验证解析成功
        assertNotNull(xmlMapper);
        assertEquals("cn.tannn.jdevelops.jdectemplate.xmlmapper.example.UserMapper",
                     xmlMapper.getNamespace());

        // 验证 SQL 片段被正确解析
        assertNotNull(xmlMapper.getSqlFragment("baseColumns"));

        // 验证 SQL 语句被正确解析
        assertNotNull(xmlMapper.getSqlStatement("findById"));
        assertNotNull(xmlMapper.getSqlStatement("findUsers"));
        assertNotNull(xmlMapper.getSqlStatement("insertUser"));
        assertNotNull(xmlMapper.getSqlStatement("updateUser"));
        assertNotNull(xmlMapper.getSqlStatement("deleteById"));

        System.out.println("✅ 测试通过：成功解析包含在线 DTD 引用的 XML 文件");
    }

    /**
     * 测试 EntityResolver 是否正确识别在线 DTD
     */
    @Test
    void testEntityResolverDetectsOnlineDtd() throws Exception {
        OfflineEntityResolver resolver = new OfflineEntityResolver();

        // 测试 HTTP 协议的 DTD
        var inputSource1 = resolver.resolveEntity(
            "-//mybatis.org//DTD Mapper 3.0//EN",
            "http://mybatis.org/dtd/mybatis-3-mapper.dtd"
        );
        assertNotNull(inputSource1, "应该拦截 HTTP 协议的在线 DTD");

        // 测试 HTTPS 协议的 DTD
        var inputSource2 = resolver.resolveEntity(
            "-//mybatis.org//DTD Mapper 3.0//EN",
            "https://mybatis.org/dtd/mybatis-3-mapper.dtd"
        );
        assertNotNull(inputSource2, "应该拦截 HTTPS 协议的在线 DTD");

        // 测试本地路径不应该被拦截
        var inputSource3 = resolver.resolveEntity(
            "-//mybatis.org//DTD Mapper 3.0//EN",
            "classpath:dtd/mybatis-3-mapper.dtd"
        );
        assertNull(inputSource3, "不应该拦截本地路径的 DTD");

        System.out.println("✅ 测试通过：EntityResolver 正确识别在线和本地 DTD");
    }

    /**
     * 测试扫描并解析多个 XML 文件
     */
    @Test
    void testScanAndParse() throws Exception {
        XmlMapperParser parser = new XmlMapperParser();

        // 扫描并解析所有 Mapper XML 文件
        var mappers = parser.scanAndParse("classpath*:jmapper/**/*Mapper.xml");

        // 验证解析成功
        assertNotNull(mappers);
        assertTrue(mappers.size() > 0, "应该至少解析到一个 Mapper 文件");

        // 验证包含 UserMapper
        assertTrue(mappers.containsKey("cn.tannn.jdevelops.jdectemplate.xmlmapper.example.UserMapper"),
                  "应该包含 UserMapper");

        System.out.println("✅ 测试通过：成功扫描并解析多个 XML 文件");
        System.out.println("📋 解析到的 Mapper 数量: " + mappers.size());
        mappers.keySet().forEach(namespace ->
            System.out.println("  - " + namespace)
        );
    }
}
