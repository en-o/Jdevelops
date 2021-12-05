package cn.jdevelop.message.scan;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.jdevelop.message.config.wx.WxMaConfigration;
import cn.jdevelop.message.core.wx.WxMaSubscribe;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

/**
 * 自动扫描
 * @author tn
 * @date 2020-09-27 10:17
 */
@ConditionalOnWebApplication
@Import({WxMaConfigration.class})
public class EnableAutoScanConfiguration {

    @ConditionalOnMissingBean(WxMaSubscribe.class)
    @Bean
    public WxMaSubscribe wxMaSubscribe(WxMaService wxMaService){
        return new WxMaSubscribe(wxMaService);
    }
}
