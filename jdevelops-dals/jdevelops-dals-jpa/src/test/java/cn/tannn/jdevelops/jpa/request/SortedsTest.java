package cn.tannn.jdevelops.jpa.request;

import cn.tannn.jdevelops.result.request.Sorted;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Sort;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class SortedsTest {

    @Test
    void sortSorted() {
        assertEquals("id: DESC",Sorteds.sort(new Sorted()).toString());
        assertEquals("name: DESC",Sorteds.sort(new Sorted("name")).toString());
        assertEquals("name: DESC,id: DESC",Sorteds.sort(new Sorted("name","id")).toString());
        assertEquals("name: DESC",Sorteds.sort(new Sorted(1,"name")).toString());
        assertEquals("name: DESC,id: DESC",Sorteds.sort(new Sorted(1,"name","id")).toString());
        assertEquals("name: ASC,id: ASC",Sorteds.sort(new Sorted(0,"name","id")).toString());
    }

    @Test
    void sortSorteds() {
        assertEquals("id: DESC",Sorteds.sort(new Sorteds()).toString());
        assertEquals("name: DESC",Sorteds.sort(new Sorteds("name")).toString());
        assertEquals("name: DESC,id: DESC",Sorteds.sort(new Sorteds("name","id")).toString());
        assertEquals("name: DESC",Sorteds.sort(new Sorteds(1,"name")).toString());
        assertEquals("name: DESC,id: DESC",Sorteds.sort(new Sorteds(1,"name","id")).toString());
        assertEquals("name: ASC,id: ASC",Sorteds.sort(new Sorteds(0,"name","id")).toString());
    }

    @Test
    void sorteds2Sort() {
        assertEquals("name: ASC,id: DESC",Sorteds.sorteds2Sort(Arrays.asList(
                new Sorteds(0,"name"),
                new Sorteds("id")
        )).toString());


    }

    @Test
    void sorteds2Sort2() {
        assertEquals("name: ASC,id: DESC",Sorteds.sorteds2Sort2(Arrays.asList(
                new Sorteds(0,"name"),
                new Sorteds("id")
        )).toString());
    }

    @Test
    void direction() {
        assertEquals("DESC",Sorteds.direction(1).toString());
        assertEquals("ASC",Sorteds.direction(0).toString());
    }

    @Test
    void fixSort() {
        assertEquals("Sorted{orderBy=[name], orderDesc=1}",new Sorteds().fixSort("name").toString());
        assertEquals("Sorted{orderBy=[name], orderDesc=0}",new Sorteds().fixSort(0,"name").toString());
        assertEquals("Sorted{orderBy=[id], orderDesc=1}",new Sorteds().fixSort(1).toString());
        assertEquals("Sorted{orderBy=[id], orderDesc=0}",new Sorteds(0,"id").fixSort(0,"name").toString());
    }

    @Test
    void sort() {
        assertEquals("id: DESC",new Sorteds().sort().toString());
    }

    @Test
    void sorted() {
        assertEquals("Sorted{orderBy=[id], orderDesc=1}",new Sorteds().sorted(new Sorted()).toString());
    }

    @Test
    void defs() {
        assertEquals("id: DESC",Sorteds.defs().sort().toString());
    }
}
