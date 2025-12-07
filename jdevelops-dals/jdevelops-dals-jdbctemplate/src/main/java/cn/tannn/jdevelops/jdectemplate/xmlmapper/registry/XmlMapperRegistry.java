package cn.tannn.jdevelops.jdectemplate.xmlmapper.registry;

import cn.tannn.jdevelops.jdectemplate.xmlmapper.executor.XmlSqlExecutor;
import cn.tannn.jdevelops.jdectemplate.xmlmapper.model.SqlStatement;
import cn.tannn.jdevelops.jdectemplate.xmlmapper.model.XmlMapper;
import cn.tannn.jdevelops.jdectemplate.xmlmapper.page.PageRequest;
import cn.tannn.jdevelops.jdectemplate.xmlmapper.page.PageResult;
import cn.tannn.jdevelops.jdectemplate.xmlmapper.parser.XmlMapperParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
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
     * 执行分页查询
     * <p>自动执行数据查询和统计查询，返回分页结果</p>
     *
     * @param namespace 命名空间
     * @param dataStatementId 数据查询 SQL 语句 ID
     * @param countStatementId 统计查询 SQL 语句 ID（如果为空，则不执行统计查询）
     * @param queryParameter 查询参数对象
     * @param pageRequest 分页参数
     * @param resultType 结果类型
     * @param <T> 结果类型泛型
     * @return 分页结果
     */
    @SuppressWarnings("unchecked")
    public <T> PageResult<T> executePageQuery(
            String namespace,
            String dataStatementId,
            String countStatementId,
            Object queryParameter,
            PageRequest pageRequest,
            Class<T> resultType) {

        // 1. 执行数据查询
        SqlStatement dataStatement = getSqlStatement(namespace, dataStatementId);
        Object dataResult = sqlExecutor.execute(
                dataStatement,
                Arrays.asList(queryParameter, pageRequest),
                resultType
        );

        List<T> list;
        if (dataResult instanceof List) {
            list = (List<T>) dataResult;
        } else {
            list = Arrays.asList((T) dataResult);
        }

        // 2. 执行统计查询（如果指定了统计SQL）
        Long total = 0L;
        if (StringUtils.hasText(countStatementId)) {
            SqlStatement countStatement = getSqlStatement(namespace, countStatementId);
            // 注意：count查询也使用相同的参数格式（包装成List），确保与data查询的条件判断一致
            Object countResult = sqlExecutor.execute(countStatement,
                    Arrays.asList(queryParameter, pageRequest), Long.class);
            LOG.debug("Count query result: type={}, value={}, isNumber={}",
                    countResult == null ? "null" : countResult.getClass().getName(),
                    countResult,
                    countResult instanceof Number);
            if (countResult instanceof Number) {
                total = ((Number) countResult).longValue();
            }
        }

        // 3. 构建分页结果
        return new PageResult<>(
                pageRequest.getPageNum(),
                pageRequest.getPageSize(),
                total,
                list
        );
    }

    /**
     * 执行分页查询（带异常处理）
     * <p>如果查询失败且 tryc=true，返回空的分页结果</p>
     *
     * @param namespace 命名空间
     * @param dataStatementId 数据查询 SQL 语句 ID
     * @param countStatementId 统计查询 SQL 语句 ID
     * @param queryParameter 查询参数对象
     * @param pageRequest 分页参数
     * @param resultType 结果类型
     * @param tryc 是否吞掉异常
     * @param <T> 结果类型泛型
     * @return 分页结果
     */
    public <T> PageResult<T> executePageQuery(
            String namespace,
            String dataStatementId,
            String countStatementId,
            Object queryParameter,
            PageRequest pageRequest,
            Class<T> resultType,
            boolean tryc) {

        try {
            return executePageQuery(namespace, dataStatementId, countStatementId,
                    queryParameter, pageRequest, resultType);
        } catch (Exception e) {
            if (tryc) {
                LOG.warn("Page query execution failed (tryc=true): {}", e.getMessage());
                return new PageResult<>(
                        pageRequest.getPageNum(),
                        pageRequest.getPageSize(),
                        0L,
                        Arrays.asList()
                );
            }
            throw e;
        }
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
