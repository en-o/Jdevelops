package cn.tannn.jdevelops.result.bean;

import cn.tannn.jdevelops.result.domin.Beans;
import cn.tannn.jdevelops.result.domin.BeansVO;
import cn.tannn.jdevelops.result.domin.UserSerializableBean;
import cn.tannn.jdevelops.result.domin.UserVO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BeanCopyUtilTest {

    @Test
    void beanCopy() {
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
    void beanCopyEmpty() {
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
    void beanCopyWithIngore() {
        UserBeean source = new UserBeean("tan",1,18);
        UserBeean target = new UserBeean("tanX");

        BeanCopyUtil.beanCopyWithIngore(source,target);
        assertEquals(target.toString(),"UserBeean{name='tan', sex=1, age=18}");

        UserBeean target2 = new UserBeean("tanX");
        BeanCopyUtil.beanCopyWithIngore(source,target2,"name");
        assertEquals(target2.toString(),"UserBeean{name='tanX', sex=1, age=18}");
    }

    @Test
    void getNullAndIgnorePropertyNames() {
        UserBeean userBeean = new UserBeean("tan",1,18);
        assertEquals(BeanCopyUtil.getNullAndIgnorePropertyNames(userBeean,"name").length,1);
    }

    @Test
    void getNullAndEmptyPropertyNames() {
        UserBeean userBeean = new UserBeean("tan",1,18);
        assertEquals(BeanCopyUtil.getNullAndEmptyPropertyNames(userBeean).length,0);
        userBeean = new UserBeean("tan",null,null);
        assertEquals(BeanCopyUtil.getNullAndEmptyPropertyNames(userBeean).length,2);
        userBeean = new UserBeean("",1,2);
        assertEquals(BeanCopyUtil.getNullAndEmptyPropertyNames(userBeean).length,1);
    }

    @Test
    void getNullAndEmptyPropertyNameSet() {
        UserBeean tan = new UserBeean("", null, null);
        assertEquals("[sex, name, age]",BeanCopyUtil.getNullAndEmptyPropertyNameSet(tan).toString());
         tan = new UserBeean(" ", null, null);
        assertEquals("[sex, age]",BeanCopyUtil.getNullAndEmptyPropertyNameSet(tan).toString());
         tan = new UserBeean("tan", null, null);
        assertEquals("[sex, age]",BeanCopyUtil.getNullAndEmptyPropertyNameSet(tan).toString());
    }

    @Test
    void getNullPropertyNames() {
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
    void getNullPropertyNameSet() {
        UserBeean tan = new UserBeean("tan", null, null);
        assertEquals("[sex, age]",BeanCopyUtil.getNullPropertyNameSet(tan).toString());

        tan = new UserBeean("", null, null);
        assertEquals("[sex, age]",BeanCopyUtil.getNullPropertyNameSet(tan).toString());
    }

    @Test
    void to() {
        BeansVO tan = new BeansVO("tan", 1, 2);
        assertEquals("Beans[name='tan', sex=1, age=2]",BeanCopyUtil.to(Beans.class, tan).toString());
        UserSerializableBean source = new UserSerializableBean();
        source.setAge(1);
        source.setName("tan");
        assertEquals("UserVO{name='tan', sex=null, age=1}",BeanCopyUtil.to(UserVO.class ,source).toString());
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
