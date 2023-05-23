package cn.jdevelops.file.oss.api.config;


/**
 * 七牛
 *
 * @author tnnn
 * @version V1.0
 * @date 2022-05-04 19:09
 */

public class Aws3Config {

    /**
     * Access key就像用户ID，可以唯一标识你的账户(密钥名称)
     */
    private String accessKey;

    /**
     * Secret key是你账户的密码。
     */
    private String secretKey;

    /**
     * 配置自己空间所在的区域：
     * 默认："us-east-1"
     * @see software.amazon.awssdk.regions.Region
     * @see <a href="https://docs.aws.amazon.com/zh_cn/AWSEC2/latest/UserGuide/using-regions-availability-zones.html#concepts-regions">Region</a>
     */
    private String regionId;

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
        if(regionId == null || regionId.length() == 0) {
            return "us-east-1";
        }
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    @Override
    public String toString() {
        return "Aws3Config{" +
                "accessKey='" + accessKey + '\'' +
                ", secretKey='" + secretKey + '\'' +
                ", regionId='" + regionId + '\'' +
                '}';
    }
}
