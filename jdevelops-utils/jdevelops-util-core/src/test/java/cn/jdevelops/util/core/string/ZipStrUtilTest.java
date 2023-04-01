package cn.jdevelops.util.core.string;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ZipStrUtilTest {


    final static String str_jiami = "{\n" +
            "\t\"code\":\"0\",\n" +
            "\t\"data\":{\n" +
            "\t\t\"sh\":1,\n" +
            "\t\t\"gd\":5,\n" +
            "\t\t\"sc\":7,\n" +
            "\t\t\"hb\":1,\n" +
            "\t\t\"zj\":1,\n" +
            "\t\t\"jx\":1,\n" +
            "\t\t\"bj\":1,\n" +
            "\t\t\"ah\":1\n" +
            "\t},\n" +
            "\t\"description\":\"\",\n" +
            "\t\"success\":true\n" +
            "}";

    final static String str_jiemi = "H4sIAAAAAAAAAD3NwQqAIBAE0LN+xpw71CGC/RtbJfNQ0RpE4b+ngt3e7sDMqxV4tw6EHl0+rIkG9GqlIB40dEWLBY1VwqCpys8tfUJTuJvm/2dKi1apljvhcz3ium95sO7JxexEQPG8nE4f+HcAX5AAAAA=";


    @Test
    public void gunzip() {
        assertEquals(ZipStrUtil.gunzip(str_jiemi),
                str_jiami);
    }

    @Test
    public void gzip() {
        assertEquals(ZipStrUtil.gzip(str_jiami),
                str_jiemi);
    }
}
