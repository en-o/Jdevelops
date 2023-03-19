package cn.jdevelops.map.core.bean;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

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
    private static final Logger LOG = LoggerFactory.getLogger(BeanCopier.class);

    public static void copy(Object source, Object target) {
        BeanUtils.copyProperties(source, target);
        Field[] sourceDeclaredFields = getAllField(source.getClass());
        Field[] targetDeclaredFields = getAllField(target.getClass());
        int var5 = sourceDeclaredFields.length;

        for(int var6 = 0; var6 < var5; ++var6) {
            Field sourceField = sourceDeclaredFields[var6];
            String simpleName = sourceField.getType().getSimpleName();
            if (simpleName.equals(Date.class.getSimpleName()) || simpleName.equals(LocalDateTime.class.getSimpleName())) {
                Field targetField = getSameNameField(targetDeclaredFields, sourceField);
                if (targetField != null && targetField.getType().isAssignableFrom(String.class)) {
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
                            // 若 Long 设置 毫秒
                            if(targetField.getType().getSimpleName().equals(Long.class.getSimpleName())){
                                targetField.set(target, localDateTime.toInstant(ZoneOffset.of("+8")).toEpochMilli());
                            }else {
                                targetField.set(target, localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                            }
                        }else if (simpleName.equals(Timestamp.class.getSimpleName())) {
                            Timestamp time = (Timestamp)sourceField.get(source);
                            LocalDateTime localDateTime = time.toLocalDateTime();
                            targetField.set(target, localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                        }
                    } catch (IllegalAccessException var11) {
                        LOG.error(var11.getMessage(),var11);
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
            LOG.error(var3.getMessage(),var3);
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
            array =  ArrayUtils.addAll(array, field);
        }

        return array;
    }

    private static Field getSameNameField(Field[] fields, Field targetField) {
        int var3 = fields.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            Field field = fields[var4];
            if (field.getName().equals(targetField.getName())) {
                return field;
            }
        }

        return null;
    }
}
