package cn.jdevelops.util.jwt.entity;



import java.util.Map;
import java.util.Objects;

/**
 * 用户登录签名办法token的数据
 *
 * @author tnnn
 * @version V1.0
 * @date 2022-07-28 15:14
 */
public class SignEntity{

    /**
     * 必填： jwt 主题 （用户唯一凭证(一般是登录名）
     */
    String subject;

    /**
     * 其他信息数据 value 如果时map list的话会变成json
     */
    Map<String, Object> map;

    /**
     * redis用 唯一登录 ，以前的会被挤下线 （默认false）
     */
    Boolean alwaysOnline;


    public SignEntity() {
    }

    public SignEntity(String subject, Map<String, Object> map, Boolean alwaysOnline) {
        this.subject = subject;
        this.map = map;
        this.alwaysOnline = alwaysOnline;
    }

    public SignEntity(String subject, Boolean alwaysOnline) {
        this.subject = subject;
        this.alwaysOnline = alwaysOnline;
    }

    /**
     * 默认false
     */
    public SignEntity(String subject) {
        this.subject = subject;
        this.alwaysOnline = false;
    }

    public SignEntity(String subject, Map<String, Object> map) {
        this.subject = subject;
        this.map = map;
        this.alwaysOnline = false;
    }

    @Override
    public String toString() {
        return "SignEntity{" +
                "subject='" + subject + '\'' +
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


    public Map<String, Object> getMap() {
        return map;
    }

    public void setMap(Map<String, Object> map) {
        this.map = map;
    }

    public Boolean isAlwaysOnline() {
        if(Objects.isNull(alwaysOnline)){
            return false;
        }
        return alwaysOnline;
    }

    public void setAlwaysOnline(Boolean alwaysOnline) {
        this.alwaysOnline = alwaysOnline;
    }
}
