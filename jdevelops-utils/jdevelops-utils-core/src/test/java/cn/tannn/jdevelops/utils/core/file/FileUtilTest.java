package cn.tannn.jdevelops.utils.core.file;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FileUtilTest {

    @Test
    void getUUIDFile() {
        // "dfdba847-b993-495f-85ff-7b6581835d55.txt",
        System.out.println(FileUtil.getUUIDFile("test.txt"));
    }

    @Test
    void getUUIDFile2() {
        // "f86d0f2e-fe6c-4f76-9b86-0efc3e54dd08",
        System.out.println(FileUtil.getUUIDFile2("test.txt"));
    }

    @Test
    void getExt() {
        assertEquals(".txt", FileUtil.getExt("test.txt"));
    }

}
