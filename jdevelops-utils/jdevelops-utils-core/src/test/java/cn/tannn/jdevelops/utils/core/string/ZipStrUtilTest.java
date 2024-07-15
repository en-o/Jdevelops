package cn.tannn.jdevelops.utils.core.string;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class ZipStrUtilTest {


    final static String str_jiami = """
            {
                "code": 0,
                "data": {
                    "sh": 1,
                    "gd": 5,
                    "sc": 7,
                    "sh": 1,
                    "hb": 1,
                    "zj": 1,
                    "jx": 1,
                    "bj": 1,
                    "ah": 1
                },
                "description" : "",
                "success" : true
            }
            """;

    final static String str_jiemi = "H4sIAAAAAAAA/33OwQqAIAyA4btPITt7qEMEvY1NyTpkOIMoeveWFYWHvP0f23ATkh+gNxYaWagrjY6ac0uVhBx3qV7oDEP1AUKGWv2suDaDdchgWDJo8wmdjqben89awtBPsfcjyEYC3E4zoiU6LYbZil0cn3upMO0AAAA=";


    @Test
    void gunzip() {
        assertEquals(ZipStrUtil.gunzip(str_jiemi),
                str_jiami);
    }

    @Test
    void gzip() {
        assertEquals(ZipStrUtil.gzip(str_jiami),
                str_jiemi);
    }
}
