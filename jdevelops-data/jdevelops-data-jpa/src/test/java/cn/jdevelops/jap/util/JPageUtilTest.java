package cn.jdevelops.jap.util;

import cn.jdevelops.api.result.request.SortDTO;
import cn.jdevelops.data.jap.util.JPageUtil;
import org.junit.Test;

import static org.junit.Assert.*;

public class JPageUtilTest {

    @Test
    public void getSv2S() {
        SortDTO sortVO = new SortDTO();
        assertEquals(JPageUtil.getSv2S(sortVO).toString(),"id: DESC");
        sortVO.setOrderDesc(0);
        assertEquals(JPageUtil.getSv2S(sortVO).toString(),"id: ASC");
        sortVO.setOrderDesc(1);
        sortVO.setOrderBy("name");
        assertEquals(JPageUtil.getSv2S(sortVO).toString(),"name: DESC");
        sortVO = new SortDTO();
        sortVO.setOrderBy("name");
        assertEquals(JPageUtil.getSv2S(sortVO).toString(),"name: DESC");
    }
}
