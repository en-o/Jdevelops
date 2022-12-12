package cn.jdevelops.file.util;

import java.util.Objects;

/**
 * @author tnnn
 * @version V1.0
 * @date 2022-12-12 13:00
 */
public class StrUtil {

    /**
     * 判非空
     * @param str  str
     * @return true false
     */
    public static boolean notBlank(String str){
        return Objects.nonNull(str) && str.length() > 0;
    }

    /**
     * 判非空
     * @param str  str
     * @return true false
     */
    public static boolean isBlank(String str){
        return Objects.isNull(str) || str.length() == 0;
    }

}
