package cn.jdevelop.message.config.wx;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.config.WxMaConfig;
import cn.binarywang.wx.miniapp.config.impl.WxMaDefaultConfigImpl;
import cn.binarywang.wx.miniapp.constant.WxMaConstants;
import cn.jdevelop.message.properties.wx.WxMaProperties;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 微信小程序
 *
 * @author tn
 * @version 1
 * @date 2021/2/28 0:58
 */
@EnableConfigurationProperties({WxMaProperties.class})
@Configuration
public class WxMaConfigration {

    private final WxMaProperties wxMaProperties;

    public WxMaConfigration(WxMaProperties wxMaProperties) {
        this.wxMaProperties = wxMaProperties;
    }


    public WxMaConfig wxMaConfig() {
        WxMaDefaultConfigImpl wxMpDefaultConfig = new WxMaDefaultConfigImpl();
        wxMpDefaultConfig.setAppid(wxMaProperties.getAppid());
        wxMpDefaultConfig.setSecret(wxMaProperties.getSecret());
        String msgDataFormat = wxMaProperties.getMsgDataFormat().toUpperCase();
        wxMpDefaultConfig.setMsgDataFormat(StringUtils.isBlank(msgDataFormat) ?
                WxMaConstants.MsgDataFormat.JSON:msgDataFormat);
        return wxMpDefaultConfig;
    }

    @Bean
    public WxMaService wxMaService() {
        WxMaService wxMpService = new WxMaServiceImpl();
        wxMpService.setWxMaConfig(wxMaConfig());
        return wxMpService;
    }


}
