package cn.tannn.jdevelops.jwt.standalone.util;


import java.util.StringJoiner;

/**
 * 测试JwtWebUtilTest时用到的bean
 *
 * @author <a href="https://tannn.cn/">tan</a>
 * @date 2023/7/12 10:36
 */
public class JwtWebUtilBean {
    String name;
    Integer sex;

    public JwtWebUtilBean() {
    }

    public JwtWebUtilBean(String name, Integer sex) {
        this.name = name;
        this.sex = sex;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", JwtWebUtilBean.class.getSimpleName() + "[", "]")
                .add("name='" + name + "'")
                .add("sex=" + sex)
                .toString();
    }
}
