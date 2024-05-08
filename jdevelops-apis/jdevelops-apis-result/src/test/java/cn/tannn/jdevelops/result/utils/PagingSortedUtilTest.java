package cn.tannn.jdevelops.result.utils;

import cn.tannn.jdevelops.result.request.Paging;
import cn.tannn.jdevelops.result.request.PagingSorted;
import cn.tannn.jdevelops.result.request.Sorted;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PagingSortedUtilTest {

    @Test
    void pageDef() {
        Paging pageDTO = PagingSortedUtil.pageDef(new Paging());
        assertEquals("0",pageDTO.getPageIndex().toString());
        assertEquals("20",pageDTO.getPageSize().toString());
    }

    @Test
    void sortDef() {
        Sorted sortDef = PagingSortedUtil.sortDef(new Sorted());
        assertEquals("1",sortDef.getOrderDesc().toString());
        assertEquals("[id]", Arrays.toString(sortDef.getOrderBy()));
    }

    @Test
    void sortPageDef() {
        PagingSorted sortPageDef = PagingSortedUtil.sortPageDef(new PagingSorted());
        assertEquals("0",sortPageDef.getPageIndex().toString());
        assertEquals("20",sortPageDef.getPageSize().toString());
        assertEquals("1",sortPageDef.getSorts().get(0).getOrderDesc().toString());
        assertEquals("[id]", Arrays.toString(sortPageDef.getSorts().get(0).getOrderBy()));
    }
}
