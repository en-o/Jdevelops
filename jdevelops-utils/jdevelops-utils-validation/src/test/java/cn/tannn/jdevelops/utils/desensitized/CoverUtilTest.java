package cn.tannn.jdevelops.utils.desensitized;


import cn.tannn.jdevelops.utils.desensitized.util.CoverUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CoverUtilTest {

    @Test
    public void chineseName() {
        assertEquals(CoverUtil.chineseName("谭宁"),"谭*");
        assertEquals(CoverUtil.chineseName("谭宁宁"),"谭**");
    }

    @Test
    public void idCardNum() {
        assertEquals(CoverUtil.idCardNum("200220129212056022",3,4),"200***********6022");
    }

    @Test
    public void fixedPhone() {
        assertEquals(CoverUtil.fixedPhone("03168228737"),"0316*****37");
    }

    @Test
    public void mobilePhone() {
        assertEquals(CoverUtil.mobilePhone("13321285210"),"133****5210");
    }

    @Test
    public void address() {
        assertEquals(CoverUtil.address("成都市金牛区二环路北三段",8),"成都市金牛区二环****");
        assertEquals(CoverUtil.address("成都市金牛区二环路北三段",2),"成都**********");
    }

    @Test
    public void email() {
        assertEquals(CoverUtil.email("c66@163.com"),"c**@163.com");
    }

    @Test
    public void password() {
        assertEquals(CoverUtil.password("123131312"),"******");
    }
}
