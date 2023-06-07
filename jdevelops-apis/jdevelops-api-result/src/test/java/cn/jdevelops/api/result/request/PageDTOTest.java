package cn.jdevelops.api.result.request;

import junit.framework.TestCase;

public class PageDTOTest extends TestCase {

    public void testPage(){
        PageDTO pageDTO = new PageDTO();
        assertEquals(pageDTO.getPageIndex().toString(),"0");
        PageDTO pageDTO2 = new PageDTO(2);
        assertEquals(pageDTO2.getPageIndex().toString(),"0");

        PageDTO pageDTO3 = new PageDTO(1,2);
        assertEquals(pageDTO3.getPageIndex().toString(),"0");
        PageDTO pageDTO4 = new PageDTO(2,2);
        assertEquals(pageDTO4.getPageIndex().toString(),"1");
    }

}
