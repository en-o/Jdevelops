package cn.tannn.jdevelops.utils.core.file;//package cn.jdevelops.util.core.file;
//
//import org.junit.FixMethodOrder;
//import org.junit.Test;
//import org.junit.runners.MethodSorters;
//
//import java.io.IOException;
//import java.util.Set;
//
//import static org.junit.Assert.*;
//
//
//
//@FixMethodOrder(MethodSorters.NAME_ASCENDING)
//public class HostsUtilTest {
//
//
//    @Test
//    public void getHostFile() {
//        if ("linux".equalsIgnoreCase(System.getProperty("os.name"))) {
//            assertEquals(HostsUtil.getHostFile(), "/etc/hosts");
//        } else {
//            String fileName = System.getenv("windir") + "\\system32\\drivers\\etc\\hosts";
//            assertEquals(HostsUtil.getHostFile(), fileName);
//        }
//    }
//
//    @Test
//    public void read() throws IOException {
//        Set<String> read = HostsUtil.read();
//        read.forEach(System.out::println);
//        assertTrue(true);
//    }
//
//    @Test
//    public void append() throws IOException {
//        assertTrue(HostsUtil.append("192.168.0.65", "tan.cn"));
//    }
//
//    @Test
//    public void exists() throws IOException {
//        assertFalse(HostsUtil.exists("192.168.0.61", "tan.cn"));
//    }
//
//    @Test
//    public void delete() throws IOException {
//        assertTrue(HostsUtil.delete("192.168.0.65", "tan.cn"));
//    }
//}
