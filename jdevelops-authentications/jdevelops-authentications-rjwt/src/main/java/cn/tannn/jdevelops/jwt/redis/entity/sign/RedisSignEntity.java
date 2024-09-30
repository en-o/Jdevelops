package cn.tannn.jdevelops.jwt.redis.entity.sign;


import cn.tannn.jdevelops.jwt.redis.entity.StorageUserRole;
import cn.tannn.jdevelops.jwt.redis.entity.StorageUserState;
import cn.tannn.jdevelops.utils.jwt.module.SignEntity;

import java.util.List;
import java.util.Objects;

/**
 * 申请token需要的数据内容
 *
 * @author tnnn
 * @version V1.0
 * @date 2022-07-28 15:14
 */
public class RedisSignEntity<T> extends SignEntity<T> {

    /**
     * redis用 是否永久在线 (默认fales,true redis中设置永不过期)
     */
    Boolean alwaysOnline;


    /**
     * redis用 唯一登录 ，以前的是否会被挤下线 （默认false：不挤）
     * <p>会干扰 alwaysOnline的操作，如果为true永久在线的也会被挤下去</p>
     */
    Boolean onlyOnline;

    /**
     * 用户状态
     */
    StorageUserState userState;
    /**
     * 用户角色
     */
    StorageUserRole userRole;



    /**
     * 用户登录签名办法token的数据
     *
     * @param subject      唯一
     * @param alwaysOnline token是否永久在线
     * @param onlyOnline   以前的是否会被挤下线
     */
    public RedisSignEntity(String subject,
                           Boolean alwaysOnline,
                           Boolean onlyOnline) {
        super(subject, null,null);
        this.alwaysOnline = alwaysOnline;
        this.onlyOnline = onlyOnline;
        this.userRole = new StorageUserRole(subject);
        this.userState = new StorageUserState(subject);
    }

    /**
     * 用户登录签名办法token的数据
     *
     * @param subject      唯一
     * @param platform     jwt所所使用的平台
     * @param alwaysOnline token是否永久在线
     * @param onlyOnline   以前的是否会被挤下线
     */
    public RedisSignEntity(String subject,
                           List<String> platform,
                           Boolean alwaysOnline,
                           Boolean onlyOnline) {
        super(subject, platform,null);
        this.alwaysOnline = alwaysOnline;
        this.onlyOnline = onlyOnline;
        this.userRole = new StorageUserRole(subject);
        this.userState = new StorageUserState(subject);
    }

    /**
     * 用户登录签名办法token的数据
     *
     * @param subject      唯一
     * @param platform     jwt所所使用的平台
     * @param alwaysOnline token是否永久在线
     * @param onlyOnline   以前的是否会被挤下线
     * @param userState 状态
     */
    public RedisSignEntity(String subject,
                           List<String> platform,
                           Boolean alwaysOnline,
                           Boolean onlyOnline,
                           StorageUserState userState) {
        super(subject, platform,null);
        this.alwaysOnline = alwaysOnline;
        this.onlyOnline = onlyOnline;
        this.userRole = new StorageUserRole(subject);
        this.userState = userState;
    }

    /**
     * 用户登录签名办法token的数据
     *
     * @param subject      唯一
     * @param platform     jwt所所使用的平台
     * @param alwaysOnline token是否永久在线
     * @param onlyOnline   以前的是否会被挤下线
     * @param userRole 角色
     * @param userState 状态
     */
    public RedisSignEntity(String subject,
                           List<String> platform,
                           Boolean alwaysOnline,
                           Boolean onlyOnline,
                           StorageUserRole userRole,
                           StorageUserState userState) {
        super(subject, platform,null);
        this.alwaysOnline = alwaysOnline;
        this.onlyOnline = onlyOnline;
        this.userRole = userRole;
        this.userState = userState;
    }

    public RedisSignEntity(SignEntity<T> subject) {
        super(subject.getSubject(),
                subject.getPlatform(),
                subject.getMap());
        this.alwaysOnline = false;
        this.onlyOnline = false;
        this.userRole = new StorageUserRole(subject.getSubject());
        this.userState = new StorageUserState(subject.getSubject());
    }


    /**
     * @param subject      SignEntity
     * @param alwaysOnline token是否永久在线
     * @param onlyOnline   以前的是否会被挤下线
     */
    public RedisSignEntity(SignEntity<T> subject, Boolean alwaysOnline, Boolean onlyOnline) {
        super(subject.getSubject(),
                subject.getPlatform(),
                subject.getMap());
        this.alwaysOnline = alwaysOnline;
        this.onlyOnline = onlyOnline;
        this.userRole = new StorageUserRole(subject.getSubject());
        this.userState = new StorageUserState(subject.getSubject());
    }


    /**
     * 不能无状态有角色
     * @param subject      SignEntity
     * @param alwaysOnline token是否永久在线
     * @param onlyOnline   以前的是否会被挤下线
     * @param userRole 角色
     * @param userState 状态
     */
    public RedisSignEntity(SignEntity<T> subject,
                           Boolean alwaysOnline,
                           Boolean onlyOnline,
                           StorageUserRole userRole,
                           StorageUserState userState) {
        super(subject.getSubject(),
                subject.getPlatform(),
                subject.getMap());
        this.alwaysOnline = alwaysOnline;
        this.onlyOnline = onlyOnline;
        this.userRole = userRole;
        this.userState = userState;
    }

    /**
     * 可以有状态无角色
     * @param subject      SignEntity
     * @param alwaysOnline token是否永久在线
     * @param onlyOnline   以前的是否会被挤下线
     * @param userState 状态
     */
    public RedisSignEntity(SignEntity<T> subject,
                           Boolean alwaysOnline,
                           Boolean onlyOnline,
                           StorageUserState userState) {
        super(subject.getSubject(),
                subject.getPlatform(),
                subject.getMap());
        this.alwaysOnline = alwaysOnline;
        this.onlyOnline = onlyOnline;
        this.userRole = new StorageUserRole(subject.getSubject());
        this.userState = userState;
    }


    @Override
    public String toString() {
        return "RedisSignEntity{" +
                "alwaysOnline=" + alwaysOnline +
                ", onlyOnline=" + onlyOnline +
                ", userState=" + userState +
                ", userRole=" + userRole +
                '}';
    }

    public Boolean getOnlyOnline() {
        if (Objects.isNull(onlyOnline)) {
            return false;
        }
        return onlyOnline;
    }

    public void setOnlyOnline(Boolean onlyOnline) {
        this.onlyOnline = onlyOnline;
    }

    public Boolean getAlwaysOnline() {
        if (Objects.isNull(alwaysOnline)) {
            return false;
        }
        return alwaysOnline;
    }

    public void setAlwaysOnline(Boolean alwaysOnline) {
        this.alwaysOnline = alwaysOnline;
    }

    public StorageUserState getUserState() {
        if (Objects.isNull(userState)) {
            return new StorageUserState(getSubject());
        }
        return userState;
    }

    public void setUserState(StorageUserState userState) {
        this.userState = userState;
    }

    public StorageUserRole getUserRole() {
        if (Objects.isNull(userRole)) {
            return new StorageUserRole(getSubject());
        }
        return userRole;
    }

    public void setUserRole(StorageUserRole userRole) {
        this.userRole = userRole;
    }
}
