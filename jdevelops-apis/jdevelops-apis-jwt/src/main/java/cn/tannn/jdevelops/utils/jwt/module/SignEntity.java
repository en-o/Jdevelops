package cn.tannn.jdevelops.utils.jwt.module;


import cn.tannn.jdevelops.annotations.web.constant.PlatformConstant;
import cn.tannn.jdevelops.utils.jwt.util.JwtUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 用户登录签名办法token的数据
 *
 * @author tnnn
 * @version V1.0
 * @date 2022-07-28 15:14
 */
public class SignEntity<T> {

    /**
     * 必填： jwt 主题 （唯一凭证(一般是登录名）
     */
    String subject;

    /**
     * 平台
     *
     * @see PlatformConstant
     */
    List<String> platform;

    /**
     * 用户身份凭证
     * <p> 1. 用于多套用户体系时，存储的subject重复导致数据一次（两个用户体系登录名会重复）
     * <p> 2. 为空忽略
     * <p> 3. 不为空会将 identity 和 subject 进行拼接, 拼接字符为 __ ，【e.g identity__subject】
     */
    String identity;

    /**
     * 其他信息数据最终会变成{map: jsonObject} 如果时map list的话会变成json
     */
    T map;


    /**
     * @param subject 必填： jwt 主题 （唯一凭证(一般是登录名）
     */
    public static <T> SignEntity<T> init(String subject) {
        return new SignEntity<>(subject, Collections.singletonList(PlatformConstant.COMMON), null);
    }

    /**
     * @param subject  必填： jwt 主题 （唯一凭证(一般是登录名）
     * @param platform jwt所所使用的平台
     * @param map      其他信息数据 value 如果时map list的话会变成json
     */
    public static <T> SignEntity<T> init(String subject,
                                         List<String> platform,
                                         T map) {
        return new SignEntity<>(subject, platform, map);
    }

    /**
     * @param subject  必填： jwt 主题 （唯一凭证(一般是登录名）
     * @param platform jwt所所使用的平台
     */
    public static <T> SignEntity<T> initPlatform(String subject,
                                                 List<String> platform) {
        return new SignEntity<>(subject, platform, null);
    }

    /**
     * @param subject  必填： jwt 主题 （唯一凭证(一般是登录名）
     * @param platform jwt所所使用的平台
     */
    public static <T> SignEntity<T> initPlatform2(String subject,
                                                 String... platform) {
        if (platform == null) {
            return new SignEntity<>(subject, null, null);
        }
        return new SignEntity<>(subject, List.of(platform), null);
    }


    /**
     * @param subject 必填： jwt 主题 （唯一凭证(一般是登录名）
     * @param map     其他信息数据 value 如果时map list的话会变成json
     */
    public static <T> SignEntity<T> initMap(String subject,
                                            T map) {
        return new SignEntity<>(subject, Collections.singletonList(PlatformConstant.COMMON), map);
    }


    /**
     * @param subject  必填： jwt 主题 （唯一凭证(一般是登录名）
     * @param platform jwt所所使用的平台
     * @param map      其他信息数据 value 如果时map list的话会变成json
     */
    public SignEntity(String subject,
                      List<String> platform,
                      T map) {
        this.subject = subject;
        this.platform = platform;
        this.map = map;
    }


    public String getSubject() {
        if (identity == null || identity.isEmpty()) {
            return subject;
        } else {
            return identity + "__" + subject;
        }
    }


    public void setSubject(String subject) {
        this.subject = subject;
    }

    public T getMap() {
        return map;
    }

    public void setMap(T map) {
        this.map = map;
    }

    public List<String> getPlatform() {
        if (platform == null) {
            return new ArrayList<>();
        }
        return platform;
    }

    public void setPlatform(List<String> platform) {
        this.platform = platform;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    /**
     * 将 identity 从 subject 中删除
     *
     * @return  原生Subject
     */
    public String parsingSubject() {
        return JwtUtil.parsingSubject(getSubject());
    }

    @Override
    public String toString() {
        return "SignEntity{" +
                "subject='" + subject + '\'' +
                ", platform=" + platform +
                ", identity='" + identity + '\'' +
                ", map=" + map +
                '}';
    }
}
