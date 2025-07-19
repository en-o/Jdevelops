package cn.tannn.jdevelops.utils.jwt.util;

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

}
