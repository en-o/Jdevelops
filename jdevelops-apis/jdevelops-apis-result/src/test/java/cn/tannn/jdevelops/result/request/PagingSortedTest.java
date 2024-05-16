package cn.tannn.jdevelops.result.request;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PagingSortedTest {

    @Test
    void getSorts() {
        PagingSorted sortPageDTO = new PagingSorted();
        assertEquals("0", sortPageDTO.getPageIndex().toString());
        assertEquals("20", sortPageDTO.getPageSize().toString());
        assertEquals("[id]", Arrays.toString(sortPageDTO.getSorts().get(0).getOrderBy()));
        assertEquals("1", sortPageDTO.getSorts().get(0).getOrderDesc().toString());
    }

    @Test
    void getSortsPage() {
        PagingSorted sortPageDTO = new PagingSorted();
        sortPageDTO.setPageIndex(2);
        sortPageDTO.setPageSize(21);
        assertEquals((2 - 1) + "", sortPageDTO.getPageIndex().toString());
        assertEquals("21", sortPageDTO.getPageSize().toString());
        assertEquals("[id]", Arrays.toString(sortPageDTO.getSorts().get(0).getOrderBy()));
        assertEquals("1", sortPageDTO.getSorts().get(0).getOrderDesc().toString());
    }


    @Test
    void getSortsSorts() {
        PagingSorted sortPageDTO = new PagingSorted();
        sortPageDTO.setSorts(Arrays.asList(new Sorted(), new Sorted(0, "name")));
        assertEquals("0", sortPageDTO.getPageIndex().toString());
        assertEquals("20", sortPageDTO.getPageSize().toString());
        assertEquals("[id]", Arrays.toString(sortPageDTO.getSorts().get(0).getOrderBy()));
        assertEquals("1", sortPageDTO.getSorts().get(0).getOrderDesc().toString());
        assertEquals("[name]", Arrays.toString(sortPageDTO.getSorts().get(1).getOrderBy()));
        assertEquals("0", sortPageDTO.getSorts().get(1).getOrderDesc().toString());
    }


    @Test
    void testPagingSorted() {
        PagingSorted sortPageDTO = new PagingSorted(1, 2, new Sorted());
        assertEquals((0) + "", sortPageDTO.getPageIndex().toString());
        assertEquals("2", sortPageDTO.getPageSize().toString());
        assertEquals("[id]", Arrays.toString(sortPageDTO.getSorts().get(0).getOrderBy()));
        assertEquals("1", sortPageDTO.getSorts().get(0).getOrderDesc().toString());
    }


    @Test
    void gortPageDTO2() {
        PagingSorted sortPageDTO = new PagingSorted(2, new Sorted());
        assertEquals("0", sortPageDTO.getPageIndex().toString());
        assertEquals("2", sortPageDTO.getPageSize().toString());
        assertEquals("[id]", Arrays.toString(sortPageDTO.getSorts().get(0).getOrderBy()));
        assertEquals("1", sortPageDTO.getSorts().get(0).getOrderDesc().toString());
    }


    @Test
    void testPagingSorted3() {
        PagingSorted sortPageDTO = new PagingSorted(1, 2, 0, "name");
        assertEquals("0", sortPageDTO.getPageIndex().toString());
        assertEquals("2", sortPageDTO.getPageSize().toString());
        assertEquals("[name]", Arrays.toString(sortPageDTO.getSorts().get(0).getOrderBy()));
        assertEquals("0", sortPageDTO.getSorts().get(0).getOrderDesc().toString());
    }


    @Test
    void sortPageDTO4() {
        PagingSorted sortPageDTO = new PagingSorted(1, 2, 0, "name", "id");
        assertEquals("0", sortPageDTO.getPageIndex().toString());
        assertEquals("2", sortPageDTO.getPageSize().toString());
        assertEquals("[name, id]", Arrays.toString(sortPageDTO.getSorts().get(0).getOrderBy()));
        assertEquals("0", sortPageDTO.getSorts().get(0).getOrderDesc().toString());
    }


    @Test
    void sortPageDef() {
        PagingSorted sortPageDTO = new PagingSorted();
        assertEquals("0", sortPageDTO.getPageIndex().toString());
        assertEquals("20", sortPageDTO.getPageSize().toString());
        assertEquals("[id]", Arrays.toString(sortPageDTO.getSorts().get(0).getOrderBy()));
        assertEquals("1", sortPageDTO.getSorts().get(0).getOrderDesc().toString());

        sortPageDTO = new PagingSorted().append("name");
        assertEquals("0", sortPageDTO.getPageIndex().toString());
        assertEquals("20", sortPageDTO.getPageSize().toString());
        assertEquals("[name]", Arrays.toString(sortPageDTO.getSorts().get(0).getOrderBy()));
        assertEquals("1", sortPageDTO.getSorts().get(0).getOrderDesc().toString());

        sortPageDTO = new PagingSorted().append(0, "name");
        assertEquals("0", sortPageDTO.getPageIndex().toString());
        assertEquals("20", sortPageDTO.getPageSize().toString());
        assertEquals("[name]", Arrays.toString(sortPageDTO.getSorts().get(0).getOrderBy()));
        assertEquals("0", sortPageDTO.getSorts().get(0).getOrderDesc().toString());

        sortPageDTO = new PagingSorted(20, new Sorted("id")).append(0, "name");
        assertEquals("0", sortPageDTO.getPageIndex().toString());
        assertEquals("20", sortPageDTO.getPageSize().toString());
        assertEquals("[id]", Arrays.toString(sortPageDTO.getSorts().get(0).getOrderBy()));
        assertEquals("1", sortPageDTO.getSorts().get(0).getOrderDesc().toString());


        sortPageDTO = new PagingSorted(20, new Sorted(0, "id")).append(0, "name", "sex");
        assertEquals("0", sortPageDTO.getPageIndex().toString());
        assertEquals("20", sortPageDTO.getPageSize().toString());
        assertEquals("[id]", Arrays.toString(sortPageDTO.getSorts().get(0).getOrderBy()));
        assertEquals("[name, sex]", Arrays.toString(sortPageDTO.getSorts().get(1).getOrderBy()));
        assertEquals(2, sortPageDTO.getSorts().size());
        assertEquals("0", sortPageDTO.getSorts().get(0).getOrderDesc().toString());


        sortPageDTO = new PagingSorted().append(0, "name", "sex");
        assertEquals("0", sortPageDTO.getPageIndex().toString());
        assertEquals("20", sortPageDTO.getPageSize().toString());
        assertEquals("[name, sex]", Arrays.toString(sortPageDTO.getSorts().get(0).getOrderBy()));
        assertEquals(1, sortPageDTO.getSorts().size());
        assertEquals("0", sortPageDTO.getSorts().get(0).getOrderDesc().toString());


        sortPageDTO = new PagingSorted().fixSort("name", "sex");
        assertEquals("0", sortPageDTO.getPageIndex().toString());
        assertEquals("20", sortPageDTO.getPageSize().toString());
        assertEquals("[name, sex]", Arrays.toString(sortPageDTO.getSorts().get(0).getOrderBy()));
        assertEquals(1, sortPageDTO.getSorts().size());
        assertEquals("1", sortPageDTO.getSorts().get(0).getOrderDesc().toString());


        sortPageDTO = new PagingSorted(20, new Sorted(0, "id")).fixSort( "name", "sex");
        assertEquals("0", sortPageDTO.getPageIndex().toString());
        assertEquals("20", sortPageDTO.getPageSize().toString());
        assertEquals("[id]", Arrays.toString(sortPageDTO.getSorts().get(0).getOrderBy()));
        assertEquals(1, sortPageDTO.getSorts().size());
        assertEquals("0", sortPageDTO.getSorts().get(0).getOrderDesc().toString());


        sortPageDTO = new PagingSorted(20, new Sorted(0, "id")).fixSort(0, "name", "sex");
        assertEquals("0", sortPageDTO.getPageIndex().toString());
        assertEquals("20", sortPageDTO.getPageSize().toString());
        assertEquals("[id]", Arrays.toString(sortPageDTO.getSorts().get(0).getOrderBy()));
        assertEquals(1, sortPageDTO.getSorts().size());
        assertEquals("0", sortPageDTO.getSorts().get(0).getOrderDesc().toString());



    }
}
