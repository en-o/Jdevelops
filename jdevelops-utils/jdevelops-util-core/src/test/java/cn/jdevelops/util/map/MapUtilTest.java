package cn.jdevelops.util.map;

import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.*;

public class MapUtilTest {

    @Test
    public void getKey() {
        HashMap<String, String> sMap = new HashMap<>();
        sMap.put("key1", "tan1");
        sMap.put("key2", "tan2");
        assertEquals( MapUtil.getKey(sMap,"tan1"),"key1");
        assertEquals( MapUtil.getKey(sMap,"tan2"),"key2");

        HashMap<Integer, Integer> iMap = new HashMap<>();
        iMap.put(1, 2);
        iMap.put(3, 4);
        assertEquals( (int) MapUtil.getKey(iMap,2),1);
        assertEquals( (int) MapUtil.getKey(iMap,4),3);

        HashMap<String, Integer> siMap = new HashMap<>();
        siMap.put("key1", 2);
        siMap.put("key2", 4);
        assertEquals( MapUtil.getKey(siMap,2),"key1");
        assertEquals( MapUtil.getKey(siMap,4),"key2");
    }

    @Test
    public void valueExist() {
        HashMap<String, String> sMap = new HashMap<>();
        sMap.put("key1", "tan1");
        sMap.put("key2", "tan2");
        sMap.put("key3", "tan2");
        assertTrue( MapUtil.valueExist(sMap,"tan2"));
        assertTrue( MapUtil.valueExist(sMap,"tan1"));
        assertFalse( MapUtil.valueExist(sMap,"tan3"));

        HashMap<Integer, Integer> iMap = new HashMap<>();
        iMap.put(1, 2);
        iMap.put(3, 4);
        iMap.put(5, 4);
        assertTrue( MapUtil.valueExist(iMap,2));
        assertTrue( MapUtil.valueExist(iMap,4));
        assertFalse( MapUtil.valueExist(iMap,5));

        HashMap<String, Integer> siMap = new HashMap<>();
        siMap.put("key1", 2);
        siMap.put("key2", 4);
        siMap.put("key3", 4);
        assertTrue( MapUtil.valueExist(siMap,2));
        assertTrue( MapUtil.valueExist(siMap,4));
        assertFalse( MapUtil.valueExist(siMap,5));
    }
}
