package cn.tannn.jdevelops.utils.core.string;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class StringNumberTest {

    @Test
    void getNum() {
        assertEquals(123, (int) StringNumber.getNum("ad重负123十大.abv*asd23123.111"));
        assertEquals(12312, (int) StringNumber.getNum("12312asdasda"));
        assertEquals(12, (int) StringNumber.getNum("sda12sda312asdasda"));
        assertEquals(0, (int) StringNumber.getNum("asdasdsad"));
        assertEquals(0, (int) StringNumber.getNum(""));
        assertEquals(0, (int) StringNumber.getNum(null));
    }

    @Test
    void getAllNum() {
        assertThrows(NullPointerException.class,()-> StringNumber.getAllNum(null));
        assertEquals(StringNumber.getAllNum("ad重负123十大.abv*asd23123.111"),"12323123111");
        assertEquals(StringNumber.getAllNum(""),"");
        assertEquals(StringNumber.getAllNum(" "),"");

    }

    @Test
    void isInteger() {
        assertTrue(StringNumber.isInteger("12312"));
        assertFalse(StringNumber.isInteger("aaa111"));
        assertFalse(StringNumber.isInteger("aaa"));
        assertFalse(StringNumber.isInteger(""));
        assertFalse(StringNumber.isInteger(" "));
        assertFalse(StringNumber.isInteger(null));
    }

    @Test
    void testCountOccurrences() {
        assertEquals(StringNumber.countOccurrences("x  % {product} {storeName} €1 €2","storeName"),1);
        assertEquals(StringNumber.countOccurrences("x  % {product} {product} €1 €2","product"),2);
    }
}
