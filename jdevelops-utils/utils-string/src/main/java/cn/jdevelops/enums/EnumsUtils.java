package cn.jdevelops.enums;

import java.security.SecureRandom;

/**
 * 枚举工具类
 * @author tn
 * @version 1
 * @date 2022-04-15 12:48
 */
public class EnumsUtils {

    /**
     * 生成随机种子
     */
    private static final SecureRandom RAND = new SecureRandom();

    /**
     * 随机获取一个枚举值
     * <pre>
     *    public static void main(String[] args) {
     *         for (int i = 0; i < 7; i++){
     *             System.out.println("i="+i+"---"+ EnumsUtils.random(BookStateEnums.class) + " ");
     *         }
     *     }
     * </pre>
     * @param ec 枚举类 Class
     * @return 枚举类
     *
     */
    public static <T extends Enum<T>> T random(Class<T> ec) {
        return random(ec.getEnumConstants());
    }

    /**
     * 随机获取一个枚举值
     *
     * @param values 枚举类 getEnumConstants
     * @return 枚举类
     */
    public static <T> T random(T[] values) {
        return values[RAND.nextInt(values.length)];
    }


}
