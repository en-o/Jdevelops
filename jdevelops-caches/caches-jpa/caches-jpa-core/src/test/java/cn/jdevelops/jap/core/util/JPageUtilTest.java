package cn.jdevelops.jap.core.util;

import cn.jdevelops.result.response.SortVO;
import org.junit.Test;
import org.springframework.data.domain.Sort;

import static org.junit.Assert.*;

public class JPageUtilTest {

    @Test
    public void getSv2S() {
        SortVO sortVO = new SortVO();
        assertEquals(JPageUtil.getSv2S(sortVO).toString(),"id: DESC");
        sortVO.setOrderDesc(0);
        assertEquals(JPageUtil.getSv2S(sortVO).toString(),"id: ASC");
        sortVO.setOrderDesc(1);
        sortVO.setOrderBy("name");
        assertEquals(JPageUtil.getSv2S(sortVO).toString(),"name: DESC");
        sortVO = new SortVO();
        sortVO.setOrderBy("name");
        assertEquals(JPageUtil.getSv2S(sortVO).toString(),"name: DESC");
    }
}
