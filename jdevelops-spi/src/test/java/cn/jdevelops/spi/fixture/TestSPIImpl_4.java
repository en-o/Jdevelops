package cn.jdevelops.spi.fixture;

import cn.jdevelops.spi.JoinSPI;

/**
 * 测试SPI的实现1
 *
 * @author tn
 * @version 1
 * @date 2022-04-01 11:11
 */
@JoinSPI(cover = true)
public class TestSPIImpl_4 implements TestSPI{
    @Override
    public String test() {
        return "test4";
    }
}
