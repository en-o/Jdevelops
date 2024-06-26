package cn.tannn.jdevelops.jwt.standalone.pojo;


/**
 * token检查结果类
 * WebApiInterceptor.check
 *
 * @author tnnn
 * @version V1.0
 * @date 2023-02-10 11:33
 */
public class CheckToken {

    /**
     * token 是否有效
     */
    Boolean check;

    /**
     * 被检查的toekn
     */
    String token;

    public CheckToken(Boolean check, String token) {
        this.check = check;
        this.token = token;
    }

    @Override
    public String toString() {
        return "CheckVO{" +
                "check=" + check +
                ", token='" + token + '\'' +
                '}';
    }

    public Boolean getCheck() {
        return check;
    }

    public void setCheck(Boolean check) {
        this.check = check;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
