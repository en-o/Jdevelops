package cn.tannn.jdevelops.cas.core;

import cn.tannn.jdevelops.cas.config.CasConfig;
import cn.tannn.jdevelops.cas.exception.CasException;
import cn.tannn.jdevelops.cas.util.GsonUtils;
import cn.tannn.jdevelops.utils.http.OkHttpTools;
import com.google.gson.JsonObject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * cas服务
 *
 * @author tnnn
 * @version V1.0
 * @date 2022-10-10 13:36
 */
@Service
public class CasServiceImpl implements CasService {

    private static final Logger log = LoggerFactory.getLogger(CasServiceImpl.class);

    private final CasConfig casConfig;

    public CasServiceImpl(CasConfig casConfig) {
        this.casConfig = casConfig;
    }


    @Override
    public JsonObject verifyTicket(String ticket) throws IOException {
        try {
            String verifyRedirect = casConfig.jRedirect();
            String ticketValidateUrl = casConfig.fullValidateUrl() + "?service=" + verifyRedirect + "&ticket=" + ticket;
            String userStr = OkHttpTools.DEF().get(ticketValidateUrl);
            log.error("单独登录验证票据返回的数据：{}", userStr);
            JsonObject userJson = GsonUtils.getGson().fromJson(userStr, JsonObject.class);
            log.info("单点登录回调回来的票据为：{} ,验证地址为：{}", ticket, verifyRedirect);
            // 最终的数据
            if (userJson.get("errcode").getAsInt() > 0) {
                log.error("cas票据验证失败，原始错误信息为：" + userJson.get("errmsg"));
                throw new CasException("单点登录失败请重新登录");
            }
            return userJson;
        } catch (Exception e) {
            if (e instanceof CasException) {
                throw e;
            } else {
                throw new CasException("统一认证失败，统一认证服务异常", e);
            }
        }
    }

    @Override
    public String redirectCasAddress() {
        return casConfig.fullLoginUrl() + "?service=" + casConfig.jRedirect();
    }


    @Override
    public CasConfig getCasConfig() {
        return casConfig;
    }

    @Override
    public String loginOut(HttpServletRequest request, HttpServletResponse response) {
        // 清除后端会话
        request.getSession().invalidate();
        // 重定向到 CAS 的登出接口
        if(Boolean.TRUE.equals(casConfig.getRedirectLogout())){
            return casConfig.fullLogoutUrl() + "?service=" + casConfig.logoutRedirect();
        }else {
            return null;
        }
    }

}
