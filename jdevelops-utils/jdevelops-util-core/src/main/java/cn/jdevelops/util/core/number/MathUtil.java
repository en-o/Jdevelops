package cn.jdevelops.util.core.number;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 数学
 *
 * @author <a href="https://tannn.cn/">tan</a>
 * @date 2023/7/31 13:45
 */
public class MathUtil {

    private static final Logger logger = LoggerFactory.getLogger(MathUtil.class);

    /**
     * 等差数列中的通项公式: result = n + (1 * (n - 1))
     * @param n n
     * @return int 负数不做计算返回0，报错也返回0
     */
    public static int calculate(int n) {
        try {
            int result =  n + (1 * (n - 1));
            return Math.max(result, 0);
        }catch (Exception e){
            logger.error("等差数列中的通项公式计算失败直接返回0值",e);
        }
        return 0;
    }
}
