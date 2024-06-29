package cn.tannn.jdevelops.frameworks.web.starter.entity.url;


import jdk.nashorn.internal.objects.annotations.Getter;
import jdk.nashorn.internal.objects.annotations.Setter;

/**
 * 接口请求类型和接口地址
 *
 * @author tnnn
 * @version V1.0
 * @date 2022-07-20 12:45
 */
public class MethodUrl {

    /**
     * get post put delete
     */
    private String method;

    /**
     * 接口地址
     */
    private String uri;

    public MethodUrl(String method, String uri) {
        this.method = method;
        this.uri = uri;
    }

    public MethodUrl() {
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    @Override
    public String toString() {
        return "MethodUrl{" +
                "method='" + method + '\'' +
                ", uri='" + uri + '\'' +
                '}';
    }
}
