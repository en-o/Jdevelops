package cn.jdevelops.jwt.entity;


import lombok.Builder;

import java.util.Map;
import java.util.Objects;

/**
 * 用户登录签名办法token的数据
 *
 * @author tnnn
 * @version V1.0
 * @date 2022-07-28 15:14
 */
@Builder
public class SignEntity{

    /**
     * 必填： jwt 主题 （用户唯一凭证(一般是登录名）
     */
    String subject;

    /**
     * 发行人;无值有默认
     */
    String issuer;

    /**
     * 其他信息数据
     * map = @see JwtUtil.getClaim(token,"map中的key"),如果value=Map,List返回json字符串对象,其他类型都是普通字符串
     */
    Map<String, Object> map;

    /**
     * 是否永久在线 (默认fales)
     */
    boolean alwaysOnline;


    public SignEntity() {
    }

    public SignEntity(String subject, String issuer, Map<String, Object> map, boolean alwaysOnline) {
        this.subject = subject;
        this.issuer = issuer;
        this.map = map;
        this.alwaysOnline = alwaysOnline;
    }

    @Override
    public String toString() {
        return "SignEntity{" +
                "subject='" + subject + '\'' +
                ", issuer='" + issuer + '\'' +
                ", map=" + map +
                ", alwaysOnline=" + alwaysOnline +
                '}';
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getIssuer() {
        if(Objects.isNull(issuer)){
            return "jdevelops";
        }
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public Map<String, Object> getMap() {
        return map;
    }

    public void setMap(Map<String, Object> map) {
        this.map = map;
    }

    public boolean isAlwaysOnline() {
        return alwaysOnline;
    }

    public void setAlwaysOnline(boolean alwaysOnline) {
        this.alwaysOnline = alwaysOnline;
    }
}
