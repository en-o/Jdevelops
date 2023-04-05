package cn.jdevelops.util.jwt.entity;


import lombok.Builder;

import java.util.Map;

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
     * 其他信息数据 value 如果时map list的话会变成json
     */
    Map<String, Object> map;



    public SignEntity() {
    }

    public SignEntity(String subject, Map<String, Object> map) {
        this.subject = subject;
        this.map = map;
    }

    @Override
    public String toString() {
        return "SignEntity{" +
                "subject='" + subject + '\'' +
                ", map=" + map +
                '}';
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }


    public Map<String, Object> getMap() {
        return map;
    }

    public void setMap(Map<String, Object> map) {
        this.map = map;
    }

}
