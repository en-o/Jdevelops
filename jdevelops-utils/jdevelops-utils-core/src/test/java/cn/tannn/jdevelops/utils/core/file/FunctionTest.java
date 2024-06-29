package cn.tannn.jdevelops.utils.core.file;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 测试用的类
 *
 * @author <a href="https://tannn.cn/">tan</a>
 * @date 2023/7/79:45
 */
public class FunctionTest {

    public String test(){
        return "function";
    }

    public String test2(String parameter){
        return "function String => "+parameter;
    }

    public String test3(Integer parameter){
        return "function Integer => "+parameter;
    }

    public String test4(Map<String,String> parameter){
        return "function Map => "+parameter.toString();
    }

    public String test5(List<String> parameter){
        return "function List => "+parameter.toString();
    }

    public String test6(Object parameter){
        return "function Object => "+parameter.toString();
    }

    public Map<String,String> test7(String parameter){
        return new HashMap<String,String>(){{
            put("function","function return map => "+parameter.toString());
        }};
    }
}
