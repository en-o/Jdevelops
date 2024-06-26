package cn.tannn.jdevelops.es.schema;

import cn.tannn.jdevelops.annotations.es.EsField;
import cn.tannn.jdevelops.annotations.es.EsFieldIgnore;
import cn.tannn.jdevelops.annotations.es.EsIndex;
import cn.tannn.jdevelops.annotations.es.basic.EsFieldBasic;
import cn.tannn.jdevelops.annotations.es.basic.EsFieldMultiType;
import cn.tannn.jdevelops.annotations.es.constant.EsDdlAuto;
import cn.tannn.jdevelops.annotations.es.constant.EsType;
import cn.tannn.jdevelops.es.config.ElasticProperties;
import cn.tannn.jdevelops.es.core.ElasticService;
import cn.tannn.jdevelops.es.util.BeanUtil;
import co.elastic.clients.elasticsearch._types.mapping.DynamicMapping;
import co.elastic.clients.elasticsearch._types.mapping.Property;
import co.elastic.clients.elasticsearch.indices.GetMappingResponse;
import co.elastic.clients.elasticsearch.indices.get_mapping.IndexMappingRecord;
import com.alibaba.fastjson2.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.io.ByteArrayInputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Set;

import static cn.tannn.jdevelops.annotations.es.constant.EsTypeDataFormat.EPOCH_MILLIS;
import static cn.tannn.jdevelops.annotations.es.constant.EsTypeDataFormat.STRICT_DATE_OPTIONAL_TIME;


/**
 * 创建 mapping
 * 获取自定义注解的类 参考：{@link ClasspathScanningPersistenceUnitPostProcessor#postProcessPersistenceUnitInfo}
 *
 * @author tan
 */
public class CreateElasticsearchMapping implements ApplicationListener<ContextRefreshedEvent>, ApplicationContextAware {

    private static final Logger logger = LoggerFactory.getLogger(CreateElasticsearchMapping.class);

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

