package cn.jdevelops.apilog.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;

/**
 * 切面参数处理
 * @author tn
 * @version 1
 * @date 2020/11/25 11:48
 */
@Slf4j
public class ParamsDis {
    /**
     * 调用日志打印
     *
     * @param request request
     * @param pjp pjp
     * @return Object
     */
    public static Object aopDis(HttpServletRequest request, ProceedingJoinPoint pjp) throws Throwable {
        String url = request.getRequestURL().toString();
        String method = request.getMethod();
        String queryString = request.getQueryString();
        Object[] args = pjp.getArgs();
        String params = "";
        if (args.length > 0) {
            if (RequestMethod.POST.toString().equals(method)) {
                for (Object arg : args) {
                    if (arg instanceof MultipartFile) {
                        params = params.concat("【文件参数】").concat(",");
                    } else if (arg instanceof MultipartFile[] && arg.getClass().isArray()) {
                        params = params.concat("【多文件参数】").concat(",");
                    } else if (arg instanceof HttpServletRequest || arg instanceof HttpServletResponse) {
                        params = "";
                    } else {
                        try {
                            ObjectMapper objectMapper = new ObjectMapper();
                            params = params.concat(objectMapper.writeValueAsString(arg)).concat(",");
                        } catch (Exception e) {
                            log.warn("入参参数处理异常，就打印了");
                        }
                    }
                }
            } else if (RequestMethod.GET.toString().equals(method)) {
                params = decode(queryString);
            }
        }
        Logger log = LoggerFactory.getLogger(pjp.getTarget().getClass());
        log.info("请求IP:{}", IpUtil.getPoxyIpEnhance(request));
        log.info("请求地址:{} {}", method, url);
        log.info("入参:{}", params);
        long ts = System.currentTimeMillis();
        Object result = pjp.proceed();
        log.info("出参 ... 耗时 {} 毫秒", System.currentTimeMillis() - ts);
        return result;
    }




    private static String decode(String url)  {
        return decode(url, "UTF-8");
    }

    private static String decode(String content, String charset)  {
        return decode(content, isEmpty(charset) ? null : String.valueOf(charset(charset)));
    }

    public static Charset charset(String charsetName) throws UnsupportedCharsetException {
        return isBlank(charsetName) ? Charset.defaultCharset() : Charset.forName(charsetName);
    }

    private static boolean isBlank(CharSequence str) {
        int length;
        if (str != null && (length = str.length()) != 0) {
            for(int i = 0; i < length; ++i) {
                if (!isBlankChar(str.charAt(i))) {
                    return false;
                }
            }
            return true;
        } else {
            return true;
        }
    }

    private static boolean isBlankChar(char c) {
        return isBlankChar((int)c);
    }

    private static boolean isBlankChar(int c) {
        return Character.isWhitespace(c) || Character.isSpaceChar(c) || c == 65279 || c == 8234 || c == 0;
    }

    private static boolean isEmpty(CharSequence str) {
        return str == null || str.length() == 0;
    }
}
