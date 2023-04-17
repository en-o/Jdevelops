package cn.jdevelops.api.result.util.bean;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BeanCopyUtilTest {


    @Test
    public void beanCopy() {
        UserBeean userBeean = new UserBeean("tan",1,18);
        UserBeean userDTO = new UserBeean("tanX");
        BeanCopyUtil.beanCopy(userDTO,userBeean);
        assertEquals(userBeean.toString(),"UserBeean{name='tanX', sex=1, age=18}");

        userBeean = new UserBeean("tan",1,18);
        UserBeean userDTO2 = new UserBeean("tanX",null,null);
        BeanCopyUtil.beanCopy(userDTO2,userBeean);
        assertEquals(userBeean.toString(),"UserBeean{name='tanX', sex=1, age=18}");

        userBeean = new UserBeean("tan",1,18);
        UserBeean userDTO3 = new UserBeean("tanX",2,null);
        BeanCopyUtil.beanCopy(userDTO3,userBeean);
        assertEquals(userBeean.toString(),"UserBeean{name='tanX', sex=2, age=18}");

        // 主要看这个
        userBeean = new UserBeean("tan",1,18);
        UserBeean userDTO4 = new UserBeean("",2,null);
        BeanCopyUtil.beanCopy(userDTO4,userBeean);
        assertEquals(userBeean.toString(),"UserBeean{name='', sex=2, age=18}");
    }


    @Test
    public void beanCopyEmpty() {
        UserBeean userBeean = new UserBeean("tan",1,18);
        UserBeean userDTO = new UserBeean("tanX");
        BeanCopyUtil.beanCopyEmpty(userDTO,userBeean);
        assertEquals(userBeean.toString(),"UserBeean{name='tanX', sex=1, age=18}");

        userBeean = new UserBeean("tan",1,18);
        UserBeean userDTO2 = new UserBeean("tanX",null,null);
        BeanCopyUtil.beanCopyEmpty(userDTO2,userBeean);
        assertEquals(userBeean.toString(),"UserBeean{name='tanX', sex=1, age=18}");

        userBeean = new UserBeean("tan",1,18);
        UserBeean userDTO3 = new UserBeean("tanX",2,null);
        BeanCopyUtil.beanCopyEmpty(userDTO3,userBeean);
        assertEquals(userBeean.toString(),"UserBeean{name='tanX', sex=2, age=18}");

        // 主要看这个
        userBeean = new UserBeean("tan",1,18);
        UserBeean userDTO4 = new UserBeean("",2,null);
        BeanCopyUtil.beanCopyEmpty(userDTO4,userBeean);
        assertEquals(userBeean.toString(),"UserBeean{name='tan', sex=2, age=18}");
    }



    @Test
    public void nullPropertyNamesTest(){
        UserBeean userBeean = new UserBeean("tan",1,18);
        assertEquals(BeanCopyUtil.getNullPropertyNames(userBeean).length,0);
        userBeean = new UserBeean("tan",null,18);
        assertEquals(BeanCopyUtil.getNullPropertyNames(userBeean).length,1);
        userBeean = new UserBeean("tan",null,null);
        assertEquals(BeanCopyUtil.getNullPropertyNames(userBeean).length,2);
        userBeean = new UserBeean("",1,2);
        assertEquals(BeanCopyUtil.getNullPropertyNames(userBeean).length,0);

    }

    @Test
    public void nullAndIgnorePropertyNamesTest() {
        UserBeean userBeean = new UserBeean("tan",1,18);
        assertEquals(BeanCopyUtil.getNullAndIgnorePropertyNames(userBeean,"name").length,1);
    }

    @Test
    public void nullAndEmptyPropertyNamesTest() {
        UserBeean userBeean = new UserBeean("tan",1,18);
        assertEquals(BeanCopyUtil.getNullAndEmptyPropertyNames(userBeean).length,0);
        userBeean = new UserBeean("tan",null,null);
        assertEquals(BeanCopyUtil.getNullAndEmptyPropertyNames(userBeean).length,2);
        userBeean = new UserBeean("",1,2);
        assertEquals(BeanCopyUtil.getNullAndEmptyPropertyNames(userBeean).length,1);
    }



    class UserBeean{
        String name;
        Integer sex;
        Integer age;

        public UserBeean(String name, Integer sex, Integer age) {
            this.name = name;
            this.sex = sex;
            this.age = age;
        }

        public UserBeean(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "UserBeean{" +
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
    }
}
