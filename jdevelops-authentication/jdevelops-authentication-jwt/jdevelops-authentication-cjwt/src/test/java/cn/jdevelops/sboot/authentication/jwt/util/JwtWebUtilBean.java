package cn.jdevelops.sboot.authentication.jwt.util;

import lombok.Data;

/**
 * 测试JwtWebUtilTest时用到的bean
 *
 * @author <a href="https://tannn.cn/">tan</a>
 * @date 2023/7/12 10:36
 */
@Data
public class JwtWebUtilBean {
    String name;
    Integer sex;


    public JwtWebUtilBean(String name, Integer sex) {
        this.name = name;
        this.sex = sex;
    }
}
