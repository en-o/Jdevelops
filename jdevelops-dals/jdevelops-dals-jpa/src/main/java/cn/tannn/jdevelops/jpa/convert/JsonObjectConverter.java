package cn.tannn.jdevelops.jpa.convert;

import com.alibaba.fastjson2.JSONObject;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * JOSN
 * <pre>
 * <code>@Comment("存储配置Json")</code>
 * <code>@Column(columnDefinition = "json not null")</code>
 * <code>@Convert(converter = JSONObjectConverter.class)</code>
 * <code>private JSONObject config;</code>
 * </pre>
 *
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @version V1.0
 * @date 2024/7/16 下午2:45
 */
@Converter(autoApply = true)
public class JsonObjectConverter implements AttributeConverter<JSONObject, String> {
    @Override
    public String convertToDatabaseColumn(JSONObject attribute) {
        return attribute == null ? null : attribute.toJSONString();
    }

    @Override
    public JSONObject convertToEntityAttribute(String dbData) {
        return dbData == null ? null : JSONObject.parseObject(dbData);
    }
}
