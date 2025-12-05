package cn.tannn.jdevelops.jdectemplate.xmlmapper.parser;

import cn.tannn.jdevelops.jdectemplate.xmlmapper.model.*;
import cn.tannn.jdevelops.jdectemplate.xmlmapper.node.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * XML Mapper 解析器
 * 解析 XML 文件并构建 XmlMapper 对象
 *
 * @author tnnn
 */
public class XmlMapperParser {

    private static final Logger LOG = LoggerFactory.getLogger(XmlMapperParser.class);

    private final ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();

    /**
     * 解析指定路径的 XML 文件
     *
     * @param xmlPath XML 文件路径（支持 classpath: 前缀和通配符）
     * @return XmlMapper 对象
     */
    public XmlMapper parse(String xmlPath) throws Exception {
        Resource resource = resourceResolver.getResource(xmlPath);
        if (!resource.exists()) {
            throw new IllegalArgumentException("XML mapper file not found: " + xmlPath);
        }

        try (InputStream inputStream = resource.getInputStream()) {
            return parseInputStream(inputStream, xmlPath);
        }
    }

    /**
     * 解析输入流
     */
    private XmlMapper parseInputStream(InputStream inputStream, String xmlPath) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        // 禁用外部实体解析以防止 XXE 攻击，但允许 DOCTYPE 声明
        factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", false);
        factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
        factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);

        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(inputStream);

        // 获取根元素
        Element root = document.getDocumentElement();
        if (!"mapper".equals(root.getNodeName())) {
            throw new IllegalArgumentException("Root element must be <mapper>");
        }

        // 解析 namespace
        String namespace = root.getAttribute("namespace");
        if (!StringUtils.hasText(namespace)) {
            throw new IllegalArgumentException("Mapper namespace is required");
        }

        XmlMapper xmlMapper = new XmlMapper(namespace);
        xmlMapper.setXmlPath(xmlPath);

        // 解析子元素
        NodeList children = root.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node node = children.item(i);
            if (node.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }

            Element element = (Element) node;
            String tagName = element.getTagName();

            switch (tagName) {
                case "sql":
                    parseSqlFragment(element, xmlMapper);
                    break;
                case "select":
                    parseSqlStatement(element, SqlCommandType.SELECT, xmlMapper);
                    break;
                case "insert":
                    parseSqlStatement(element, SqlCommandType.INSERT, xmlMapper);
                    break;
                case "update":
                    parseSqlStatement(element, SqlCommandType.UPDATE, xmlMapper);
                    break;
                case "delete":
                    parseSqlStatement(element, SqlCommandType.DELETE, xmlMapper);
                    break;
                default:
                    LOG.warn("Unknown element: {}", tagName);
            }
        }

        return xmlMapper;
    }

    /**
     * 解析 SQL 片段 (&lt;sql&gt; 标签)
     */
    private void parseSqlFragment(Element element, XmlMapper xmlMapper) {
        String id = element.getAttribute("id");
        if (!StringUtils.hasText(id)) {
            LOG.warn("SQL fragment id is required");
            return;
        }

        List<SqlNode> sqlNodes = parseChildren(element, xmlMapper);
        SqlFragment fragment = new SqlFragment(id, sqlNodes);
        xmlMapper.addSqlFragment(id, fragment);
    }

    /**
     * 解析 SQL 语句
     */
    private void parseSqlStatement(Element element, SqlCommandType commandType, XmlMapper xmlMapper) {
        String id = element.getAttribute("id");
        if (!StringUtils.hasText(id)) {
            LOG.warn("SQL statement id is required");
            return;
        }

        SqlStatement statement = new SqlStatement();
        statement.setId(id);
        statement.setCommandType(commandType);
        statement.setResultType(element.getAttribute("resultType"));
        statement.setParameterType(element.getAttribute("parameterType"));

        // 解析 tryc 属性
        String trycAttr = element.getAttribute("tryc");
        if (StringUtils.hasText(trycAttr)) {
            statement.setTryc(Boolean.parseBoolean(trycAttr));
        }

        // 解析 timeout 属性
        String timeoutAttr = element.getAttribute("timeout");
        if (StringUtils.hasText(timeoutAttr)) {
            statement.setTimeout(Integer.parseInt(timeoutAttr));
        }

        // 解析子节点
        List<SqlNode> sqlNodes = parseChildren(element, xmlMapper);
        statement.setSqlNodes(sqlNodes);

        xmlMapper.addSqlStatement(statement);
    }

    /**
     * 解析子节点
     */
    private List<SqlNode> parseChildren(Element parent, XmlMapper xmlMapper) {
        List<SqlNode> sqlNodes = new ArrayList<>();
        NodeList children = parent.getChildNodes();

        for (int i = 0; i < children.getLength(); i++) {
            Node node = children.item(i);

            if (node.getNodeType() == Node.TEXT_NODE || node.getNodeType() == Node.CDATA_SECTION_NODE) {
                // 文本节点
                String text = node.getTextContent();
                if (StringUtils.hasText(text)) {
                    // 使用混合节点处理可能包含参数的文本
                    sqlNodes.add(new MixedSqlNode(text));
                }
            } else if (node.getNodeType() == Node.ELEMENT_NODE) {
                // 元素节点
                Element element = (Element) node;
                SqlNode sqlNode = parseElement(element, xmlMapper);
                if (sqlNode != null) {
                    sqlNodes.add(sqlNode);
                }
            }
        }

        return sqlNodes;
    }

    /**
     * 解析元素节点
     */
    private SqlNode parseElement(Element element, XmlMapper xmlMapper) {
        String tagName = element.getTagName();

        return switch (tagName) {
            case "if" -> parseIfNode(element, xmlMapper);
            case "foreach" -> parseForeachNode(element, xmlMapper);
            case "where" -> parseWhereNode(element, xmlMapper);
            case "set" -> parseSetNode(element, xmlMapper);
            case "trim" -> parseTrimNode(element, xmlMapper);
            case "choose" -> parseChooseNode(element, xmlMapper);
            case "include" -> parseIncludeNode(element, xmlMapper);
            default -> {
                LOG.warn("Unknown element: {}", tagName);
                yield null;
            }
        };
    }

    /**
     * 解析 &lt;if&gt; 标签
     */
    private SqlNode parseIfNode(Element element, XmlMapper xmlMapper) {
        String test = element.getAttribute("test");
        if (!StringUtils.hasText(test)) {
            LOG.warn("if element test attribute is required");
            return null;
        }

        List<SqlNode> contents = parseChildren(element, xmlMapper);
        return new IfSqlNode(test, contents);
    }

    /**
     * 解析 &lt;foreach&gt; 标签
     */
    private SqlNode parseForeachNode(Element element, XmlMapper xmlMapper) {
        String collection = element.getAttribute("collection");
        String item = element.getAttribute("item");
        String index = element.getAttribute("index");
        String open = element.getAttribute("open");
        String separator = element.getAttribute("separator");
        String close = element.getAttribute("close");

        if (!StringUtils.hasText(collection)) {
            LOG.warn("foreach element collection attribute is required");
            return null;
        }

        List<SqlNode> contents = parseChildren(element, xmlMapper);
        return new ForeachSqlNode(collection, item, index, open, separator, close, contents);
    }

    /**
     * 解析 &lt;where&gt; 标签
     */
    private SqlNode parseWhereNode(Element element, XmlMapper xmlMapper) {
        List<SqlNode> contents = parseChildren(element, xmlMapper);
        return new WhereSqlNode(contents);
    }

    /**
     * 解析 &lt;set&gt; 标签
     */
    private SqlNode parseSetNode(Element element, XmlMapper xmlMapper) {
        List<SqlNode> contents = parseChildren(element, xmlMapper);
        return new SetSqlNode(contents);
    }

    /**
     * 解析 &lt;trim&gt; 标签
     */
    private SqlNode parseTrimNode(Element element, XmlMapper xmlMapper) {
        // TODO: 实现 trim 节点解析
        LOG.warn("trim element is not yet implemented");
        return null;
    }

    /**
     * 解析 &lt;choose&gt; 标签
     */
    private SqlNode parseChooseNode(Element element, XmlMapper xmlMapper) {
        // TODO: 实现 choose 节点解析
        LOG.warn("choose element is not yet implemented");
        return null;
    }

    /**
     * 解析 &lt;include&gt; 标签
     */
    private SqlNode parseIncludeNode(Element element, XmlMapper xmlMapper) {
        String refid = element.getAttribute("refid");
        if (!StringUtils.hasText(refid)) {
            LOG.warn("include element refid attribute is required");
            return null;
        }

        // 返回一个包含引用片段的节点
        return new IncludeSqlNode(refid, xmlMapper);
    }

    /**
     * 扫描并解析多个 XML 文件
     *
     * @param locationPattern 位置模式（支持通配符）
     * @return XmlMapper 映射（namespace -> XmlMapper）
     */
    public Map<String, XmlMapper> scanAndParse(String locationPattern) throws Exception {
        Map<String, XmlMapper> mappers = new HashMap<>();

        Resource[] resources = resourceResolver.getResources(locationPattern);
        LOG.info("Found {} XML mapper files", resources.length);

        for (Resource resource : resources) {
            try {
                String xmlPath = resource.getURI().toString();
                XmlMapper xmlMapper = parse(xmlPath);
                mappers.put(xmlMapper.getNamespace(), xmlMapper);
                LOG.info("Loaded XML mapper: {}", xmlMapper.getNamespace());
            } catch (Exception e) {
                LOG.error("Failed to parse XML mapper: {}", resource.getFilename(), e);
            }
        }

        return mappers;
    }

    /**
     * Include 节点实现
     */
    private static class IncludeSqlNode implements SqlNode {
        private final String refid;
        private final XmlMapper xmlMapper;

        public IncludeSqlNode(String refid, XmlMapper xmlMapper) {
            this.refid = refid;
            this.xmlMapper = xmlMapper;
        }

        @Override
        public boolean apply(SqlContext context, Object parameter) {
            SqlFragment fragment = xmlMapper.getSqlFragment(refid);
            if (fragment == null) {
                LOG.warn("SQL fragment not found: {}", refid);
                return false;
            }

            // 应用片段中的所有节点
            boolean applied = false;
            for (SqlNode sqlNode : fragment.getSqlNodes()) {
                if (sqlNode.apply(context, parameter)) {
                    applied = true;
                }
            }
            return applied;
        }

        @Override
        public String toString() {
            return "IncludeSqlNode{refid='" + refid + "'}";
        }
    }
}
