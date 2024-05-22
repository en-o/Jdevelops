package cn.tannn.jdevelops.knife4j.core.util;

import java.util.concurrent.ThreadLocalRandom;

/**
 * RandomUtil
 *
 * @author cn.hutool.core.util
 * @version V1.0
 * @date 2023-03-12 19:00
 */
public class RandomUtil {

    public static int randomInt(int min, int max) {
        return getRandom().nextInt(min, max);
    }

    public static ThreadLocalRandom getRandom() {
        return ThreadLocalRandom.current();
    }
}
