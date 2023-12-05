//package cn.jdevelops.sboot.authentication.jredis.entity.multi;
//
//import lombok.*;
//
//import java.util.Map;
//
///**
// * 多端登录
// * JwtRedisUtil.sign 用
// * 后面在想什么实现把，现在不搞了
// *
// * @author tnnn
// * @version V1.0
// * @date 2022-07-28 15:14
// */
//@Getter
//@Setter
//@ToString
//@Builder
//@AllArgsConstructor
//@NoArgsConstructor
//public class MultiSignEntity {
//
//    /**
//     * 必填： jwt 主题 （用户唯一凭证(一般是登录名）
//     */
//    String subject;
//
//    /**
//     * 其他信息数据
//     */
//    Map<String, Object> map;
//
//    /**
//     * 是否永久在线 (默认fales)
//     */
//    boolean alwaysOnline;
//
//    /**
//     * 客户端 ClientUtil.getRequestHeader获取(如果为空，羡慕设置的跟他有关就失效
//     */
//    String client;
//
//    /**
//     * 是否支持多端登录（默认true)
//     */
//    boolean multiLogin;
//
//    /**
//     * 允许几个端同时登录（ = 0 不限制 , >0 会将最先登录的挤下去
//     */
//    int numMultiLogin;
//
//
//
//}
