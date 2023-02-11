package cn.jdevelops.jredis.entity.multi;

import cn.jdevelops.jredis.entity.only.StorageUserTokenEntity;
import lombok.*;

/**
 * 登录用户存在redis中的登录信息
 *
 * @author tnnn
 * @version V1.0
 * @date 2022-07-28 12:08
 */
@Getter
@Setter
@ToString
public class MultiStorageUserTokenEntity  extends StorageUserTokenEntity {

    /**
     * 客户端名字
     */
    String client;
    /**
     * 时间戳 默认当前时间
     */
    Long timestamps;

    /**
     * 是否支持多端登录（默认true, false则单点登录)
     */
    boolean multiLogin;

    /**
     * 允许几个端同时登录（ = 0 不限制 , >0 会将最先登录的挤下去
     */
    int numMultiLogin;



}
