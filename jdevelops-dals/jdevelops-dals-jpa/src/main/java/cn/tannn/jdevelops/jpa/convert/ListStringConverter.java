package cn.tannn.jdevelops.jpa.convert;

import com.alibaba.fastjson2.JSON;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.List;

/**
 * List<String>
 * <pre>
 * <code>@Comment("liststr")</code>
 * <code>@Column(columnDefinition = "varchar(100)")</code>
 * <code>@Convert(converter = ListStringConverter.class)</code>
 * <code>private List<String> images;</code>
 * </pre>
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @version V1.0
 * @date 2024/7/16 下午2:45
 */
@Converter(autoApply = true)
public class ListStringConverter implements AttributeConverter<List<String>, String> {

    @Override
    public String convertToDatabaseColumn(List<String> attribute) {
        if(attribute == null || attribute.isEmpty()){
            return null;
        }
        return JSON.toJSONString(attribute);
    }

    @Override
    public List<String> convertToEntityAttribute(String dbData) {
        if(dbData == null || dbData.isEmpty()){
            return null;
        }
        return JSON.parseArray(dbData).toJavaList(String.class);
    }
}
