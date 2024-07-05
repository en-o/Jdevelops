package cn.tannn.jdevelops.utils.http;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

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
    void testWin2Linux() {
        // 测试 Windows 路径转换为 Linux 路径
        assertEquals("/", MacUtil.win2Linux("\\"));
        assertEquals("/path/to/file", MacUtil.win2Linux("\\path\\to\\file"));
        assertEquals("/path/to/file", MacUtil.win2Linux("/path/to/file")); // 已经是 Linux 路径
        assertEquals("", MacUtil.win2Linux("")); // 空字符串
        assertNull(MacUtil.win2Linux(null)); // null
    }

    @Test
    void testLinux2Win() {
        // 测试 Linux 路径转换为 Windows 路径
        assertEquals("\\", MacUtil.linux2Win("/"));
        assertEquals("\\path\\to\\file", MacUtil.linux2Win("/path/to/file"));
        assertEquals("\\path\\to\\file", MacUtil.linux2Win("\\path\\to\\file")); // 已经是 Windows 路径
        assertEquals("", MacUtil.linux2Win("")); // 空字符串
        assertNull(MacUtil.linux2Win(null)); // null
    }
}
