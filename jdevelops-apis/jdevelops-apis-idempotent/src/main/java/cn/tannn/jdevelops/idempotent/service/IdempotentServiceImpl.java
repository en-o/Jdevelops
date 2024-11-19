package cn.tannn.jdevelops.idempotent.service;


import cn.tannn.jdevelops.idempotent.annotation.ApiIdempotent;
import cn.tannn.jdevelops.idempotent.config.IdempotentConfig;
import cn.tannn.jdevelops.idempotent.exception.IdempotentException;
import cn.tannn.jdevelops.idempotent.util.ParamUtil;
import cn.tannn.jdevelops.idempotent.util.ParseSha256;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author tnnn
 */
@Service
public class IdempotentServiceImpl implements IdempotentService {

    private static final Logger LOG = LoggerFactory.getLogger(IdempotentServiceImpl.class);

    /**
     * redis中 登录用户的文件夹名 幂等
     */
    static final String REDIS_IDEMPOTENT_FOLDER = "idempotent";

    static final String UNKNOWN = "unKnown";

    static final String LOCALHOST = "localhost";

    static final String COMMA = ",";

    @Resource
    private IdempotentConfig idempotentConfig;


    /**
     * reids
     */
    @Resource
    private RedisTemplate<String, String> redisTemplate;


    /**
     * @param paramsHeader          参数
     * @param requestUri            请求地址
     * @param idempotentRedisFolder redis文件夹
     * @param methodAnnotation      接口上的注解
     */
    public void createApiCall(String paramsHeader, String requestUri, String idempotentRedisFolder, ApiIdempotent methodAnnotation) {
        redisTemplate.boundHashOps(idempotentRedisFolder).put(requestUri,
                paramsHeader);
        // 默认全局
        long expireTime = idempotentConfig.getExpireTime();
        if (methodAnnotation.expireTime() > -1) {
            // 当注解上存在过期时间且大于-1是，则以注解上的时间为准
            expireTime = methodAnnotation.expireTime();
        }
        // 设置过期时间（秒
        redisTemplate.expire(idempotentRedisFolder, expireTime, TimeUnit.MILLISECONDS);
    }

    @Override
    public boolean checkApiRedo(HttpServletRequest request, HttpServletResponse response, ApiIdempotent methodAnnotation) {
        // 加密让数据变短
        String paramsHeader = "";
        if (methodAnnotation.paramsHeader()) {
            paramsHeader = ParamUtil.getRequestParam(request);
            if (idempotentConfig.isParameterEncryption()) {
                paramsHeader = ParseSha256.getSha256StrJava(paramsHeader);
            }
        }
        String requestUri = request.getRequestURI();
        // 存在token就用token做文件名，不存在就用IP
        String idempotentRedisFolder = getRedisFolder(request, idempotentConfig.getGroupStr(), requestUri);
        String redisValue = (String) redisTemplate.boundHashOps(idempotentRedisFolder).get(requestUri);
        // 值相等则进行保存
        if (Objects.isNull(redisValue) || !redisValue.equals(paramsHeader)) {
            createApiCall(paramsHeader, requestUri, idempotentRedisFolder, methodAnnotation);
            LOG.info("当前接口在redis中无记录，此处进行新增记录");
            return true;
        } else {
            if (methodAnnotation.responseStatus()) {
                response.setStatus(IdempotentException.IDEMPOTENT_CODE);
            }
            throw IdempotentException.specialMessage(methodAnnotation.message());
        }

    }


    /**
     * redis简单存储建立文件夹
     *
     * @param request  request
     * @param groupStr getHeader(groupStr) getParameter(groupStr)
     * @param key      key
     * @return folderName:key
     */
    private static String getRedisFolder(HttpServletRequest request, String groupStr, String key) {
        return REDIS_IDEMPOTENT_FOLDER + ":" + catalog(request, groupStr) + ":" + key;
    }


    /**
     * 获取redis存储的目录 （默认用token，其次用IP）
     *
     * @param request  request
     * @param groupStr getHeader(groupStr) getParameter(groupStr)
     * @return String
     */
    private static String catalog(HttpServletRequest request, String groupStr) {
        String token = request.getHeader(groupStr);
        if (Objects.isNull(token) || token.isEmpty()) {
            token = request.getParameter(groupStr);
        }
        // 给token md5下
        if (Objects.nonNull(token) && !token.isEmpty()) {
            return ParseSha256.getSha256StrJava(token);
        }
        // token 拿不到就用IP
        return getPoxyIpEnhance(request);
    }


    public static final String[] IP_HEADER_CANDIDATES = {
            "X-Forwarded-For",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_X_FORWARDED_FOR",
            "HTTP_X_FORWARDED",
            "HTTP_X_CLUSTER_CLIENT_IP",
            "HTTP_CLIENT_IP",
            "HTTP_FORWARDED_FOR",
            "HTTP_FORWARDED",
            "HTTP_VIA",
            "REMOTE_ADDR"
    };

    /***
     * 增强版 - 普通使用getPoxyIp即可满足需求
     * 获取有网关是 的真正客户端IP 测试过nginx可以获取
     * @param request request
     * @return ip
     */
    public static String getPoxyIpEnhance(HttpServletRequest request) {
        for (String header : IP_HEADER_CANDIDATES) {
            String ip = request.getHeader(header);
            if (ip != null && !ip.isEmpty() && !UNKNOWN.equalsIgnoreCase(ip)) {
                return extractFirstIp(ip);
            }
        }
        // 如果无法通过 header 获取，则使用 getRemoteAddr
        String ip = request.getRemoteAddr();
        return LOCALHOST.equalsIgnoreCase(ip) || "127.0.0.1".equals(ip) || "0:0:0:0:0:0:0:1".equals(ip)
                ? getLocalHostIp()
                : ip;
    }

    /**
     * 截取IP
     * @param ip 逗号隔开的ip
     * @return 第一位IP
     */
    private static String extractFirstIp(String ip) {
        return ip.contains(COMMA) ? ip.split(COMMA)[0].trim() : ip;
    }

    /**
     * @return 获取本地 IP
     */
    public static String getLocalHostIp() {
        try {
            InetAddress inet = InetAddress.getLocalHost();
            return inet.getHostAddress();
        } catch (UnknownHostException e) {
            LOG.error("获取本地 IP 失败", e);
            return null;
        }
    }

}
