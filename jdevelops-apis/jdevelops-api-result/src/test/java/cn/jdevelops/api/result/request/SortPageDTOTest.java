package cn.jdevelops.api.result.request;

import junit.framework.TestCase;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

public class SortPageDTOTest extends TestCase {


    public void testGetSorts() {
        SortPageDTO sortPageDTO = new SortPageDTO();
        assertEquals("0", sortPageDTO.getPageIndex().toString());
        assertEquals("20", sortPageDTO.getPageSize().toString());
        assertEquals("id", sortPageDTO.getSorts().get(0).getOrderBy());
        assertEquals("1", sortPageDTO.getSorts().get(0).getOrderDesc().toString());
    }

    public void testGetSortsPage() {
        SortPageDTO sortPageDTO = new SortPageDTO();
        sortPageDTO.setPageIndex(2);
        sortPageDTO.setPageSize(21);
        assertEquals((2-1)+"", sortPageDTO.getPageIndex().toString());
        assertEquals("21", sortPageDTO.getPageSize().toString());
        assertEquals("id", sortPageDTO.getSorts().get(0).getOrderBy());
        assertEquals("1", sortPageDTO.getSorts().get(0).getOrderDesc().toString());
    }

    public void testGetSortsSorts() {
        SortPageDTO sortPageDTO = new SortPageDTO();
        sortPageDTO.setSorts(Arrays.asList(new SortDTO(),new SortDTO("name",0)));
        assertEquals("0", sortPageDTO.getPageIndex().toString());
        assertEquals("20", sortPageDTO.getPageSize().toString());
        assertEquals("id", sortPageDTO.getSorts().get(0).getOrderBy());
        assertEquals("1", sortPageDTO.getSorts().get(0).getOrderDesc().toString());
        assertEquals("name", sortPageDTO.getSorts().get(1).getOrderBy());
        assertEquals("0", sortPageDTO.getSorts().get(1).getOrderDesc().toString());
    }

}
