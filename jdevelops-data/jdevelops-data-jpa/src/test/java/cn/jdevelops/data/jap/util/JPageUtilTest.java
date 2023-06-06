package cn.jdevelops.data.jap.util;

import cn.jdevelops.api.result.request.PageDTO;
import cn.jdevelops.api.result.request.SortDTO;
import cn.jdevelops.api.result.request.SortPageDTO;
import org.junit.Test;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.lang.reflect.Array;
import java.util.Arrays;

import static org.junit.Assert.*;

public class JPageUtilTest {

    @Test
    public void getSv2S() {
        Sort sv2S = JPageUtil.getSv2S(new SortDTO());
        assertEquals("id: DESC",sv2S.toString());

        Sort sv2S2 = JPageUtil.getSv2S(new SortDTO(0,"name","title"));
        assertEquals("name: ASC,title: ASC",sv2S2.toString());

    }


    @Test
    public void testGetSv2S1() {
        Sort sv2S = JPageUtil.getSv2S(Arrays.asList(new SortDTO(0, "name", "title"), new SortDTO(1, "age")));
        assertEquals("name: ASC,title: ASC,age: DESC",sv2S.toString());
    }

    @Test
    public void testGetSv2S() {
        Sort sv2S = JPageUtil.getSv2S(new SortPageDTO());
        assertEquals("id: DESC",sv2S.toString());

        Sort sv2S2 = JPageUtil.getSv2S(new SortPageDTO(1,10, 0,"name","title"));
        assertEquals("name: ASC,title: ASC",sv2S2.toString());


        Sort sv2S3 = JPageUtil.getSv2S(new SortPageDTO(1,10,
                Arrays.asList(new SortDTO(0,"name","title"),new SortDTO(1,"age"))));
        assertEquals("name: ASC,title: ASC,age: DESC",sv2S3.toString());
    }

    @Test
    public void getPageable() {
        Pageable pageable = JPageUtil.getPageable(new PageDTO());
        assertEquals("Page request [number: 0, size 20, sort: UNSORTED]",pageable.toString());

        // 下标0开始，但人为习惯从1开始所以传入的是从1开始
        Pageable pageable2 = JPageUtil.getPageable(new PageDTO(2,2));
        assertEquals("Page request [number: 1, size 2, sort: UNSORTED]",pageable2.toString());
    }

    @Test
    public void testGetPageable() {
        Pageable pageable = JPageUtil.getPageable(new SortPageDTO());
        assertEquals("Page request [number: 0, size 20, sort: id: DESC]",pageable.toString());

        // 下标0开始，但人为习惯从1开始所以传入的是1
        Pageable pageable2 = JPageUtil.getPageable(new SortPageDTO(2,2));
        assertEquals("Page request [number: 1, size 2, sort: id: DESC]",pageable2.toString());

        Pageable pageable3 = JPageUtil.getPageable(new SortPageDTO(2,2, 1, "name", "age"));
        assertEquals("Page request [number: 1, size 2, sort: name: DESC,age: DESC]",pageable3.toString());
    }

    @Test
    public void testGetPageable1() {

        Pageable pageable = JPageUtil.getPageable(new PageDTO(),new SortDTO());
        assertEquals("Page request [number: 0, size 20, sort: id: DESC]",pageable.toString());


        Pageable pageable2 = JPageUtil.getPageable(new PageDTO(1,2),new SortDTO(1,"name","age"));
        assertEquals("Page request [number: 0, size 2, sort: name: DESC,age: DESC]",pageable2.toString());

    }

    @Test
    public void testGetPageable2() {
        Pageable pageable = JPageUtil.getPageable(new PageDTO(),Sort.by("name").ascending());
        assertEquals("Page request [number: 0, size 20, sort: name: ASC]",pageable.toString());


        Pageable pageable2 = JPageUtil.getPageable(new PageDTO(1,22),Sort.by("name","age").ascending());
        assertEquals("Page request [number: 0, size 22, sort: name: ASC,age: ASC]",pageable2.toString());

    }


}
