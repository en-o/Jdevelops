package com.detabes.string.core;

import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.*;

public class StringHideTest {


    @Test
    public void hideName() {
        System.out.println("hideName = " + StringHide.hideName("谭ad", 1));
        System.out.println("hideName1 = " + StringHide.hideName("谭a"));
        System.out.println("hideName1 = " + StringHide.hideName("谭"));
        System.out.println("hideName1 = " + StringHide.hideName("谭您啊啊啊"));
    }


    @Test
    public void hideCerCardNum() {
        System.out.println("hideCerCardNum = " + StringHide.hideCerCardNum("000230192512056251", 2,5));
    }

    @Test
    public void idPassport() {
        System.out.println("idPassport = " + StringHide.idPassport("154789369"));
    }

    @Test
    public void hidePhone() {
        System.out.println("hidePhone = " + StringHide.hidePhone("13320385140",2));
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
}