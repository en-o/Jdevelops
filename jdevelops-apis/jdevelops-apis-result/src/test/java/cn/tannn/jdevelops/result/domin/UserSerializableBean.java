package cn.tannn.jdevelops.result.domin;

import cn.tannn.jdevelops.result.bean.SerializableBean;

/**
 * @author <a href="https://tannn.cn/">tan</a>
 * @date 2024/5/8 下午2:35
 */
public class UserSerializableBean extends SerializableBean<UserSerializableBean> {
    String name;
    Integer sex;
    Integer age;

    public UserSerializableBean() {
    }

    public UserSerializableBean(String name, Integer sex, Integer age) {
        this.name = name;
        this.sex = sex;
        this.age = age;
    }

    @Override
    public String toString() {
        return "UserSerializableBean{" +
                "name='" + name + '\'' +
                ", sex=" + sex +
                ", age=" + age +
                '}';
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

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public boolean isEmpty(){
        return age==null&&sex==null&&name==null;
    }
}
