package cn.jdevelops.version.config;

import cn.jdevelops.aops.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.mvc.condition.RequestCondition;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * 版本匹配
 *
 * @author tnnn
 * <a href="https://www.cnblogs.com/amuge/articles/13821162.html">参考</a>
 */
public class ApiVersionCondition implements RequestCondition<ApiVersionCondition> {

    private static final Logger LOG = LoggerFactory.getLogger(ApiVersionCondition.class);

    private double apiVersion;

    public ApiVersionCondition(double apiVersion) {
        this.apiVersion = apiVersion;
    }

    public double getApiVersion() {
        return apiVersion;
    }

    public void setApiVersion(double apiVersion) {
        this.apiVersion = apiVersion;
    }

    @Override
    public ApiVersionCondition combine(ApiVersionCondition other) {
        return new ApiVersionCondition(other.getApiVersion());
    }

    @Override
    public ApiVersionCondition getMatchingCondition(HttpServletRequest httpServletRequest) {
        try {
            String reqVersion = httpServletRequest.getParameter("version");
            reqVersion = Optional.ofNullable(reqVersion)
                    .orElse(httpServletRequest.getHeader("version"));
            reqVersion = Optional.ofNullable(reqVersion).orElse("1.0");
            if (StringUtil.isNotBlank(reqVersion)) {
                double version = Double.parseDouble(reqVersion);
                if (version >= this.apiVersion) {
                    return this;
                }
            }
        }catch (Exception e){
            LOG.error("判断版本号失败",e);
            return this;
        }
        return null;

    }

    @Override
    public int compareTo(ApiVersionCondition apiVersionCondition, HttpServletRequest httpServletRequest) {
        return Double.compare(apiVersionCondition.getApiVersion(), this.apiVersion);
    }



}
