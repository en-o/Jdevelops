package cn.jdevelops.spring.entity;

import junit.framework.TestCase;
import lombok.Getter;
import lombok.Setter;

public class TypedBeanTest extends TestCase {

    public void testOf() {
        assertEquals(TypedBean.of(Apply.class).getKey(Apply::getAge),"age");
        assertEquals(TypedBean.of(Apply.class).getKey(Apply::getName),"name");
        assertEquals(TypedBean.of(Help.class).getKey(Help::getAuthor),"author");
        assertEquals(TypedBean.of(Help.class).getKey(Help::getTitle),"title");
    }

    @Getter
    @Setter
    public class Apply {
        private String age,name;
    }
    @Getter
    @Setter
    public class Help {
        private String author,title;
    }
}