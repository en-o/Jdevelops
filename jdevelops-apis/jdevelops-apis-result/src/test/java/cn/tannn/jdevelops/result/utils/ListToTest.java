package cn.tannn.jdevelops.result.utils;

import cn.tannn.jdevelops.result.domin.Beans;
import cn.tannn.jdevelops.result.domin.BeansVO;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ListToTest {

    @Test
    void to() {
        BeansVO tan = new BeansVO("tan", 1, 2);
        ArrayList<BeansVO> vos = new ArrayList<>();
        vos.add(tan);
        List<Beans> beans = ListTo.to(Beans.class, vos);
        Optional<?> firstElement = beans.stream().findFirst();
        firstElement.ifPresent(obj -> {
            Class<?> objectType = obj.getClass();
            assertEquals("cn.tannn.jdevelops.result.domin.Beans",objectType.getName());
        });
    }




}