        try {
            // 扫描指定路径
            ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
            provider.addIncludeFilter(new AnnotationTypeFilter(EsIndex.class));
            ElasticProperties elasticProperties = applicationContext.getBean(ElasticProperties.class);
            if (null == elasticProperties.getBasePackage() || elasticProperties.getBasePackage().isEmpty()) {
                logger.warn("创建 mappings 没有设置package，所有不进行创建");
                return;
            }
            Set<BeanDefinition> beanDefinitionSet = provider.findCandidateComponents(
                    elasticProperties.getBasePackage());
            for (BeanDefinition definition : beanDefinitionSet) {
                String indexName;
                JSONObject creatEsIndexDsl = new JSONObject();
                try {
                    Object creatEsIndexBean = getObject(definition);
                    // 创建类的实例
                    EsIndex esIndex = creatEsIndexBean.getClass().getAnnotation(EsIndex.class);
                    indexName = esIndex.name();
                    ElasticService elasticService = applicationContext.getBean(ElasticService.class);
                    if (esIndex.ddlAuto().equals(EsDdlAuto.NONE)) {
                        logger.warn(definition.getBeanClassName() + "不需要创建索引");
                        continue;
                    }
                    creatEsIndexDsl.put("dynamic", esIndex.dynamic().name().toLowerCase());
                    assemblyProperties(creatEsIndexDsl, creatEsIndexBean);
                    if (!creatEsIndexDsl.isEmpty() && indexName != null) {
                        JSONObject mappingsJson = new JSONObject();
                        mappingsJson.put("mappings", creatEsIndexDsl);

                        if (esIndex.ddlAuto().equals(EsDdlAuto.CREATE)) {
                            logger.debug("开始创建索引[" + indexName + "]先删后建");
                            elasticService.deleteIndex(indexName);
                        } else if (esIndex.ddlAuto().equals(EsDdlAuto.VALIDATE)) {
                            if (elasticService.existIndex(indexName)) {
                                logger.debug("开始创建索引[" + indexName + "]存在无需创建");
                                continue;
                            }
                        } else if (esIndex.ddlAuto().equals(EsDdlAuto.UPDATE)) {
                            logger.debug("开始更新索引[" + indexName + "]检查索引结构一致性");
                            GetMappingResponse getMappingResponse = elasticService.showIndexMapping(indexName);
                            if (getMappingResponse == null) {
                                logger.debug("开始更新索引[" + indexName + "]索引未找到，将直接创建索引");
                            } else {
                                IndexMappingRecord mappingRecord = getMappingResponse.get(indexName);
                                if (mappingRecord == null) {
                                    logger.debug("开始更新索引[" + indexName + "]索引未找到，将直接创建索引");
                                } else {
                                    Map<String, Property> properties = mappingRecord.mappings().properties();
                                    DynamicMapping dynamic = mappingRecord.mappings().dynamic();
                                    if (dynamic != null && properties != null && !properties.isEmpty()) {
                                        // 一模一样
                                        if (dynamic.jsonValue().equals(creatEsIndexDsl.getString("dynamic"))
                                            && BeanUtil.compareDSL(creatEsIndexDsl.getJSONObject("properties"), properties)) {
                                            logger.debug("开始更新索引[" + indexName + "]检查索引结构无变化");
                                            continue;
                                        } else {
                                            logger.debug("开始更新索引[" + indexName + "]原有的mappings发生变化，正在进行重写构建");
                                            elasticService.deleteIndex(indexName);
                                        }
                                    } else {
                                        logger.debug("开始更新索引[" + indexName + "]检测结构失败，将不做任何操作请主动检查索引结构");
                                        continue;
                                    }
                                }
                            }
                        }
                        logger.debug("正在创建索引[" + indexName + "]mappings ======================>" + mappingsJson);
                        elasticService.createIndexNoVerify(indexName,
                                new ByteArrayInputStream(mappingsJson.toJSONString().getBytes()));
                    }
                } catch (Exception e) {
                    logger.error("@EsIndex字段值获取失败,请检查", e);
                    SpringApplication.exit(applicationContext);
                    throw new RuntimeException(e);
                }
            }

        } catch (Exception e) {
            logger.error("创建索引失败,请手动完成创建 ======================>", e);
        }
    }


    /**
     * 创建 dsl中的 properties 内容
     *
     * @param creatEsIndexDsl  预先初始化好的dsl json
     * @param creatEsIndexBean 需要处理成properties的对象
     */
    private static void assemblyProperties(JSONObject creatEsIndexDsl,
                                           Object creatEsIndexBean) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException, InstantiationException {
        // properties 节点
        JSONObject creatEsIndexDslProperties = new JSONObject();

        Field[] allField = BeanUtil.getAllField(creatEsIndexBean.getClass());
        for (Field field : allField) {
            // 设置字段可访问，即使是私有字段
            field.setAccessible(true);
            // 获取注解，里面的元数据我要用
            EsField esField = field.getAnnotation(EsField.class);
            // 字段名做 dsl的key使用
            String name = field.getName();
            EsFieldIgnore esFieldIgnore = field.getAnnotation(EsFieldIgnore.class);
            if (esFieldIgnore != null) {
                logger.warn("构建mappings时忽略字段：" + name);
                continue;
            }

            assemblyPropertiesInfo(creatEsIndexDslProperties, esField, name);
            // 嵌套对象
            if (esField != null && esField.basic().type().equals(EsType.nested)) {
                Object nestedValue = field.get(creatEsIndexBean);
                Object fieldNestedObject = getFieldNestedObject(field, nestedValue);
                JSONObject nestedJson = creatEsIndexDslProperties.getJSONObject(name);
                assemblyProperties(nestedJson, fieldNestedObject);
            }
        }
        if (!creatEsIndexDslProperties.isEmpty()) {
            creatEsIndexDsl.put("properties", creatEsIndexDslProperties);
        }
    }

    /**
     * 组装详细数据
     *
     * @param creatEsIndexDslProperties creatEsIndexDslProperties
     * @param esField                   注解元数据
     * @param name                      es mapping 中 properties中 的 key[字段名]
     */
    private static void assemblyPropertiesInfo(JSONObject creatEsIndexDslProperties, EsField esField, String name) {
        if (esField == null) {
            // 没有注解的字段给予默认值
            JSONObject fieldJson = JSONObject.of("type", EsType.keyword.name().toLowerCase());
            creatEsIndexDslProperties.put(name, fieldJson);
        } else {
            JSONObject fieldJson = JSONObject.of("type", esField.basic().type().name().toLowerCase());
            analyzerEsField(esField.basic(), fieldJson);
            if (esField.fields().length > 0) {
                EsFieldMultiType[] fields = esField.fields();
                JSONObject multiType = new JSONObject();
                for (EsFieldMultiType multoTypefield : fields) {
                    JSONObject multiTypeDataInfo = JSONObject.of("type", multoTypefield.basic().type().name().toLowerCase());
                    analyzerEsField(multoTypefield.basic(), multiTypeDataInfo);
                    multiType.put(multoTypefield.alias().toLowerCase(), multiTypeDataInfo);
                }
                fieldJson.put("fields", multiType);
            }
            creatEsIndexDslProperties.put(name, fieldJson);
        }
    }


    /**
     * 解析注解
     *
     * @param esField   EsField
     * @param fieldJson JSONObject
     */
    private static void analyzerEsField(EsFieldBasic esField, JSONObject fieldJson) {
        if (!esField.index()) {
            fieldJson.put("index", false);
        }
        if (!"".equals(esField.analyzer())) {
            fieldJson.put("analyzer", esField.analyzer());
        }
        if (esField.ignoreMalformed()) {
            fieldJson.put("ignore_malformed", true);
        }
        if (esField.format().length > 0) {
            fieldJson.put("format", StringUtils.join(esField.format(), "||"));
        } else {
            if (esField.type().equals(EsType.date)) {
                fieldJson.put("format", STRICT_DATE_OPTIONAL_TIME + "||" + EPOCH_MILLIS);
            }
        }
    }


    /**
     * 获取扫描出来 对象
     *
     * @param definition BeanDefinition
     * @return Object
     */
    private static Object getObject(BeanDefinition definition) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        String creatEsIndexBeanClassName = definition.getBeanClassName();
        Class<?> creatEsIndexBeanClazz = Class.forName(creatEsIndexBeanClassName);
        return creatEsIndexBeanClazz.newInstance();
    }

    /**
     * 获取对象中的 对象属性的对象
     *
     * @param field       Field
     * @param nestedValue field 的值
     * @return Object
     */
    private static Object getFieldNestedObject(Field field, Object nestedValue) throws InstantiationException,
            IllegalAccessException,
            NoSuchMethodException,
            InvocationTargetException {
        if (nestedValue == null) { // 必然等于空这步好像有点多余
            // 获取字段的类型
            Class<?> fieldType = field.getType();
            // 创建一个新的对象
            return fieldType.getDeclaredConstructor().newInstance();
        } else {
            return nestedValue;
        }

    }


}
