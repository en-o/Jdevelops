package cn.jdevelops.authentication.sas.server.core.entity;

/**
 * token 设置
 * @author <a href="https://tannn.cn/">tan</a>
 * @date 2024/1/9 11:22
 */
public class SasTokenSettings {

    /**
     * 鉴权令牌存活时间/小时 [默认2小时]
     */
    Integer accessTokenTimeToLive;

    /**
     * 刷新令牌的存活时间/天【时间内当令牌过期时，可以用刷新令牌重新申请新鉴权令牌，不需要再认证】[默认1天]
     */
    Integer refreshTokenTimeToLive;

    /**
     * 令牌是否能被刷新【默认ture】Auth 2.0 Parameter: grant_type
     */
    Boolean reuseRefreshTokens;

    public Integer getAccessTokenTimeToLive() {
        if(null == accessTokenTimeToLive){
            return 2;
        }
        return accessTokenTimeToLive;
    }

    public void setAccessTokenTimeToLive(Integer accessTokenTimeToLive) {
        this.accessTokenTimeToLive = accessTokenTimeToLive;
    }

    public Integer getRefreshTokenTimeToLive() {
        if(null == refreshTokenTimeToLive){
            return 1;
        }
        return refreshTokenTimeToLive;
    }

    public void setRefreshTokenTimeToLive(Integer refreshTokenTimeToLive) {
        this.refreshTokenTimeToLive = refreshTokenTimeToLive;
    }

    public boolean getReuseRefreshTokens() {
        if(null == reuseRefreshTokens){
            return true;
        }
        return reuseRefreshTokens;
    }

    public void setReuseRefreshTokens(boolean reuseRefreshTokens) {
        this.reuseRefreshTokens = reuseRefreshTokens;
    }

    @Override
    public String toString() {
        return "SasTokenSettings{" +
                "accessTokenTimeToLive=" + accessTokenTimeToLive +
                ", refreshTokenTimeToLive=" + refreshTokenTimeToLive +
                ", reuseRefreshTokens=" + reuseRefreshTokens +
                '}';
    }
}
