package cn.tannn.jdevelops.utils.jwt.util;

import java.util.concurrent.TimeUnit;

/**
 * jwt
 *
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @version V1.0
 * @date 2025/4/24 10:34
 */
public class JwtUtil {


    /**
     * 将 identity 从 subject 中删除
     *
     * @return  原生Subject
     */
    public static String parsingSubject(String subject) {
        if (subject == null || subject.isEmpty()) {
            return subject;
        } else {
            String[] split = subject.split("__");
            if (split.length < 2) {
                return split[0];
            }
            return split[1];
        }
    }

    /**
     * 将配置的过期时间转换为分钟
     *
     * @param time 时间值
     * @param unit 时间单位
     * @return 转换后的分钟数
     */
    public static float convertToMinutes(long time, TimeUnit unit) {
        return switch (unit) {
            case SECONDS -> time / 60.0f;
            case MINUTES -> time;
            case HOURS -> time * 60;
            case DAYS -> time * 60 * 24;
            default ->
                // 默认按小时处理
                    time * 60;
        };
    }
}
