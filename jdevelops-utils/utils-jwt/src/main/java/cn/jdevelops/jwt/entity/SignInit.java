package cn.jdevelops.jwt.entity;

import cn.jdevelops.jwt.bean.JwtBean;
import com.auth0.jwt.algorithms.Algorithm;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * sign初始化
 *
 * @author tnnn
 * @version V1.0
 * @date 2023-02-09 09:57
 */
public class SignInit {

    /**
     * jwt 参数
     */
    private JwtBean jwtBean;

    /**
     * 私钥及加密算法
     */
    private  Algorithm algorithm;


    /**
     * 过期时间
     */
    private Date date;

    /**
     * sign 头信息
     */
    private Map<String, Object> header = new HashMap<>(2);

    public SignInit(JwtBean jwtBean) {
        this.jwtBean = jwtBean;
        this.algorithm = Algorithm.HMAC256(jwtBean.getTokenSecret());
        this.date = new Date(System.currentTimeMillis() + jwtBean.getExpireTime());
        this.header.put("typ", "JWT");
        this.header.put("alg", "HS256");
    }

    public SignInit(JwtBean jwtBean,long expireTime) {
        this.jwtBean = jwtBean;
        this.algorithm = Algorithm.HMAC256(jwtBean.getTokenSecret());
        this.date = new Date(expireTime);
        this.header.put("typ", "JWT");
        this.header.put("alg", "HS256");
    }


    @Override
    public String toString() {
        return "SignInit{" +
                "jwtBean=" + jwtBean +
                ", algorithm=" + algorithm +
                ", date=" + date +
                ", header=" + header +
                '}';
    }

    public Map<String, Object> getHeader() {
        return header;
    }

    public void setHeader(Map<String, Object> header) {
        this.header = header;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }



    public JwtBean getJwtBean() {
        return jwtBean;
    }

    public void setJwtBean(JwtBean jwtBean) {
        this.jwtBean = jwtBean;
    }

    public Algorithm getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(Algorithm algorithm) {
        this.algorithm = algorithm;
    }
}
