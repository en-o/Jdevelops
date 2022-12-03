package cn.jdevelops.idempotent.service;


import cn.jdevelops.idempotent.config.IdempotentConfig;
import cn.jdevelops.idempotent.exception.IdempotentException;
import cn.jdevelops.idempotent.util.ParseSha256;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static cn.jdevelops.idempotent.util.ParamUtil.getRequestParam;

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
     */
    public void createApiCall(String paramsHeader, String requestUri, String idempotentRedisFolder) {
        redisTemplate.boundHashOps(idempotentRedisFolder).put(requestUri,
                paramsHeader);
        // 设置过期时间（秒
        redisTemplate.expire(idempotentRedisFolder, idempotentConfig.getExpireTime(), TimeUnit.MILLISECONDS);
    }

    @Override
    public boolean checkApiRedo(HttpServletRequest request) {
        // 加密让数据变短
        String paramsHeader = getRequestParam(request);
        if(idempotentConfig.isParameterEncryption()){
            paramsHeader = ParseSha256.getSha256StrJava(paramsHeader);
        }
        String requestUri = request.getRequestURI();
        String idempotentRedisFolder = getRedisFolder(request, idempotentConfig, requestUri);
        String redisValue = (String) redisTemplate.boundHashOps(idempotentRedisFolder).get(requestUri);
        // 值相等则进行保存
        if (Objects.isNull(redisValue) || !redisValue.equals(paramsHeader)) {
            createApiCall(paramsHeader, requestUri, idempotentRedisFolder);
            LOG.info("当前接口在redis中无记录，此处进行新增记录");
            return true;
        } else {
            throw new IdempotentException("短时间内请勿重复调用！");
        }

    }


    /**
     * redis简单存储建立文件夹
     *
     * @param request          request
     * @param idempotentConfig 参数配置
     * @param key              key
     * @return folderName:key
     */
    private static String getRedisFolder(HttpServletRequest request, IdempotentConfig idempotentConfig, String key) {
        return REDIS_IDEMPOTENT_FOLDER + ":" + getToken(request, idempotentConfig.getGroupStr()) + ":" + key;
    }


    /**
     * 获取token 如果token为空就获取IP
     *
     * @param request  request
     * @param groupStr getHeader(groupStr) getParameter(groupStr)
     * @return String
     */
    private static String getToken(HttpServletRequest request, String groupStr) {
        String token = request.getHeader(groupStr);
        if (Objects.isNull(token) || "".equals(token)) {
            token = request.getParameter(groupStr);
        }
        if (Objects.nonNull(token) && !"".equals(token)) {
            return ParseSha256.getSha256StrJava(token);
        }
        return getPoxyIpEnhance(request);
    }


    /***
     * 增强版 - 普通使用getPoxyIp即可满足需求
     * 获取有网关是 的真正客户端IP 测试过nginx可以获取
     * @param request request
     * @return ip
     */
    private static String getPoxyIpEnhance(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_CLUSTER_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_FORWARDED");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_VIA");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("REMOTE_ADDR");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        // 本机访问
        if (LOCALHOST.equalsIgnoreCase(ip)
                || "127.0.0.1".equalsIgnoreCase(ip)
                || "0:0:0:0:0:0:0:1".equalsIgnoreCase(ip)) {
            // 根据网卡取本机配置的IP
            InetAddress inet;
            try {
                inet = InetAddress.getLocalHost();
                ip = inet.getHostAddress();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }
        // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if (null != ip && ip.indexOf(COMMA) > 15) {
            ip = ip.substring(0, ip.indexOf(COMMA));
        }
        return ip;
    }

}
