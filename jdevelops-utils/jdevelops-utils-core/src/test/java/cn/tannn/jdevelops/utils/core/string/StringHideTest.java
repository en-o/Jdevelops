package cn.tannn.jdevelops.utils.core.string;


import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StringHideTest {


    @Test
    void testHideCerCardNum() {
        assertEquals(StringHide.hideCerCardNum("000230192512056251", 2, 5), "00**********56251");
    }

    @Test
    void testIdPassport() {
        assertEquals(StringHide.idPassport("154789369"), "15****369");
    }

    @Test
    void testHidePhone() {
        assertEquals(StringHide.hidePhone("13320385140", 2), "*********40");
    }

    @Test
    void testEmail() {
        assertEquals(StringHide.email("38asda41@163.com"), "3*******@163.com");
        assertEquals(StringHide.email("132我51@qq.com"), "1*****@qq.com");
    }

    @Test
    void testBankCard() {
        assertEquals(StringHide.bankCard("61251495156475491"), "6125*******5491");
    }

    @Test
    void testAddress() {
        assertEquals(StringHide.address("重庆市重庆市重庆市", 2), "重庆市重庆市重**");
    }

    @Test
    void testNodeHide() {
        assertEquals(StringHide.nodeHide(Arrays.asList("小猫", "小狗"), "你好,小猫是小狗的小狗的小猫猫的小狗狗"),
                "你好,***是***的***的***猫的***狗");
    }

    @Test
    void testTestNodeHide() {
        assertEquals(StringHide.nodeHide(Arrays.asList("小猫", "小狗"), "你好,小猫是小狗的小狗的小猫猫的小狗狗", "hi"),
                "你好,hi是hi的hi的hi猫的hi狗");
    }
}
