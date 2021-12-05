package cn.jdevelop.string;

import org.junit.Test;

public class StringHideTest {



    @Test
    public void hideCerCardNum() {
        System.out.println("hideCerCardNum = " + StringHide.hideCerCardNum("000230192512056251", 2, 5));
    }

    @Test
    public void idPassport() {
        System.out.println("idPassport = " + StringHide.idPassport("154789369"));
    }

    @Test
    public void hidePhone() {
        System.out.println("hidePhone = " + StringHide.hidePhone("13320385140", 2));
    }

    @Test
    public void email() {
        System.out.println("email = " + StringHide.email("38asda41@163.com"));
        System.out.println("email = " + StringHide.email("132我51@qq.com"));
    }

    @Test
    public void bankCard() {
        System.out.println("bankCard = " + StringHide.bankCard("61251495156475491"));
    }

    @Test
    public void address() {
        System.out.println("address = " + StringHide.address("重庆市重庆市重庆市", 2));
    }
}