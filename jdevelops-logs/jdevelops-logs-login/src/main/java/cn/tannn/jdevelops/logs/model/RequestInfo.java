package cn.tannn.jdevelops.logs.model;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.util.StringUtils;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.WebUtils;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 请求头
 *
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @version V1.0
 * @date 2025/5/6 11:27
 */
public class RequestInfo {
    /**
     * 请求基本信息
     */
    private String requestId;           // 请求唯一标识
    private String url;                 // 完整请求URL
    private String uri;                 // 请求URI
    private String queryString;         // 查询字符串
    private String method;              // 请求方法
    private String protocol;            // 协议版本
    private String contextPath;         // 上下文路径
    private String servletPath;         // Servlet路径

    /**
     * 客户端信息
     */
    private String remoteAddr;          // 远程地址
    private String remoteHost;          // 远程主机
    private int remotePort;             // 远程端口
    private String localAddr;           // 本地地址
    private int localPort;              // 本地端口
    private String serverName;          // 服务器名称

    /**
     * 请求头信息
     */
    private Map<String, String> headers = new HashMap<>();

    /**
     * 请求参数
     */
    private Map<String, String[]> parameters = new HashMap<>();

    /**
     * 请求体信息
     */
    private String requestBody;         // 请求体内容
    private String contentType;         // 内容类型
    private long contentLength;         // 内容长度
    private String characterEncoding;   // 字符编码

    /**
     * 认证信息
     */
    private String authType;            // 认证类型
    private String userPrincipal;       // 用户Principal

    /**
     * 会话信息
     */
    private String sessionId;           // 会话ID
    private boolean requestedSessionIdValid; // 会话ID是否有效

    /**
     * 时间信息
     */
    private String requestTime;         // 请求时间 (UTC)
    private long requestTimestamp;      // 请求时间戳

    /**
     * 其他信息
     */
    private Map<String, Object> attributes = new HashMap<>();  // 请求属性
    private List<Locale> acceptedLocales = new ArrayList<>(); // 接受的语言
    private Map<String, Object> extraInfo = new HashMap<>();  // 额外信息

