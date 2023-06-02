package cn.jdevelops.sboot.authentication.jredis.entity.sign;


import cn.jdevelops.util.jwt.entity.SignEntity;

import java.util.Objects;

/**
 * 用户登录签名办法token的数据
 *
 * @author tnnn
 * @version V1.0
 * @date 2022-07-28 15:14
 */
public class RedisSignEntity<T> extends SignEntity<T> {

    /**
     * redis用 唯一登录 ，以前的是否会被挤下线 （默认false：不挤）
     */
    Boolean alwaysOnline;


    /**
     * 用户登录签名办法token的数据
     * @param subject 唯一
     * @param loginName 登录名
     * @param userId 用户id
     * @param userName 用户名
     * @param alwaysOnline 唯一登录 ，以前的是否会被挤下线 （默认false：不挤）
     */
    public RedisSignEntity(String subject,
                           String loginName,
                           String userId,
                           String userName,
                           Boolean alwaysOnline) {
        super(subject,loginName,userId,userName);
        this.alwaysOnline = alwaysOnline;
    }

    public RedisSignEntity(SignEntity<T> subject) {
        super(subject.getSubject(),
                subject.getLoginName(),
                subject.getUserId(),
                subject.getUserName(),
                subject.getMap());
        this.alwaysOnline = false;
    }

    public RedisSignEntity(SignEntity<T> subject, Boolean alwaysOnline) {
        super(subject.getSubject(),
                subject.getLoginName(),
                subject.getUserId(),
                subject.getUserName(),
                subject.getMap());
        this.alwaysOnline = alwaysOnline;
    }



    @Override
    public String toString() {
        return "RedisSignEntity{" +
                "alwaysOnline=" + alwaysOnline +
                '}';
    }

    public Boolean getAlwaysOnline() {
        if(Objects.isNull(alwaysOnline)){
            return false;
        }
        return alwaysOnline;
    }

    public void setAlwaysOnline(Boolean alwaysOnline) {
        this.alwaysOnline = alwaysOnline;
    }

}
