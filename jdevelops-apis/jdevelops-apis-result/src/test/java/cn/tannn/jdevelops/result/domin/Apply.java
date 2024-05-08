package cn.tannn.jdevelops.result.domin;

import cn.tannn.jdevelops.result.bean.ColumnUtil;

public class Apply {
    private String age, name;
    @ColumnUtil.TableField(value = "sex")
    private String mySex;

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMySex() {
        return mySex;
    }

    public void setMySex(String mySex) {
        this.mySex = mySex;
    }
}




