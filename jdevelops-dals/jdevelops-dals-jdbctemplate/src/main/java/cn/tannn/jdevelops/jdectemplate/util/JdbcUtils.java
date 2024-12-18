package cn.tannn.jdevelops.jdectemplate.util;

import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SingleColumnRowMapper;

import java.lang.reflect.Array;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 一些工具、
 *
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @date 2024/6/25 上午10:51
 */
public class JdbcUtils {

    static Pattern pattern = Pattern.compile("FROM\\s+(\\w+)");

    /**
     * 获取sql中的表名
     * @param sql sql
     * @return 表名
     */
    public static String tableName(String sql){
        Matcher matcher = pattern.matcher(sql.toUpperCase());
        if(matcher.find()) {
            return matcher.group(1);
        }
        throw new RuntimeException("Invalid sql : "+ sql);
    }


    /**
     * 判断是不是基础类型 （String, Integer ）
     *
     * @param resultType 类型
     * @return boolean
     */
    public static boolean isBasicType(String resultType) {
        return resultType.equals(String.class.getName())
                || resultType.equals(Integer.class.getName());
    }


    /**
     * 判断是不是基础类型 （String, Integer ）
     *
     * @param resultType 类型
     * @return boolean
     */
    public static boolean isBasicType( Class<?> resultType) {
        return resultType.isAssignableFrom(String.class)
                || resultType.isAssignableFrom(Integer.class);
    }


    /**
     * 返回 sql 的 from
     *
     * @param sql sql
     * @return from xx [注意此项包含 from]
     */
    public static String extractFromClause(String sql) {
        // 查找 FROM 关键字的位置
        int fromIndex = sql.toUpperCase().indexOf("FROM");

        if (fromIndex != -1) {
            // 截取 FROM 关键字之后的所有内容
            return sql.substring(fromIndex);
        } else {
            return null; // 如果没有找到 FROM 关键字，则返回 null
        }
    }


    /**
     * 验证类型 返回对应的 RowMapper
     * @param resultActualType 类型 class.getName
     * @return RowMapper
     * @throws ClassNotFoundException ClassNotFoundException
     */
    public static RowMapper<?> rowMapper(String resultActualType) throws ClassNotFoundException {
        if (resultActualType.equals(Integer.class.getName())) {
            return new SingleColumnRowMapper<>(Integer.class);
        } else {
            return new DataClassRowMapper<>(Class.forName(resultActualType));
        }
    }


    /**
     * 验证类型 返回对应的 RowMapper
     * @param resultActualType 类型 clas
     * @return RowMapper
     */
    public static RowMapper<?> rowMapper( Class<?> resultActualType){
        if (resultActualType.isAssignableFrom(Integer.class)) {
            return new SingleColumnRowMapper<>(Integer.class);
        } else {
            return new DataClassRowMapper<>(resultActualType);
        }
    }

    /**
     * 验证类型 返回对应的 RowMapper
     * @param resultActualType 类型 class
     * @param <T> 泛型类型
     * @return RowMapper<T>
     */
    public static <T> RowMapper<T> rowMapper2(Class<T> resultActualType) {
        if (resultActualType.isAssignableFrom(Integer.class)) {
            // 强制转换为泛型类型 T
            return (RowMapper<T>) new SingleColumnRowMapper<>(Integer.class);
        } else {
            return new DataClassRowMapper<>(resultActualType);
        }
    }




    /**
     * 获取并移除数组中的第一个元素。
     *
     * @param array 要操作的数组
     * @return 第一个元素，如果数组为空则返回null
     */
    public static <T> T removeFirstElement(T[] array) {
        if (array == null || array.length == 0) {
            return null;
        }
        T firstElement = array[0];
        T[] newArray = (T[]) new Object[array.length - 1];
        System.arraycopy(array, 1, newArray, 0, array.length - 1);
        return firstElement;
    }

    /**
     * 将元素插入到数组的第一个位置。
     *
     * @param originalArray 原始数组
     * @param newElement    要插入的新元素
     * @return 包含新元素的新数组
     */
    public static <T> T[] insertFirstElement(T[] originalArray, T newElement) {
        if (originalArray == null) {
            throw new IllegalArgumentException("Original array must not be null");
        }
        int length = originalArray.length;
        T[] newArray = (T[]) Array.newInstance(originalArray.getClass().getComponentType(), length + 1);
        System.arraycopy(originalArray, 0, newArray, 1, length);
        newArray[0] = newElement;
        return newArray;
    }
}
