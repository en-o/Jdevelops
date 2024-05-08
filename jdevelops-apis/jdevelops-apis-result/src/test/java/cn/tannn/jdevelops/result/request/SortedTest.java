package cn.tannn.jdevelops.result.request;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class SortedTest {



    @Test
    void getOrderBy() {
        Sorted sortDTO = new Sorted();
        assertEquals(sortDTO.getOrderDesc().toString(),"1");
        assertEquals(Arrays.toString(sortDTO.getOrderBy()),"[id]");

        // 测试 超过阈值之后使用默认值 1
        sortDTO = new Sorted(3,"name");
        assertEquals(sortDTO.getOrderDesc().toString(),"1");
        assertEquals(Arrays.toString(sortDTO.getOrderBy()),"[name]");

        // 测试 超过阈值之后使用默认值 1
        sortDTO = new Sorted(-1,"name");
        assertEquals(sortDTO.getOrderDesc().toString(),"1");
        assertEquals(Arrays.toString(sortDTO.getOrderBy()),"[name]");
    }
}
