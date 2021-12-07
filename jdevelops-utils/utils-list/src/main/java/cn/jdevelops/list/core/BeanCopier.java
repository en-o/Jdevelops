package cn.jdevelops.list.core;

import org.springframework.beans.BeanUtils;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 实体工具
 * @author tn
 * @version 1
 * @date 2020/6/29 9:08
 */
public class BeanCopier {

    protected static void copy(Object source, Object target) {
        BeanUtils.copyProperties(source, target);
        Field[] sourceDeclaredFields = getAllField(source.getClass());
        Field[] targetDeclaredFields = getAllField(target.getClass());
        int var5 = sourceDeclaredFields.length;

        for(int var6 = 0; var6 < var5; ++var6) {
            Field sourceField = sourceDeclaredFields[var6];
            String simpleName = sourceField.getType().getSimpleName();
            if (simpleName.equals(Date.class.getSimpleName()) || simpleName.equals(LocalDateTime.class.getSimpleName())) {
                Field targetField = getSameNameField(targetDeclaredFields, sourceField);
                if (targetField != null && targetField.getType().getSimpleName().equals(String.class.getSimpleName())) {
                    try {
                        sourceField.setAccessible(true);
                        targetField.setAccessible(true);
                        if(sourceField.get(source)==null){
                            targetField.set(target, null);
                        }else if (simpleName.equals(Date.class.getSimpleName())) {
                            // 创建时间
                            Date date =(Date)sourceField.get(source);
                            // 将时间转为 秒级时间戳
                            long second = date.toInstant().atOffset(ZoneOffset.ofHours(8)).toEpochSecond();
                            LocalDateTime localDateTime = LocalDateTime.ofEpochSecond(second, 0, ZoneOffset.ofHours(8));
                            targetField.set(target, localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                        } else if (simpleName.equals(LocalDateTime.class.getSimpleName())) {
                            LocalDateTime localDateTime = (LocalDateTime)sourceField.get(source);
                            targetField.set(target, localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                        }else if (simpleName.equals(Timestamp.class.getSimpleName())) {
                            Timestamp time = (Timestamp)sourceField.get(source);
                            LocalDateTime localDateTime = time.toLocalDateTime();
                            targetField.set(target, localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                        }
                    } catch (IllegalAccessException var11) {
                        var11.printStackTrace();
                    }
                }
            }
        }

    }

    public static <T> T copy(Object source, Class<T> clazz) {
        try {
            T t = clazz.newInstance();
            copy(source, t);
            return t;
        } catch (IllegalAccessException | InstantiationException var3) {
            var3.printStackTrace();
            return null;
        }
    }

    public static <T> List<T> listCopy(List<?> sources, Class<T> clazz) {
        return (sources == null ? new ArrayList() : sources.stream().map((source) ->
                copy(source, clazz)).collect(Collectors.toList()));
    }

    private static Field[] getAllField(Class clazz) {
        Field[] array;
        for(array = null; clazz != Object.class; clazz = clazz.getSuperclass()) {
            Field[] field = clazz.getDeclaredFields();
            array = addAll(array, field);
        }

        return array;
    }

    private static Field getSameNameField(Field[] fields, Field targetField) {
        Field[] var2 = fields;
        int var3 = fields.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            Field field = var2[var4];
            if (field.getName().equals(targetField.getName())) {
                return field;
            }
        }

        return null;
    }


    protected static <T> T[] addAll(final T[] array1, final T... array2) {
        if (array1 == null) {
            return clone(array2);
        } else if (array2 == null) {
            return clone(array1);
        }
        final Class<?> type1 = array1.getClass().getComponentType();
        @SuppressWarnings("unchecked") // OK, because array is of type T
        final T[] joinedArray = (T[]) Array.newInstance(type1, array1.length + array2.length);
        System.arraycopy(array1, 0, joinedArray, 0, array1.length);
        try {
            System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
        } catch (final ArrayStoreException ase) {
            // Check if problem was due to incompatible types
            /*
             * We do this here, rather than before the copy because:
             * - it would be a wasted check most of the time
             * - safer, in case check turns out to be too strict
             */
            final Class<?> type2 = array2.getClass().getComponentType();
            if (!type1.isAssignableFrom(type2)) {
                throw new IllegalArgumentException("Cannot store " + type2.getName() + " in an array of "
                        + type1.getName(), ase);
            }
            throw ase; // No, so rethrow original
        }
        return joinedArray;
    }

    // Clone
    //-----------------------------------------------------------------------
    /**
     * <p>Shallow clones an array returning a typecast result and handling
     * {@code null}.
     *
     * <p>The objects in the array are not cloned, thus there is no special
     * handling for multi-dimensional arrays.
     *
     * <p>This method returns {@code null} for a {@code null} input array.
     *
     * @param <T> the component type of the array
     * @param array  the array to shallow clone, may be {@code null}
     * @return the cloned array, {@code null} if {@code null} input
     */
    protected static <T> T[] clone(final T[] array) {
        if (array == null) {
            return null;
        }
        return array.clone();
    }
}