    /**
     * 从HttpServletRequest创建RequestInfo对象
     *
     * @param request HTTP请求对象
     * @return RequestInfo实例
     */
    public static RequestInfo from(HttpServletRequest request) {
        if (request == null) {
            return null;
        }

        RequestInfo info = new RequestInfo();
        try {
            // 设置基本请求信息
            info.setRequestId(UUID.randomUUID().toString());
            info.setUrl(request.getRequestURL().toString());
            info.setUri(request.getRequestURI());
            info.setQueryString(request.getQueryString());
            info.setMethod(request.getMethod());
            info.setProtocol(request.getProtocol());
            info.setContextPath(request.getContextPath());
            info.setServletPath(request.getServletPath());

            // 设置客户端信息
            info.setRemoteAddr(getClientIp(request));
            info.setRemoteHost(request.getRemoteHost());
            info.setRemotePort(request.getRemotePort());
            info.setLocalAddr(request.getLocalAddr());
            info.setLocalPort(request.getLocalPort());
            info.setServerName(request.getServerName());

            // 设置请求头
            Map<String, String> headers = new HashMap<>();
            Enumeration<String> headerNames = request.getHeaderNames();
            while (headerNames.hasMoreElements()) {
                String name = headerNames.nextElement();
                headers.put(name, request.getHeader(name));
            }
            info.setHeaders(headers);

            // 设置请求参数
            info.setParameters(new HashMap<>(request.getParameterMap()));

            // 设置请求体
            String requestBody = getRequestBody(request);
            if (requestBody != null) {
                info.setRequestBody(requestBody);
            }

            // 设置内容信息
            info.setContentType(request.getContentType());
            info.setContentLength(request.getContentLengthLong());
            info.setCharacterEncoding(request.getCharacterEncoding());

            // 设置认证信息
            info.setAuthType(request.getAuthType());
            if (request.getUserPrincipal() != null) {
                info.setUserPrincipal(request.getUserPrincipal().getName());
            }

            // 设置会话信息
            if (request.getSession(false) != null) {
                info.setSessionId(request.getSession().getId());
            }
            info.setRequestedSessionIdValid(request.isRequestedSessionIdValid());

            // 设置时间信息
            LocalDateTime now = LocalDateTime.now(ZoneId.of("UTC"));
            info.setRequestTime(now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            info.setRequestTimestamp(System.currentTimeMillis());

            // 设置接受的语言
            List<Locale> locales = new ArrayList<>();
            Enumeration<Locale> localeEnum = request.getLocales();
            while (localeEnum.hasMoreElements()) {
                locales.add(localeEnum.nextElement());
            }
            info.setAcceptedLocales(locales);

            return info;
        } catch (Exception e) {
            throw new RuntimeException("构建RequestInfo失败", e);
        }
    }

    /**
     * 获取客户端真实IP地址
     */
    private static String getClientIp(HttpServletRequest request) {
        String[] headerNames = {
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

        for (String headerName : headerNames) {
            String ip = request.getHeader(headerName);
            if (StringUtils.hasText(ip) && !"unknown".equalsIgnoreCase(ip)) {
                // 多个代理的情况，第一个IP为客户端真实IP
                if (ip.contains(",")) {
                    ip = ip.split(",")[0];
                }
                return ip;
            }
        }

        return request.getRemoteAddr();
    }

    /**
     * 获取请求体内容
     */
    private static String getRequestBody(HttpServletRequest request) {
        ContentCachingRequestWrapper wrapper = WebUtils.getNativeRequest(request, ContentCachingRequestWrapper.class);
        if (wrapper != null) {
            byte[] buf = wrapper.getContentAsByteArray();
            if (buf.length > 0) {
                try {
                    return new String(buf, 0, buf.length, wrapper.getCharacterEncoding());
                } catch (Exception e) {
                    return new String(buf, 0, buf.length, StandardCharsets.UTF_8);
                }
            }
        }
        return null;
    }

    /**
     * 添加额外信息
     */
    public void addExtraInfo(String key, Object value) {
        this.extraInfo.put(key, value);
    }

    /**
     * 获取完整的请求URL（包含查询字符串）
     */
    public String getFullUrl() {
        StringBuilder fullUrl = new StringBuilder(this.url);
        if (StringUtils.hasText(this.queryString)) {
            fullUrl.append('?').append(this.queryString);
        }
        return fullUrl.toString();
    }


    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getQueryString() {
        return queryString;
    }

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getContextPath() {
        return contextPath;
    }

    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }

    public String getServletPath() {
        return servletPath;
    }

    public void setServletPath(String servletPath) {
        this.servletPath = servletPath;
    }

    public String getRemoteAddr() {
        return remoteAddr;
    }

    public void setRemoteAddr(String remoteAddr) {
        this.remoteAddr = remoteAddr;
    }

    public String getRemoteHost() {
        return remoteHost;
    }

    public void setRemoteHost(String remoteHost) {
        this.remoteHost = remoteHost;
    }

    public int getRemotePort() {
        return remotePort;
    }

    public void setRemotePort(int remotePort) {
        this.remotePort = remotePort;
    }

    public String getLocalAddr() {
        return localAddr;
    }

    public void setLocalAddr(String localAddr) {
        this.localAddr = localAddr;
    }

    public int getLocalPort() {
        return localPort;
    }

    public void setLocalPort(int localPort) {
        this.localPort = localPort;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public Map<String, String[]> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, String[]> parameters) {
        this.parameters = parameters;
    }

    public String getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public long getContentLength() {
        return contentLength;
    }

    public void setContentLength(long contentLength) {
        this.contentLength = contentLength;
    }

    public String getCharacterEncoding() {
        return characterEncoding;
    }

    public void setCharacterEncoding(String characterEncoding) {
        this.characterEncoding = characterEncoding;
    }

    public String getAuthType() {
        return authType;
    }

    public void setAuthType(String authType) {
        this.authType = authType;
    }

    public String getUserPrincipal() {
        return userPrincipal;
    }

    public void setUserPrincipal(String userPrincipal) {
        this.userPrincipal = userPrincipal;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public boolean isRequestedSessionIdValid() {
        return requestedSessionIdValid;
    }

    public void setRequestedSessionIdValid(boolean requestedSessionIdValid) {
        this.requestedSessionIdValid = requestedSessionIdValid;
    }

    public String getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(String requestTime) {
        this.requestTime = requestTime;
    }

    public long getRequestTimestamp() {
        return requestTimestamp;
    }

    public void setRequestTimestamp(long requestTimestamp) {
        this.requestTimestamp = requestTimestamp;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public List<Locale> getAcceptedLocales() {
        return acceptedLocales;
    }

    public void setAcceptedLocales(List<Locale> acceptedLocales) {
        this.acceptedLocales = acceptedLocales;
    }

    public Map<String, Object> getExtraInfo() {
        return extraInfo;
    }

    public void setExtraInfo(Map<String, Object> extraInfo) {
        this.extraInfo = extraInfo;
    }

    public RequestInfo() {
    }

    public RequestInfo(String requestId
            , String url
            , String uri
            , String queryString
            , String method
            , String protocol
            , String contextPath
            , String servletPath
            , String remoteAddr
            , String remoteHost
            , int remotePort
            , String localAddr
            , int localPort
            , String serverName
            , Map<String, String> headers
            , Map<String, String[]> parameters
            , String requestBody
            , String contentType
            , long contentLength
            , String characterEncoding
            , String authType
            , String userPrincipal
            , String sessionId
            , boolean requestedSessionIdValid
            , String requestTime
            , long requestTimestamp
            , Map<String, Object> attributes
            , List<Locale> acceptedLocales
            , Map<String, Object> extraInfo) {
        this.requestId = requestId;
        this.url = url;
        this.uri = uri;
        this.queryString = queryString;
        this.method = method;
        this.protocol = protocol;
        this.contextPath = contextPath;
        this.servletPath = servletPath;
        this.remoteAddr = remoteAddr;
        this.remoteHost = remoteHost;
        this.remotePort = remotePort;
        this.localAddr = localAddr;
        this.localPort = localPort;
        this.serverName = serverName;
        this.headers = headers;
        this.parameters = parameters;
        this.requestBody = requestBody;
        this.contentType = contentType;
        this.contentLength = contentLength;
        this.characterEncoding = characterEncoding;
        this.authType = authType;
        this.userPrincipal = userPrincipal;
        this.sessionId = sessionId;
        this.requestedSessionIdValid = requestedSessionIdValid;
        this.requestTime = requestTime;
        this.requestTimestamp = requestTimestamp;
        this.attributes = attributes;
        this.acceptedLocales = acceptedLocales;
        this.extraInfo = extraInfo;
    }
}
