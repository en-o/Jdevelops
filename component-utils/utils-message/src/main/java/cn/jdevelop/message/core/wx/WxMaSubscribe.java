package cn.jdevelop.message.core.wx;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.WxMaSubscribeService;
import cn.binarywang.wx.miniapp.bean.WxMaSubscribeMessage;
import cn.binarywang.wx.miniapp.constant.WxMaConstants;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * 微信小程序发送订阅消息
 *
 * @author tn
 * @date 2021-03-01 10:14
 */
@Slf4j
public class WxMaSubscribe {

    private final WxMaService wxMaService;

    public WxMaSubscribe(WxMaService wxMaService) {
        this.wxMaService = wxMaService;
    }

    /**
     * @param templateId       模板id
     * @param toUser           用户 appid
     * @param page             跳转页面地址
     * @param miniProgramState "developer", "开发版"
     *                         "trial", "体验版"
     *                         "formal", "正式版" (默认)
     * @param date             模板数据
     *                         date:中的字符不要超过20字符
     * @return
     */
    public Boolean subScribe(String templateId,
                             String toUser,
                             String page,
                             String miniProgramState,
                             List<WxMaSubscribeMessage.MsgData> date) {
        try {
            WxMaSubscribeService subscribeService = wxMaService.getSubscribeService();
            WxMaSubscribeMessage.WxMaSubscribeMessageBuilder wxBuilder =
                    WxMaSubscribeMessage.builder()
                            .data(date)
                            .toUser(toUser)
                            .templateId(templateId);
            if (StringUtils.isNotBlank(page)) {
                wxBuilder.page(page);
            }
            if (StringUtils.isNotBlank(miniProgramState)) {
                wxBuilder.miniprogramState(miniProgramState);
            } else {
                wxBuilder.miniprogramState(WxMaConstants.MiniProgramState.FORMAL);
            }
            WxMaSubscribeMessage build = wxBuilder.build();

            subscribeService.sendSubscribeMsg(build);
            return true;
        } catch (Exception e) {
            if (e instanceof WxErrorException) {
                // 当用户没有授权，获取之前的授权已使用。微信会返回错误代码 {"errcode":43101,"errmsg":"user refuse to accept the msg hint: [xxxxxxxxxxx]"}
                if (((WxErrorException) e).getError().getErrorCode() != 43101) {
                    log.error("微信小程序发送消息报错：", e);
                } else {
                    log.error("当用户没有授权：", e);
                }
            } else {
                log.error("微信小程序发送消息报错：", e);
            }
        }
        return false;
    }
}
