package cn.tannn.jdevelops.result.domin;

import java.util.StringJoiner;

public class Beans {
    String name;
    int sex;
    Integer age;

    public Beans() {
    }

    public Beans(String name, int sex, Integer age) {
        this.name = name;
        this.sex = sex;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Beans.class.getSimpleName() + "[", "]")
                .add("name='" + name + "'")
                .add("sex=" + sex)
                .add("age=" + age)
                .toString();
    }
}
