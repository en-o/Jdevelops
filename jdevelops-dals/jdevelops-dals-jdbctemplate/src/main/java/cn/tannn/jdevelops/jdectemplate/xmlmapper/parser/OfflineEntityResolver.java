package cn.tannn.jdevelops.jdectemplate.xmlmapper.parser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * 离线 Entity Resolver
 * 用于在无网环境下解析 XML 文件，避免加载外部 DTD
 *
 * @author tnnn
 */
public class OfflineEntityResolver implements EntityResolver {

    private static final Logger LOG = LoggerFactory.getLogger(OfflineEntityResolver.class);

    /**
     * MyBatis 3.0 Mapper DTD 公共标识符
     */
    private static final String MYBATIS_3_MAPPER_PUBLIC_ID = "-//mybatis.org//DTD Mapper 3.0//EN";

    /**
     * MyBatis 3.0 Mapper DTD 系统标识符（旧版本）
     */
    private static final String MYBATIS_3_MAPPER_SYSTEM_ID_OLD = "http://mybatis.org/dtd/mybatis-3-mapper.dtd";

    /**
     * MyBatis 3.0 Mapper DTD 系统标识符（新版本 HTTPS）
     */
    private static final String MYBATIS_3_MAPPER_SYSTEM_ID_NEW = "https://mybatis.org/dtd/mybatis-3-mapper.dtd";

    @Override
    public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
        LOG.debug("Resolving entity - publicId: {}, systemId: {}", publicId, systemId);

        // 检查是否是 MyBatis Mapper DTD
        if (isMyBatisMapperDtd(publicId, systemId)) {
            LOG.debug("Using offline mode for MyBatis Mapper DTD");
            // 返回一个空的 InputSource，跳过 DTD 验证
            return new InputSource(new ByteArrayInputStream("<?xml version='1.0' encoding='UTF-8'?>".getBytes()));
        }

        // 其他情况使用默认行为
        return null;
    }

    /**
     * 判断是否是 MyBatis Mapper DTD
     *
     * @param publicId 公共标识符
     * @param systemId 系统标识符
     * @return 是否是 MyBatis Mapper DTD
     */
    private boolean isMyBatisMapperDtd(String publicId, String systemId) {
        // 检查公共标识符
        if (MYBATIS_3_MAPPER_PUBLIC_ID.equals(publicId)) {
            return true;
        }

        // 检查系统标识符
        if (systemId != null) {
            return MYBATIS_3_MAPPER_SYSTEM_ID_OLD.equals(systemId)
                    || MYBATIS_3_MAPPER_SYSTEM_ID_NEW.equals(systemId)
                    || systemId.contains("mybatis") && systemId.contains("mapper.dtd");
        }

        return false;
    }
}
