package com.detabes.message.scan;

import cn.binarywang.wx.miniapp.api.WxMaService;
import com.detabes.message.config.wx.WxMaConfigration;
import com.detabes.message.core.wx.WxMaSubscribe;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

/**
 * @author tn
 * @ClassName EnableScan
 * @description 自动扫描
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
