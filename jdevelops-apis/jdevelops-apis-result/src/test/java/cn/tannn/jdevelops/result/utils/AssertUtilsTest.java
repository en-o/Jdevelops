package cn.tannn.jdevelops.result.utils;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class AssertUtilsTest {

    @Test
    void isTrue() {
        assertThrows(IllegalArgumentException.class, () -> {
            AssertUtils.isTrue(false,"error");
        });
        assertDoesNotThrow(() -> {
            AssertUtils.isTrue(true,"ok");
        });
    }

    @Test
    void testIsTrue() {
        assertThrows(RuntimeException.class, () -> {
            AssertUtils.isTrue(false, () -> {
                throw new RuntimeException("error");
            });
        });
        assertDoesNotThrow(() -> {
            AssertUtils.isTrue(true, () -> {
                throw new RuntimeException("error");
            });
        });
    }

    @Test
    void testIsTrue1() {
        assertThrows(IllegalArgumentException.class, () -> {
            AssertUtils.isTrue(false);
        });
        assertDoesNotThrow(() -> {
            AssertUtils.isTrue(true);
        });
    }

    @Test
    void isNull() {
        assertDoesNotThrow(() -> {
            AssertUtils.isNull(null,"ok");
        });
        assertThrows(IllegalArgumentException.class, () -> {
            AssertUtils.isNull("","error");
        });
        assertThrows(IllegalArgumentException.class, () -> {
            AssertUtils.isNull(" ","error");
        });
        assertThrows(IllegalArgumentException.class, () -> {
            AssertUtils.isNull("abc","error");
        });

    }

    @Test
    void testIsNull() {
        assertDoesNotThrow(() -> {
            AssertUtils.isNull(null,() -> { throw new RuntimeException("ok");});
        });
        assertThrows(RuntimeException.class, () -> {
            AssertUtils.isNull("",() -> { throw new RuntimeException("error");});
        });
        assertThrows(RuntimeException.class, () -> {
            AssertUtils.isNull(" ",() -> { throw new RuntimeException("error");});
        });
        assertThrows(RuntimeException.class, () -> {
            AssertUtils.isNull("abc",() -> { throw new RuntimeException("error");});
        });
    }

    @Test
    void testIsNull1() {
        assertDoesNotThrow(() -> {
            AssertUtils.isNull(null);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            AssertUtils.isNull("");
        });
        assertThrows(IllegalArgumentException.class, () -> {
            AssertUtils.isNull(" ");
        });
        assertThrows(IllegalArgumentException.class, () -> {
            AssertUtils.isNull("abc");
        });
    }

    @Test
    void notNull() {
        assertDoesNotThrow(() -> {
            AssertUtils.notNull("","ok");
        });
        assertDoesNotThrow(() -> {
            AssertUtils.notNull(" ","ok");
        });
        assertDoesNotThrow(() -> {
            AssertUtils.notNull("abc","ok");
        });
        assertThrows(IllegalArgumentException.class, () -> {
            AssertUtils.notNull(null,"error");
        });
    }

    @Test
    void testNotNull() {
        assertDoesNotThrow(() -> {
            AssertUtils.notNull("", () -> {throw new RuntimeException("ok");});
        });
        assertDoesNotThrow(() -> {
            AssertUtils.notNull(" ", () -> {throw new RuntimeException("ok");});
        });
        assertDoesNotThrow(() -> {
            AssertUtils.notNull("abc", () -> {throw new RuntimeException("ok");});
        });
        assertThrows(RuntimeException.class, () -> {
            AssertUtils.notNull(null, () -> {throw new RuntimeException("error");});
        });
    }

    @Test
    void testNotNull1() {
        assertDoesNotThrow(() -> {
            AssertUtils.notNull("");
        });
        assertDoesNotThrow(() -> {
            AssertUtils.notNull(" ");
        });
        assertDoesNotThrow(() -> {
            AssertUtils.notNull("abc");
        });
        assertThrows(IllegalArgumentException.class, () -> {
            AssertUtils.notNull(null);
        });
    }

    @Test
    void notEmpty() {
        assertThrows(IllegalArgumentException.class, () -> {
            AssertUtils.notEmpty(new String[0],"is empty");
        });
        assertThrows(IllegalArgumentException.class, () -> {
            AssertUtils.notEmpty(null,"is empty");
        });
        assertDoesNotThrow(() -> {
            AssertUtils.notEmpty(new String[1],"is empty");
        });
    }
}
