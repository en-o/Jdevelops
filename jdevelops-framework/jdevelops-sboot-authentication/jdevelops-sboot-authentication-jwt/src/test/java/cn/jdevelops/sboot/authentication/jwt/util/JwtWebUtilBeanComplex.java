package cn.jdevelops.sboot.authentication.jwt.util;

import lombok.Data;

import java.util.List;

/**
 * 测试JwtWebUtilTest时用到的bean
 *
 * @author <a href="https://tannn.cn/">tan</a>
 * @date 2023/7/12 10:36
 */
@Data
public class JwtWebUtilBeanComplex {
    String name;
    Integer sex;

    List<String> strs;
    List<Integer> ints;
    List<JwtWebUtilBean> beans;


    public JwtWebUtilBeanComplex(String name, Integer sex, List<String> strs, List<Integer> ints, List<JwtWebUtilBean> beans) {
        this.name = name;
        this.sex = sex;
        this.strs = strs;
        this.ints = ints;
        this.beans = beans;
    }
}
