package cn.tannn.jdevelops.jwt.standalone.util;


import java.util.List;
import java.util.StringJoiner;

/**
 * 测试JwtWebUtilTest时用到的bean
 *
 * @author <a href="https://tannn.cn/">tan</a>
 * @date 2023/7/12 10:36
 */
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

    public JwtWebUtilBeanComplex() {
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

    public List<String> getStrs() {
        return strs;
    }

    public void setStrs(List<String> strs) {
        this.strs = strs;
    }

    public List<Integer> getInts() {
        return ints;
    }

    public void setInts(List<Integer> ints) {
        this.ints = ints;
    }

    public List<JwtWebUtilBean> getBeans() {
        return beans;
    }

    public void setBeans(List<JwtWebUtilBean> beans) {
        this.beans = beans;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", JwtWebUtilBeanComplex.class.getSimpleName() + "[", "]")
                .add("name='" + name + "'")
                .add("sex=" + sex)
                .add("strs=" + strs)
                .add("ints=" + ints)
                .add("beans=" + beans)
                .toString();
    }
}
