package cn.jdevelops.util.core.list;

import java.util.Objects;

/**
 * 测试bean
 *
 * @author <a href="https://tannn.cn/">tan</a>
 * @date 2023/11/7 13:19
 */
public class TestBean {
    String name;
    Integer sex;


    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        TestBean myBean = (TestBean) obj;
        if (!Objects.equals(name, myBean.name)) return false;
        return sex != null ? sex.equals(myBean.sex) : myBean.sex == null;
    }

    public TestBean() {
    }

    public TestBean(String name, Integer sex) {
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
        return "TestBean{" +
                "name='" + name + '\'' +
                ", sex=" + sex +
                '}';
    }
}
