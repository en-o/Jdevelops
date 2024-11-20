package cn.tannn.jdevelops.cas.core;

import cn.tannn.jdevelops.cas.config.CasConfig;
import cn.tannn.jdevelops.cas.exception.CasException;
import cn.tannn.jdevelops.cas.util.GsonUtils;
import cn.tannn.jdevelops.utils.http.OkHttpTools;
import com.google.gson.JsonObject;
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
    }

    @Override
    public CasConfig getCasConfig() {
        return casConfig;
    }
}
