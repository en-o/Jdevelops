package cn.jdevelops.api.result.request;

import junit.framework.TestCase;

import java.util.Arrays;

public class SortPageDTOTest extends TestCase {


    public void testGetSorts() {
        SortPageDTO sortPageDTO = new SortPageDTO();
        assertEquals("0", sortPageDTO.getPageIndex().toString());
        assertEquals("20", sortPageDTO.getPageSize().toString());
        assertEquals("[id]",Arrays.toString(sortPageDTO.getSorts().get(0).getOrderBy()));
        assertEquals("1", sortPageDTO.getSorts().get(0).getOrderDesc().toString());
    }

    public void testGetSortsPage() {
        SortPageDTO sortPageDTO = new SortPageDTO();
        sortPageDTO.setPageIndex(2);
        sortPageDTO.setPageSize(21);
        assertEquals((2 - 1) + "", sortPageDTO.getPageIndex().toString());
        assertEquals("21", sortPageDTO.getPageSize().toString());
        assertEquals("[id]",Arrays.toString(sortPageDTO.getSorts().get(0).getOrderBy()));
        assertEquals("1", sortPageDTO.getSorts().get(0).getOrderDesc().toString());
    }

    public void testGetSortsSorts() {
        SortPageDTO sortPageDTO = new SortPageDTO();
        sortPageDTO.setSorts(Arrays.asList(new SortDTO(), new SortDTO(0, "name")));
        assertEquals("0", sortPageDTO.getPageIndex().toString());
        assertEquals("20", sortPageDTO.getPageSize().toString());
        assertEquals("[id]",Arrays.toString(sortPageDTO.getSorts().get(0).getOrderBy()));
        assertEquals("1", sortPageDTO.getSorts().get(0).getOrderDesc().toString());
        assertEquals("[name]",Arrays.toString(sortPageDTO.getSorts().get(1).getOrderBy()));
        assertEquals("0", sortPageDTO.getSorts().get(1).getOrderDesc().toString());
    }


    public void testSortPageDTO() {
        SortPageDTO sortPageDTO = new SortPageDTO(1, 2, new SortDTO());
        assertEquals((0) + "", sortPageDTO.getPageIndex().toString());
        assertEquals("2", sortPageDTO.getPageSize().toString());
        assertEquals("[id]",Arrays.toString(sortPageDTO.getSorts().get(0).getOrderBy()));
        assertEquals("1", sortPageDTO.getSorts().get(0).getOrderDesc().toString());
    }


    public void testSortPageDTO2() {
        SortPageDTO sortPageDTO = new SortPageDTO(2, new SortDTO());
        assertEquals("0", sortPageDTO.getPageIndex().toString());
        assertEquals("2", sortPageDTO.getPageSize().toString());
        assertEquals("[id]",Arrays.toString(sortPageDTO.getSorts().get(0).getOrderBy()));
        assertEquals("1", sortPageDTO.getSorts().get(0).getOrderDesc().toString());
    }


    public void testSortPageDTO3() {
        SortPageDTO sortPageDTO = new SortPageDTO(1, 2, 0, "name");
        assertEquals("0", sortPageDTO.getPageIndex().toString());
        assertEquals("2", sortPageDTO.getPageSize().toString());
        assertEquals("[name]",Arrays.toString(sortPageDTO.getSorts().get(0).getOrderBy()));
        assertEquals("0", sortPageDTO.getSorts().get(0).getOrderDesc().toString());
    }


    public void testSortPageDTO4() {
        SortPageDTO sortPageDTO = new SortPageDTO(1, 2, 0, "name","id");
        assertEquals("0", sortPageDTO.getPageIndex().toString());
        assertEquals("2", sortPageDTO.getPageSize().toString());
        assertEquals("[name, id]",Arrays.toString(sortPageDTO.getSorts().get(0).getOrderBy()));
        assertEquals("0", sortPageDTO.getSorts().get(0).getOrderDesc().toString());
    }



    public void testSortPageDef() {
        SortPageDTO sortPageDTO = new SortPageDTO();
        assertEquals("0", sortPageDTO.getPageIndex().toString());
        assertEquals("20", sortPageDTO.getPageSize().toString());
        assertEquals("[id]",Arrays.toString(sortPageDTO.getSorts().get(0).getOrderBy()));
        assertEquals("1", sortPageDTO.getSorts().get(0).getOrderDesc().toString());

        sortPageDTO = new SortPageDTO().optionsDefOrderBy("name");
        assertEquals("0", sortPageDTO.getPageIndex().toString());
        assertEquals("20", sortPageDTO.getPageSize().toString());
        assertEquals("[name]",Arrays.toString(sortPageDTO.getSorts().get(0).getOrderBy()));
        assertEquals("1", sortPageDTO.getSorts().get(0).getOrderDesc().toString());


        sortPageDTO = new SortPageDTO().optionsDefOrderBy(0, "name");
        assertEquals("0", sortPageDTO.getPageIndex().toString());
        assertEquals("20", sortPageDTO.getPageSize().toString());
        assertEquals("[name]",Arrays.toString(sortPageDTO.getSorts().get(0).getOrderBy()));
        assertEquals("0", sortPageDTO.getSorts().get(0).getOrderDesc().toString());
    }


}
