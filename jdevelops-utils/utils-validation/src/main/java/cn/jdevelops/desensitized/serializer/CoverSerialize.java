package cn.jdevelops.desensitized.serializer;

import cn.jdevelops.desensitized.annotation.Cover;
import cn.jdevelops.desensitized.enums.CoverRuleEnum;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;

import java.io.IOException;

/**
 * jackson 序列化时遮掩敏感数据
 * @author <a href="https://blog.csdn.net/u012424449/article/details/127239346">web</a>
 */
public class CoverSerialize extends JsonSerializer<String> implements ContextualSerializer {


    private CoverRuleEnum desensitizeRuleEnums;

    public CoverSerialize() {
    }

    public CoverSerialize(CoverRuleEnum desensitizeRuleEnums) {
        this.desensitizeRuleEnums = desensitizeRuleEnums;
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider serializerProvider, BeanProperty beanProperty) throws JsonMappingException {
        if (beanProperty != null){
            if (beanProperty.getType().getRawClass().equals(String.class)){
                Cover cover = beanProperty.getAnnotation(Cover.class);
                if (cover == null){
                    return serializerProvider.findKeySerializer(beanProperty.getType(), beanProperty);
                }else {
                    return new CoverSerialize(cover.rule());
                }
            }
            return serializerProvider.findKeySerializer(beanProperty.getType(), beanProperty);
        }
        return serializerProvider.findNullValueSerializer(null);
    }

    @Override
    public void serialize(String s, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(desensitizeRuleEnums.rules().apply(s));
    }
}
