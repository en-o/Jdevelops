package cn.tannn.jdevelops.jdectemplate.xmlmapper.registry;

import cn.tannn.jdevelops.jdectemplate.xmlmapper.executor.XmlSqlExecutor;
import cn.tannn.jdevelops.jdectemplate.xmlmapper.model.SqlStatement;
import cn.tannn.jdevelops.jdectemplate.xmlmapper.model.XmlMapper;
import cn.tannn.jdevelops.jdectemplate.xmlmapper.parser.XmlMapperParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * XML Mapper 注册器
 * 管理所有的 XML Mapper 和 SQL 执行
 *
 * @author tnnn
 */
public class XmlMapperRegistry {

    private static final Logger LOG = LoggerFactory.getLogger(XmlMapperRegistry.class);

    /**
     * Mapper 缓存（namespace -> XmlMapper）
     */
    private final Map<String, XmlMapper> mapperCache = new ConcurrentHashMap<>();

    /**
     * SQL 执行器
     */
    private final XmlSqlExecutor sqlExecutor;

    /**
     * XML 解析器
     */
    private final XmlMapperParser parser = new XmlMapperParser();

    public XmlMapperRegistry(JdbcTemplate jdbcTemplate) {
        this.sqlExecutor = new XmlSqlExecutor(jdbcTemplate);
    }

    /**
     * 注册单个 XML Mapper
     *
     * @param xmlPath XML 文件路径
     */
    public void registerMapper(String xmlPath) throws Exception {
        XmlMapper xmlMapper = parser.parse(xmlPath);
        mapperCache.put(xmlMapper.getNamespace(), xmlMapper);
        LOG.info("Registered XML mapper: {}", xmlMapper.getNamespace());
    }

    /**
     * 扫描并注册多个 XML Mapper
     * @param locationPattern 位置模式（支持通配符，如 classpath*:jmapper///*//*///*.xml）
     */
    public void scanAndRegisterMappers(String locationPattern) throws Exception {
        Map<String, XmlMapper> mappers = parser.scanAndParse(locationPattern);
        mapperCache.putAll(mappers);
        LOG.info("Registered {} XML mappers", mappers.size());
    }

    /**
     * 获取 XML Mapper
     *
     * @param namespace 命名空间
     * @return XmlMapper 对象
     */
    public XmlMapper getMapper(String namespace) {
        XmlMapper mapper = mapperCache.get(namespace);
        if (mapper == null) {
            throw new IllegalArgumentException("Mapper not found: " + namespace);
        }
        return mapper;
    }

    /**
     * 获取 SQL 语句
     *
     * @param namespace 命名空间
     * @param statementId SQL 语句 ID
     * @return SqlStatement 对象
     */
    public SqlStatement getSqlStatement(String namespace, String statementId) {
        XmlMapper mapper = getMapper(namespace);
        SqlStatement statement = mapper.getSqlStatement(statementId);
        if (statement == null) {
            throw new IllegalArgumentException(
                    "SQL statement not found: " + namespace + "." + statementId);
        }
        return statement;
    }

    /**
     * 执行查询
     *
     * @param namespace 命名空间
     * @param statementId SQL 语句 ID
     * @param parameter 参数对象
     * @param resultType 结果类型
     * @param <T> 结果类型泛型
     * @return 查询结果
     */
    public <T> Object executeQuery(String namespace, String statementId, Object parameter, Class<T> resultType) {
        SqlStatement statement = getSqlStatement(namespace, statementId);
        return sqlExecutor.execute(statement, parameter, resultType);
    }

    /**
     * 执行更新操作
     *
     * @param namespace 命名空间
     * @param statementId SQL 语句 ID
     * @param parameter 参数对象
     * @return 影响行数或自增ID (如果配置了 useGeneratedKeys=true)
     */
    public Object executeUpdate(String namespace, String statementId, Object parameter) {
        SqlStatement statement = getSqlStatement(namespace, statementId);
        return sqlExecutor.execute(statement, parameter, null);
    }

    /**
     * 获取所有已注册的 Mapper 命名空间
     */
    public java.util.Set<String> getRegisteredMappers() {
        return mapperCache.keySet();
    }

    /**
     * 清除缓存
     */
    public void clearCache() {
        mapperCache.clear();
        LOG.info("Mapper cache cleared");
    }

    /**
     * 重新加载 Mapper
     *
     * @param namespace 命名空间
     */
    public void reloadMapper(String namespace) throws Exception {
        XmlMapper oldMapper = mapperCache.get(namespace);
        if (oldMapper == null) {
            throw new IllegalArgumentException("Mapper not found: " + namespace);
        }

        String xmlPath = oldMapper.getXmlPath();
        if (!StringUtils.hasText(xmlPath)) {
            throw new IllegalStateException("XML path not found for mapper: " + namespace);
        }

        XmlMapper newMapper = parser.parse(xmlPath);
        mapperCache.put(namespace, newMapper);
        LOG.info("Reloaded XML mapper: {}", namespace);
    }
}
