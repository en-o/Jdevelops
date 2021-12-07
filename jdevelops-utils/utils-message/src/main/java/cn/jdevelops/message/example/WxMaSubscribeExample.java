//package cn.jdevelops.message.example;
//
//import cn.binarywang.wx.miniapp.bean.WxMaSubscribeMessage;
//import cn.jdevelops.message.core.wx.WxMaSubscribe;
//import cn.jdevelops.message.properties.wx.WxMaProperties;
//import org.springframework.stereotype.Controller;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * 示例
// *
// * @author tn
// * @className WxMaSubscribeExample
// * @date 2021-03-01 11:51
// */
//@Controller
//public class WxMaSubscribeExample {
//
//    private final WxMaSubscribe wxMaSubscribe;
//    private final WxMaProperties wxMaProperties;
//
//    public WxMaSubscribeExample(WxMaSubscribe wxMaSubscribe, WxMaProperties wxMaProperties) {
//        this.wxMaSubscribe = wxMaSubscribe;
//        this.wxMaProperties = wxMaProperties;
//    }
//
//    @GetMapping("/testMessage")
//    @ResponseBody
//    public Boolean testMessage() {
//        List<WxMaSubscribeMessage.MsgData> date = new ArrayList<>();
//        WxMaSubscribeMessage.MsgData msgData = new WxMaSubscribeMessage.MsgData();
//        msgData.setName("phrase5");
//        msgData.setValue("预约成功");
//        date.add(msgData);
//        msgData = new WxMaSubscribeMessage.MsgData();
//        msgData.setName("thing11");
//        msgData.setValue("重庆市渝北区融创金茂时代北区");
//        date.add(msgData);
//        msgData = new WxMaSubscribeMessage.MsgData();
//        msgData.setName("thing6");
//        msgData.setValue("请按规则签到");
//        date.add(msgData);
//        msgData = new WxMaSubscribeMessage.MsgData();
//        msgData.setName("time3");
//        msgData.setValue("2021-03-05 22:00~23:00");
//        date.add(msgData);
//        return wxMaSubscribe.subScribe(wxMaProperties.getTemplateId(),
//                "opZJu5SP-BzygsLRsjQMCxNgJ6kk",null,
//                wxMaProperties.getMiniProgramState(),date);
//    }
//}
