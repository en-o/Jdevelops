package cn.tannn.jdevelops.jpa.request;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Sort;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PagingsTest {

    @Test
    void pageable() {
        assertEquals("Page request [number: 0, size 20, sort: UNSORTED]", new Pagings().pageable().toString());
        assertEquals("Page request [number: 0, size 20, sort: UNSORTED]", Pagings.pageable(new Pagings()).toString());
        assertEquals("Page request [number: 0, size 20, sort: id: DESC]", Pagings.pageable(
                new Pagings(),
                new Sorteds()
        ).toString());
    }

    @Test
    void sort() {
        assertEquals("Paging{pageIndex=null, pageSize=null}, Sort{sort='id: ASC'}"
                , new Pagings().sort(Sort.by("id")).toString());
        assertEquals("Paging{pageIndex=1, pageSize=2}, Sort{sort='id: ASC'}"
                , new Pagings(1, 2).sort(Sort.by("id")).toString());
    }


    @Test
    void defs() {
        assertEquals("Paging{pageIndex=null, pageSize=null}, Sort{sort='null'}", Pagings.defs().toString());
        assertEquals(0, Pagings.defs().getPageIndex());
        assertEquals(20, Pagings.defs().getPageSize());
    }


    @Test
    void pageableSorted() {
        assertEquals("Page request [number: 0, size 20, sort: name: ASC]", Pagings.pageableSorted(Sort.by("name")).toString());
        assertEquals("Page request [number: 0, size 20, sort: name: DESC]", Pagings.pageableSorted(Sort.by("name").descending()).toString());
    }

    @Test
    void testPageableSorted() {
        assertEquals("Page request [number: 0, size 20, sort: id: DESC]", Pagings.pageableSorted(new Sorteds()).toString());
        assertEquals("Page request [number: 0, size 20, sort: name: DESC]", Pagings.pageableSorted(new Sorteds("name")).toString());
    }
}
