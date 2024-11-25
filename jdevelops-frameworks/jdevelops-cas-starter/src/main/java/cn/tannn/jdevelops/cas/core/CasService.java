package cn.tannn.jdevelops.cas.core;


import cn.tannn.jdevelops.cas.config.CasConfig;
import com.google.gson.JsonObject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * cas服务
 *
 * @author tnnn
 * @version V1.0
 * @date 2022-10-10 13:35
 */
public interface CasService {

    /**
     * cas 票据验证 , Json 具体值根据对接方提供的来，下面只是参考
     * <ol>
     *    <li> 验票成功返回：{errcode:0,
     *    <li> errmsg:”OK”,
     *    <li> errtype:”验证通过”,
     *    <li> user_no:”{学工号}”,
     *    <li> user_name:”{姓名}”,
     *    <li> user_group:”{帐号分组}”}，
     *    <li> user_no表示帐号
     *    <li> user_name为姓名，
     *    <li> 当验票失败时返回：{errcode:{错误代码},
     *    <li> errmsg:”{错误消息}”,
     *    <li> errtype:”{错误类型}”}，
     *    <li> errcode为大于0的整数，如400，500等，errtype及errmsg返回失败的类型和消息说明。根据返回结果设置系统本地登录会话，登录完成；
     * </ol>
     *
     * @param ticket cas令牌
     * @return cas返回的数据JsonObject
     */
    JsonObject verifyTicket(String ticket) throws IOException;


    /**
     * cas 重定向地址
     *
     * @return 可以直接（response.sendRedirect(loginUrl);）
     */
    String redirectCasAddress() throws IOException;


    CasConfig getCasConfig();

    /**
     * 退出
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     * @return 不为空就是：重定向到 CAS 的登出接口 ， 空就是不重定向，前端根据这个处理
     */
    String loginOut(HttpServletRequest request, HttpServletResponse response);
}

