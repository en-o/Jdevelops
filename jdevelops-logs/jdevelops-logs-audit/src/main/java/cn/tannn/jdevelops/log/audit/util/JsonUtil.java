package cn.tannn.jdevelops.log.audit.util;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.filter.SimplePropertyPreFilter;

import java.util.Arrays;
import java.util.Collection;

/**
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @version V1.0
 * @date 2025/6/26 12:07
 */
public class JsonUtil {

    /**
     * 将对象转换为JSONObject的通用方法
     *
     * @param obj 待转换的对象
     * @return JSONObject
     */
    public static JSONObject convertToJsonObject(Object obj) {
        if (obj == null) {
            return new JSONObject();
        }

        if (obj instanceof Collection<?> || obj.getClass().isArray()) {
            // 如果是集合或数组类型,将其包装在一个JSONObject中
            JSONObject wrapper = new JSONObject();
            wrapper.put("data", obj);
            return wrapper;
        } else {
            // 先转换为JSON字符串，再解析为JSONObject，避免直接类型转换异常
            String jsonString = JSON.toJSONString(obj);
            Object parsed = JSON.parse(jsonString);

            if (parsed instanceof JSONObject) {
                return (JSONObject) parsed;
            } else {
                // 如果解析结果不是JSONObject（比如是JSONArray），则包装它
                JSONObject wrapper = new JSONObject();
                wrapper.put("data", parsed);
                return wrapper;
            }
        }
    }

    /**
     * 带过滤字段的对象转换为JSONObject的方法
     *
     * @param obj         待转换的对象
     * @param filterField 需要过滤的字段
     * @return JSONObject
     */
    public static JSONObject convertToJsonObjectWithFilter(Object obj, String... filterField) {
        if (obj == null) {
            return new JSONObject();
        }

        SimplePropertyPreFilter filter = new SimplePropertyPreFilter();
        if (filterField != null && filterField.length > 0) {
            filter.getExcludes().addAll(Arrays.asList(filterField));
        }

        if (obj instanceof Collection<?> || obj.getClass().isArray()) {
            // 对于集合或数组，先应用过滤器，再包装
            String filteredJson = JSON.toJSONString(obj, filter);
            Object parsed = JSON.parse(filteredJson);

            JSONObject wrapper = new JSONObject();
            wrapper.put("data", parsed);
            return wrapper;
        } else {
            String filteredJson = JSON.toJSONString(obj, filter);
            Object parsed = JSON.parse(filteredJson);

            if (parsed instanceof JSONObject) {
                return (JSONObject) parsed;
            } else {
                // 如果解析结果不是JSONObject，则包装它
                JSONObject wrapper = new JSONObject();
                wrapper.put("data", parsed);
                return wrapper;
            }
        }
    }
}
