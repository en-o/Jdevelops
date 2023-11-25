package cn.jdevelops.api.result.util;

import cn.jdevelops.api.result.request.PageDTO;
import cn.jdevelops.api.result.request.SortDTO;
import cn.jdevelops.api.result.request.SortPageDTO;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class PageUtilTest {

    @Test
    public void pageDef() {
        PageDTO pageDTO = PageUtil.pageDef(new PageDTO());
        assertEquals("0",pageDTO.getPageIndex().toString());
        assertEquals("20",pageDTO.getPageSize().toString());
    }

    @Test
    public void sortDef() {
        SortDTO sortDef = PageUtil.sortDef(new SortDTO());
        assertEquals("1",sortDef.getOrderDesc().toString());
        assertEquals("[id]", Arrays.toString(sortDef.getOrderBy()));
    }

    @Test
    public void sortPageDef() {
        SortPageDTO sortPageDef = PageUtil.sortPageDef(new SortPageDTO());
        assertEquals("0",sortPageDef.getPageIndex().toString());
        assertEquals("20",sortPageDef.getPageSize().toString());
        assertEquals("1",sortPageDef.getSorts().get(0).getOrderDesc().toString());
        assertEquals("[id]", Arrays.toString(sortPageDef.getSorts().get(0).getOrderBy()));
    }
}
