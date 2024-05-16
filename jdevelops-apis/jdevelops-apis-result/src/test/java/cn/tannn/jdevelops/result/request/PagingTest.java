package cn.tannn.jdevelops.result.request;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PagingTest {

    @Test
    void page() {
        Paging pageDTO = new Paging();
        assertEquals(pageDTO.getPageIndex().toString(), "0");
        Paging pageDTO2 = new Paging(2);
        assertEquals(pageDTO2.getPageIndex().toString(), "0");

        Paging pageDTO3 = new Paging(1, 2);
        assertEquals(pageDTO3.getPageIndex().toString(), "0");
        Paging pageDTO4 = new Paging(2, 2);
        assertEquals(pageDTO4.getPageIndex().toString(), "1");
    }

    @Test
    void realPageIndex() {
        assertEquals(new Paging().realPageIndex(), 0);
        assertEquals(new Paging(1, 20).realPageIndex(), 1);
        assertEquals(new Paging(2, 20).realPageIndex(), 2);

    }

    @Test
    void getPageIndex() {
        assertEquals(new Paging().getPageIndex(), 0);
        assertEquals(new Paging(1, 20).getPageIndex(), 0);
        assertEquals(new Paging(2, 20).getPageIndex(), 1);
    }
}
