package cn.tannn.jdevelops.frameworks.web.starter.apiSecurity;


import java.util.Objects;

/**
 * @ClassName ApiEncryptRes  用于返回前端解密返回体的aeskey和返回体
 * @Description TODO
 * @Author L
 * @Date 2024/1/26 17:51
 */
public class ApiEncryptRes {
    /**
     * 经过rsa加密的aeskey
     */
    private String aesKeyByRsa;
    /**
     * 加密数据
     */
    private String data;

    /**
     * 公钥
     */
    private String frontPublicKey;

    public String getAesKeyByRsa() {
        return aesKeyByRsa;
    }

    public void setAesKeyByRsa(String aesKeyByRsa) {
        this.aesKeyByRsa = aesKeyByRsa;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getFrontPublicKey() {
        return frontPublicKey;
    }

    public void setFrontPublicKey(String frontPublicKey) {
        this.frontPublicKey = frontPublicKey;
    }

    @Override
    public String toString() {
        return "ApiEncryptRes{" +
                "aesKeyByRsa='" + aesKeyByRsa + '\'' +
                ", data='" + data + '\'' +
                ", frontPublicKey='" + frontPublicKey + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ApiEncryptRes that = (ApiEncryptRes) o;
        return Objects.equals(aesKeyByRsa, that.aesKeyByRsa) && Objects.equals(data, that.data) && Objects.equals(frontPublicKey, that.frontPublicKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(aesKeyByRsa, data, frontPublicKey);
    }
}
