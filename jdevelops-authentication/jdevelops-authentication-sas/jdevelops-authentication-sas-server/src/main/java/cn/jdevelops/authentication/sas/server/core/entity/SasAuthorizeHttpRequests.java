package cn.jdevelops.authentication.sas.server.core.entity;

import java.util.HashSet;
import java.util.Set;

/**
 * 请求的放行与拦截
 *
 * @author <a href="https://tannn.cn/">tan</a>
 * @date 2024/1/9 11:49
 */
public class SasAuthorizeHttpRequests {


    /**
     * 放行静态资源
     */
    private Set<String> mvcMatchers = new HashSet<>();
    /**
     * 放行接口
     */
    private Set<String> antMatchers = new HashSet<>();


    public Set<String> getMvcMatchers() {
        mvcMatchers.add("/assets/**");
        mvcMatchers.add("/webjars/**");
        mvcMatchers.add("/page/login");
        mvcMatchers.add("/oauth/client/add");
        return mvcMatchers;
    }


    public void setMvcMatchers(Set<String> mvcMatchers) {
        this.mvcMatchers = mvcMatchers;
    }

    public Set<String> getAntMatchers() {
        antMatchers.add("/page/login");
        return antMatchers;
    }

    public void setAntMatchers(Set<String> antMatchers) {
        this.antMatchers = antMatchers;
    }


    public String[] antMatchersToArr() {
        return getAntMatchers().toArray((new String[0]));
    }
    public String[] mvcMatchersToArr() {
        return getMvcMatchers().toArray((new String[0]));
    }

    @Override
    public String toString() {
        return "SasAuthorizeHttpRequests{" +
                "mvcMatchers=" + mvcMatchers +
                ", antMatchers=" + antMatchers +
                '}';
    }
}
