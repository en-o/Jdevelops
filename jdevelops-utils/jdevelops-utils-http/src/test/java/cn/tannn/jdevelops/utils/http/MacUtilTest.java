package cn.tannn.jdevelops.utils.http;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MacUtilTest {

    @Test
    void getMacAddressList() throws IOException {
        System.out.println(MacUtil.getMacAddressList());
    }

    @Test
    void getMacAddress() {
        // 00-15-5D-89-B5-5B
        System.out.println(MacUtil.getMacAddress());
    }

    @Test
    void getComputerName() {
        // TAN
        System.out.println(MacUtil.getComputerName());
    }

    @Test
    void getComputerId() {
        // TAN
        System.out.println(MacUtil.getComputerId());
    }

    @Test
    void win2Linux() {
        if(System.getProperty("os.name").toLowerCase().contains("win")){
            assertEquals("\\",MacUtil.win2Linux("/"));
            assertEquals("\\",MacUtil.win2Linux("\\"));
        }else {
            assertEquals("/",MacUtil.win2Linux("/"));
            assertEquals("\\",MacUtil.win2Linux("\\"));
        }

    }
}
