package cn.tannn.jdevelops.es.antlr.tools;

/**
 * 字段转换接口
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @date 2025-02-25 15:22:08
 */
public interface FieldTransformer {
    /**
     * 转换字段名
     * @param originalField 原始字段名
     * @return 转换后的字段名
     */
    String transformField(String originalField);
}
