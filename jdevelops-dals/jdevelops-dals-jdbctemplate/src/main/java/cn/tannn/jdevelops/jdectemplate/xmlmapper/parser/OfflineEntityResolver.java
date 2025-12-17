package cn.tannn.jdevelops.jdectemplate.xmlmapper.parser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;

/**
 * 离线 Entity Resolver
 * <p>
 * 用于在无网环境下解析 XML 文件，自动将外部 DTD 引用替换为本地 DTD 文件。
 * 支持以下几种方式：
 * <ul>
 *     <li>自动处理在线 DTD 引用：http://mybatis.org/dtd/mybatis-3-mapper.dtd</li>
 *     <li>自动处理在线 DTD 引用：https://mybatis.org/dtd/mybatis-3-mapper.dtd</li>
 *     <li>支持本地 DTD 引用：如果 XML 中已经使用本地路径，则不做处理</li>
 * </ul>
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
     * MyBatis 3.0 Mapper DTD 系统标识符（HTTP）
     */
    private static final String MYBATIS_3_MAPPER_SYSTEM_ID_HTTP = "http://mybatis.org/dtd/mybatis-3-mapper.dtd";

    /**
     * MyBatis 3.0 Mapper DTD 系统标识符（HTTPS）
     */
    private static final String MYBATIS_3_MAPPER_SYSTEM_ID_HTTPS = "https://mybatis.org/dtd/mybatis-3-mapper.dtd";

    /**
     * 本地 DTD 文件路径（使用 Class.getResourceAsStream 读取，需要以 / 开头表示从 classpath 根目录查找）
     * 对应文件位置：src/main/resources/dtd/mybatis-3-mapper.dtd
     * 打包后位置：jar包根目录/dtd/mybatis-3-mapper.dtd
     */
    private static final String LOCAL_DTD_PATH = "/dtd/mybatis-3-mapper.dtd";

    @Override
    public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
        LOG.debug("Resolving entity - publicId: {}, systemId: {}", publicId, systemId);

        // 检查是否是需要替换为本地 DTD 的在线 MyBatis Mapper DTD
        if (isOnlineMyBatisMapperDtd(publicId, systemId)) {
            LOG.debug("Detected online MyBatis Mapper DTD, using local DTD file: {}", LOCAL_DTD_PATH);
            return loadLocalDtd();
        }

        // 如果是本地文件路径或其他情况，使用默认行为（不做处理）
        LOG.debug("Using default entity resolver behavior for systemId: {}", systemId);
        return null;
    }

    /**
     * 判断是否是需要替换的在线 MyBatis Mapper DTD
     *
     * @param publicId 公共标识符
     * @param systemId 系统标识符
     * @return 是否是在线 MyBatis Mapper DTD
     */
    private boolean isOnlineMyBatisMapperDtd(String publicId, String systemId) {
        // 首先检查 systemId 是否是在线 URL
        if (systemId != null) {
            boolean isOnlineUrl = MYBATIS_3_MAPPER_SYSTEM_ID_HTTP.equals(systemId)
                    || MYBATIS_3_MAPPER_SYSTEM_ID_HTTPS.equals(systemId)
                    || ((systemId.startsWith("http://") || systemId.startsWith("https://"))
                    && systemId.contains("mybatis") && systemId.contains("mapper.dtd"));

            // 只有 systemId 是在线 URL 时才拦截
            if (isOnlineUrl) {
                return true;
            }
        }

        // 如果 systemId 不是在线 URL，则不拦截（即使 publicId 匹配也不处理）
        // 这样可以支持用户自定义的本地 DTD 路径
        return false;
    }

    /**
     * 加载本地 DTD 文件
     *
     * @return InputSource 包含本地 DTD 内容
     * @throws IOException 如果加载失败
     */
    private InputSource loadLocalDtd() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream(LOCAL_DTD_PATH);
        if (inputStream == null) {
            LOG.warn("Local DTD file not found: {}, using empty DTD", LOCAL_DTD_PATH);
            // 如果本地 DTD 文件不存在，返回空的 InputSource
            return new InputSource(new java.io.ByteArrayInputStream("<?xml version='1.0' encoding='UTF-8'?>".getBytes()));
        }

        InputSource source = new InputSource(inputStream);
        source.setPublicId(MYBATIS_3_MAPPER_PUBLIC_ID);
        source.setSystemId(LOCAL_DTD_PATH);
        return source;
    }
}
