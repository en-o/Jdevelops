package cn.tannn.jdevelops.utils.jwt.module;



import cn.tannn.jdevelops.utils.jwt.constant.PlatformConstant;

import java.util.Collections;
import java.util.List;

/**
 * 用户登录签名办法token的数据
 *
 * @author tnnn
 * @version V1.0
 * @date 2022-07-28 15:14
 */
public class SignEntity<T>{

    /**
     * 必填： jwt 主题 （唯一凭证(一般是登录名）
     */
    String subject;

    /**
     * 平台
     */
    List<PlatformConstant> platform;

    /**
     * 其他信息数据最终会变成{map: jsonObject} 如果时map list的话会变成json
     */
    T map;


    /**
     *
     * @param subject 必填： jwt 主题 （唯一凭证(一般是登录名）
     * @param map 其他信息数据 value 如果时map list的话会变成json
     */
    public SignEntity(String subject,
                      T map) {
        this.subject = subject;
        this.platform = Collections.singletonList(PlatformConstant.COMMON);
        this.map = map;
    }

    /**
     *
     * @param subject 必填： jwt 主题 （唯一凭证(一般是登录名）
     * @param platform jwt所所使用的平台
     * @param map 其他信息数据 value 如果时map list的话会变成json
     */
    public SignEntity(String subject,
                      List<PlatformConstant> platform,
                      T map) {
        this.subject = subject;
        this.platform = platform;
        this.map = map;
    }


    /**
     *
     * @param subject 必填： jwt 主题 （唯一凭证(一般是登录名）
     * @param platform jwt所所使用的平台
     */
    public SignEntity(String subject,
                      List<PlatformConstant> platform) {
        this.subject = subject;
        this.platform = platform;
    }


    /**
     *
     * @param subject 必填： jwt 主题 （唯一凭证(一般是登录名）
     */
    public SignEntity(String subject) {
        this.subject = subject;
        this.platform = Collections.singletonList(PlatformConstant.COMMON);
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public  T getMap() {
        return map;
    }

    public void setMap(T map) {
        this.map = map;
    }

    public List<PlatformConstant> getPlatform() {
        return platform;
    }

    public void setPlatform(List<PlatformConstant> platform) {
        this.platform = platform;
    }

    @Override
    public String toString() {
        return "SignEntity{" +
                "subject='" + subject + '\'' +
                ", platform=" + platform +
                ", map=" + map +
                '}';
    }
}
