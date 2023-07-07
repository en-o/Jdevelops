package cn.jdevelops.util.core.file;

import junit.framework.TestCase;
import org.junit.jupiter.api.Assertions;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class FunctionUtilTest extends TestCase {


    public void testExecuteDeclaredMethod() {
        assertEquals(FunctionUtil.executeDeclaredMethod(FunctionTest.class, "test"), "function");
        assertEquals(FunctionUtil.executeDeclaredMethod(FunctionTest.class, "test2", "hi"), "function String => hi");
        assertEquals(FunctionUtil.executeDeclaredMethod(FunctionTest.class, "test3", 1), "function Integer => 1");
        assertEquals(FunctionUtil.executeDeclaredMethod(FunctionTest.class, "test4", new HashMap<String, String>() {{
            put("function", "tan");
        }}), "function Map => {function=tan}");

        assertEquals(FunctionUtil.executeDeclaredMethod(FunctionTest.class, "test5", Arrays.asList("function", "tan")),
                "function List => [function, tan]");

        // 方法的参数类型一定要明确
        Assertions.assertThrows(NullPointerException.class,FunctionUtil.executeDeclaredMethod(FunctionTest.class, "test7", 1));

        assertEquals(FunctionUtil.executeDeclaredMethod(FunctionTest.class, "test7", "dd").toString(),
                "{function=function return map => dd}");
    }
}
