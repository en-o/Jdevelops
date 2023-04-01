package cn.jdevelops.util.core.string;

import junit.framework.TestCase;

import java.util.Arrays;

public class StringHideTest  extends TestCase {


    public void testHideCerCardNum() {
        assertEquals(StringHide.hideCerCardNum("000230192512056251", 2, 5),"00**********56251");
    }

    public void testIdPassport() {
        assertEquals(StringHide.idPassport("154789369"),"15****369");
    }

    public void testHidePhone() {
        assertEquals(StringHide.hidePhone("13320385140", 2),"*********40");
    }

    public void testEmail() {
        assertEquals(StringHide.email("38asda41@163.com"),"3*******@163.com");
        assertEquals(StringHide.email("132我51@qq.com"),"1*****@qq.com");
    }

    public void testBankCard() {
        assertEquals(StringHide.bankCard("61251495156475491"),"6125*******5491");
    }

    public void testAddress() {
        assertEquals(StringHide.address("重庆市重庆市重庆市", 2),"重庆市重庆市重**");
    }

    public void testNodeHide() {
        assertEquals(StringHide.nodeHide(Arrays.asList("小猫","小狗"),"你好,小猫是小狗的小狗的小猫猫的小狗狗"),
                "你好,***是***的***的***猫的***狗");
    }

    public void testTestNodeHide() {
        assertEquals(StringHide.nodeHide(Arrays.asList("小猫","小狗"),"你好,小猫是小狗的小狗的小猫猫的小狗狗","hi"),
                "你好,hi是hi的hi的hi猫的hi狗");
    }
}
