package cn.jdevelops.util.desensitized;


import cn.jdevelops.util.model.ValidationResultBean;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;


import static org.junit.Assert.assertEquals;

/**
 * 遮掩测试
 */
public class DesensitizedTest {


    @Test
    public void desensitizedTest() throws JsonProcessingException {
        // 序列化是才有效
        ValidationResultBean validationResultBean = new ValidationResultBean();


        // 创建自定义的Jackson ObjectMapper
        ObjectMapper objectMapper = new ObjectMapper();


        // 序列化对象为JSON字符串
        String json = objectMapper.writeValueAsString(validationResultBean);

        // 验证序列化结果
        String expectedJson = "{\"iphone\":\"133****5210\",\"iphone2\":null,\"fphone\":\"0316*****37\",\"idCard\":\"200***********6022\",\"cname\":\"谭*\",\"address\":\"成都市金牛区二环****\",\"password\":\"******\",\"email\":\"c**@163.com\"}";
        assertEquals(expectedJson,json);

        // 反序列化JSON字符串为对象
        ValidationResultBean deserializedObject = objectMapper.readValue(json, ValidationResultBean.class);


        assertEquals("成都市金牛区二环****",deserializedObject.getAddress());
        assertEquals("133****5210",deserializedObject.getIphone());
        assertEquals("0316*****37",deserializedObject.getFphone());
        assertEquals("200***********6022",deserializedObject.getIdCard());
        assertEquals("谭*",deserializedObject.getCname());
        assertEquals("******",deserializedObject.getPassword());
        assertEquals("c**@163.com",deserializedObject.getEmail());
    }
}
