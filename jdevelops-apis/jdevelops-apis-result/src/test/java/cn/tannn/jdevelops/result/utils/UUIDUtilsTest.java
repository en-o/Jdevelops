package cn.tannn.jdevelops.result.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class UUIDUtilsTest {

    @Test
    void generateShortUuid() {
        Assertions.assertNotNull(UUIDUtils.getInstance().generateShortUuid());
    }

    @Test
    void generateShortUuidLong() {
        Assertions.assertNotNull(UUIDUtils.getInstance().generateShortUuidLong());
    }
}
