package cn.jdevelops.api.result.request;

import junit.framework.TestCase;

import java.util.Arrays;

public class SortDTOTest extends TestCase {

    public void testGetOrderBy() {
        SortDTO sortDTO = new SortDTO();
        assertEquals(sortDTO.getOrderDesc().toString(),"1");
        assertEquals(Arrays.toString(sortDTO.getOrderBy()),"[id]");

        // 测试 超过阈值之后使用默认值 1
        sortDTO = new SortDTO(3,"name");
        assertEquals(sortDTO.getOrderDesc().toString(),"1");
        assertEquals(Arrays.toString(sortDTO.getOrderBy()),"[name]");

        // 测试 超过阈值之后使用默认值 1
        sortDTO = new SortDTO(-1,"name");
        assertEquals(sortDTO.getOrderDesc().toString(),"1");
        assertEquals(Arrays.toString(sortDTO.getOrderBy()),"[name]");
    }
}
