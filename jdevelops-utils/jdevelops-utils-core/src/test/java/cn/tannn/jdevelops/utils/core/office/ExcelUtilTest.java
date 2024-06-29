package cn.tannn.jdevelops.utils.core.office;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ExcelUtilTest {

    @Test
    void genSheet() {
        System.out.println();
        assertEquals("青山少年·生态艺术（照母山人和水库）上午1030-1200"
                ,ExcelUtil.genSheet("青山少年·生态艺术（照母山人和水库）上午：10:30-12:00"));

        assertEquals("青山少年·生态艺术（照母山人和水库）上午1030-1200"
                ,ExcelUtil.genSheet("青山少年·生态艺术（照母山人和水库）上午[1030-1200]"));

        assertEquals("青山少年·生态艺术（照母山人和水库）上午1030-1200"
                ,ExcelUtil.genSheet("青山少年·生态艺术（照母山人和水库）上午[1030-1200"));

        assertEquals("青山少年·生态艺术（照母山人和水库）上午1030-1200"
                ,ExcelUtil.genSheet("青山少年·生态艺术（照母山人和水库）上午1030-1200"));

        assertEquals("青山少年·生态艺术（照母山人和水库）上午1030-1200"
                ,ExcelUtil.genSheet("青山少年·生态艺术（照母山人和水库）上午1030-1200?"));

        assertEquals("青山少年·生态艺术（照母山人和水库）上午1030-1200"
                ,ExcelUtil.genSheet("青山少年·生态艺术（照母山人和水库）上午1030-1200/?"));

        assertEquals("青山少年·生态艺术（照母山人和水库）上午1030-1200"
                ,ExcelUtil.genSheet("青山少年·生态艺术（照母山人和水库）上午1030-1200*"));

        assertEquals("青山少年·生态艺术（照母山人和水库）上午1030-1200"
                ,ExcelUtil.genSheet("青山少年·生态艺术（照母山人和水库）上午1030-1200/"));
    }
}
