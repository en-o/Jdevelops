//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package cn.tannn.jdevelops.utils.core.number;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author t
 */
public class NumberUtil {

    private static final Logger LOG = LoggerFactory.getLogger(NumberUtil.class);


    /**
     * 除法
     * @param v1 v1
     * @param v2 v2
     * @return BigDecimal
     */
    public static BigDecimal div(Number v1, Number v2) {
        return div(v1, v2, 10);
    }


    /**
     * 乘法
     * @param v1 v1
     * @param v2 v2
     * @return BigDecimal
     */
    public static BigDecimal mul(Number v1, Number v2) {
       try {
           return mul(v1, v2);
       }catch (Exception e){
           LOG.error("乘法失败", e);
       }
       return new BigDecimal(0);
    }


    /**
     * 设置number四舍五入
     * @param number number
     * @param scale 保留小数
     * @return BigDecimal
     */
    public static BigDecimal round(BigDecimal number, int scale) {
        return round(number, scale, RoundingMode.HALF_UP);
    }

    /**
     * 除法 - 四舍五入
     * @param v1 v1
     * @param v2 v2
     * @param scale 保留小数
     * @return BigDecimal
     */
    public static BigDecimal div(Number v1, Number v2, int scale) {
        return div(v1, v2, scale, RoundingMode.HALF_UP);
    }

    /**
     * 除法 - 四舍五入
     * @param v1 v1
     * @param v2 v2
     * @param scale 保留小数
     * @param roundingMode  自定义 舍入模式
     * @return BigDecimal
     */
    public static BigDecimal div(Number v1, Number v2, int scale, RoundingMode roundingMode) {
        return div(v1.toString(), v2.toString(), scale, roundingMode);
    }


    /**
     * 除法 - 四舍五入
     * @param v1 字符串类型的数字
     * @param v2 字符串类型的数字
     * @param scale 保留小数
     * @param roundingMode  自定义 舍入模式
     * @return BigDecimal
     */
    public static BigDecimal div(String v1, String v2, int scale, RoundingMode roundingMode) {
        return div(new BigDecimal(v1), new BigDecimal(v2), scale, roundingMode);
    }


    /**
     * 设置number舍入模式
     * @param number number
     * @param scale 保留小数
     * @param roundingMode 自定义舍入模式
     * @return BigDecimal
     */
    public static BigDecimal round(BigDecimal number, int scale, RoundingMode roundingMode) {
        if (null == number) {
            number = BigDecimal.ZERO;
        }

        if (scale < 0) {
            scale = 0;
        }

        if (null == roundingMode) {
            roundingMode = RoundingMode.HALF_UP;
        }

        return number.setScale(scale, roundingMode);
    }

}
