package cn.tannn.jdevelops.result.bean;

import cn.tannn.jdevelops.result.domin.Beans;
import cn.tannn.jdevelops.result.domin.BeansVO;
import cn.tannn.jdevelops.result.domin.UserSerializableBean;
import cn.tannn.jdevelops.result.domin.UserVO;
import cn.tannn.jdevelops.result.utils.ListTo;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class SerializableBeanTest {

    @Test
    void copy() {
        UserSerializableBean target = new UserSerializableBean();
        UserSerializableBean source = new UserSerializableBean();
        source.setAge(1);
        source.setName("tan");
        assertTrue(target.isEmpty());
        target.copy(source);
        assertEquals("UserSerializableBean{name='tan', sex=null, age=1}",target.toString());
        assertFalse(target.isEmpty());
    }


    @Test
    void to() {
        UserSerializableBean source = new UserSerializableBean();
        source.setAge(1);
        source.setName("tan");
        UserVO userVO = source.to(UserVO.class);
        assertEquals("cn.tannn.jdevelops.result.domin.UserVO",userVO.getClass().getName());
        assertEquals("UserVO{name='tan', sex=null, age=1}",userVO.toString());
    }

    @Test
    void toList() {

        BeansVO tan = new BeansVO("tan", 1, 2);
        ArrayList<BeansVO> vos = new ArrayList<>();
        vos.add(tan);
        List<Beans> beans = SerializableBean.to(vos, Beans.class);
        Optional<?> firstElement = beans.stream().findFirst();
        firstElement.ifPresent(obj -> {
            Class<?> objectType = obj.getClass();
            assertEquals("cn.tannn.jdevelops.result.domin.Beans",objectType.getName());
        });
        Set<BeansVO> vos2 = new HashSet<>();
        vos2.add(tan);
        List<Beans> beans2 = SerializableBean.to(vos2, Beans.class);
        Optional<?> firstElement2 = beans2.stream().findFirst();
        firstElement2.ifPresent(obj -> {
            Class<?> objectType = obj.getClass();
            assertEquals("cn.tannn.jdevelops.result.domin.Beans",objectType.getName());
        });
    }

    @Test
    void to2() {
        BeansVO tan = new BeansVO("tan", 1, 2);
        assertEquals("Beans[name='tan', sex=1, age=2]",SerializableBean.to2(tan, Beans.class).toString());
        assertNull(SerializableBean.to2(null, Beans.class));

        UserSerializableBean source = new UserSerializableBean();
        source.setAge(1);
        source.setName("tan");
        assertEquals("UserVO{name='tan', sex=null, age=1}",SerializableBean.to2(source, UserVO.class).toString());
    }


    @Test
    void testToEmpty() {
        assertEquals("cn.tannn.jdevelops.result.domin.UserVO",
                SerializableBean.to(UserSerializableBean.class,UserVO.class).getClass().getName());
    }
}
