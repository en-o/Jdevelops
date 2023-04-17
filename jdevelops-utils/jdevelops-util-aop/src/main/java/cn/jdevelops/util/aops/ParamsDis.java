package cn.jdevelops.util.aops;

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
public class ParamsDis {
    private static final Logger LOG = LoggerFactory.getLogger(ParamsDis.class);
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
        StringBuilder params = new StringBuilder();
        if (args.length > 0) {
            if (RequestMethod.POST.toString().equals(method)) {
                for (Object arg : args) {
                    if (arg instanceof MultipartFile) {
                        params.append(params.toString().concat("【文件参数】").concat(","));
                    } else if (arg instanceof MultipartFile[] && arg.getClass().isArray()) {
                        params.append(params.toString().concat("【多文件参数】").concat(","));
                    } else if (arg instanceof HttpServletRequest || arg instanceof HttpServletResponse) {
                        LOG.info("入参排除：HttpServletRequest，HttpServletResponse");
                    } else {
                        try {
                            params.append(params.toString().concat(JsonUtils.toJson(arg)).concat(","));
                        } catch (Exception e) {
                            LOG.warn("入参参数处理异常，就打印了");
                        }
                    }
                }
            } else if (RequestMethod.GET.toString().equals(method)) {
                params.append(decode(queryString));
            }
        }
        Logger log = LoggerFactory.getLogger("api");
        log.info("请求IP:{}", IpUtil.getPoxyIpEnhance(request));
        log.info("请求地址:{} {}", method, url);
        log.info("入参:{}", params);
        long ts = System.currentTimeMillis();
        Object result = pjp.proceed();
        log.info("出参 ... 耗时 {} 毫秒", System.currentTimeMillis() - ts);
        return result;
    }


    private static String decode(String url)  {
        return decode(url,  charset("UTF-8"),true);
    }


    public static String decode(String str, Charset charset, boolean isPlusToSpace) {
        return null == charset ? str : StringUtil.str(StringUtil.decode(StringUtil.bytes(str, charset), isPlusToSpace), charset);
    }


    public static Charset charset(String charsetName) throws UnsupportedCharsetException {
        return isBlank(charsetName) ? Charset.defaultCharset() : Charset.forName(charsetName);
    }

    private static boolean isBlank(CharSequence str) {
        int length;
        if (str != null && (length = str.length()) != 0) {
            for(int i = 0; i < length; ++i) {
                if (!StringUtil.isBlankChar(str.charAt(i))) {
                    return false;
                }
            }
            return true;
        } else {
            return true;
        }
    }





}
