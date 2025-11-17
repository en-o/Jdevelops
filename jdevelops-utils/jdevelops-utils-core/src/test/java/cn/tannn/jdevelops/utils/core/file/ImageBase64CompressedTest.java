package cn.tannn.jdevelops.utils.core.file;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

class ImageBase64CompressedTest {

    @Test
    void compressImageToBase64() throws IOException {
        // 读取外部文件内容 因为 java: 常量字符串过长
        String longString = new String(
                Objects.requireNonNull(Thread.currentThread().getContextClassLoader()
                        .getResourceAsStream("image.txt")).readAllBytes(),
                StandardCharsets.UTF_8
        );
        System.out.println(ImageBase64Compressed.compressImageToBase64(
                longString,
                ImageBase64Compressed.DatabaseType.MYSQL_TEXT
        ));
    }
}
