package cn.jdevelops.sboot.web.entity.http;

/**
 * Response 包装类
 *
 * @author tnnn
 * @version V1.0
 * @date 2023-02-11 12:58
 */
public interface JdevelopsResponse {

    /**
     * 获取底层源对象
     *
     * @return see note
     */
    Object getSource();


    /**
     * 设置响应状态码
     *
     * @param status 响应状态码
     * @return 对象自身
     */
    JdevelopsResponse setStatus(int status);

    /**
     * 在响应头里写入一个值
     *
     * @param name  名字
     * @param value 值
     * @return 对象自身
     */
    JdevelopsResponse setHeader(String name, String value);

    /**
     * 在响应头里添加一个值
     *
     * @param name  名字
     * @param value 值
     * @return 对象自身
     */
    JdevelopsResponse addHeader(String name, String value);

    /**
     * 在响应头写入Server服务器名称
     *
     * @param value 服务器名称
     * @return 对象自身
     */
    default JdevelopsResponse setServer(String value) {
        return this.setHeader("Server", value);
    }

    /**
     * 重定向
     *
     * @param url 重定向地址
     * @return 任意值
     */
    Object redirect(String url);

}
