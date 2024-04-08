package cn.jdevelops.authentication.jredis.pojo;

/**
 * 签名对象
 *
 * @author <a href="https://tannn.cn/">tan</a>
 * @date 2024/4/9 10:04
 */
public class TokenVO {

    /**
     * token
     */
    String sign;

    /**
     * token描述
     */
    String description;

    public TokenVO() {
    }

    public TokenVO(String sign, String description) {
        this.sign = sign;
        this.description = description;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "TokenVO{" +
                "sign='" + sign + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
