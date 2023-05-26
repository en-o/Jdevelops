package cn.jdevelops.file.oss.api.config;


/**
 * 七牛
 *
 * @author tnnn
 * @version V1.0
 * @date 2022-05-04 19:09
 */

public class QiNiuConfig {




    /**
     * 是否使用https[默认false]
     */
    private Boolean https = false;


    /**
     * Access key就像用户ID，可以唯一标识你的账户
     */
    private String accessKey;

    /**
     * Secret key是你账户的密码。
     */
    private String secretKey;

    /**
     * 配置自己空间所在的区域： z0, z1, z2
     * @see <a href="https://developer.qiniu.com/kodo/1671/region-endpoint-fq">七牛云</a>
     */
    private String regionId;



    public Boolean getHttps() {
        return https;
    }

    public void setHttps(Boolean https) {
        this.https = https;
    }


    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    @Override
    public String toString() {
        return "QiNiuConfig{" +
                ", https=" + https +
                ", accessKey='" + accessKey + '\'' +
                ", secretKey='" + secretKey + '\'' +
                ", regionId='" + regionId + '\'' +
                '}';
    }
}
