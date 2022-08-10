package cn.jdevelops.version.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.mvc.condition.RequestCondition;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * 版本匹配
 *
 * @author tnnn
 * @link https://www.cnblogs.com/amuge/articles/13821162.html
 */
@Slf4j
public class ApiVersionCondition implements RequestCondition<ApiVersionCondition> {
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
            if (isNotBlank(reqVersion)) {
                double version = Double.parseDouble(reqVersion);
                if (version >= this.apiVersion) {
                    return this;
                }
            }
        }catch (Exception e){
            log.error("判断版本号失败",e);
            return this;
        }
        return null;

    }

    @Override
    public int compareTo(ApiVersionCondition apiVersionCondition, HttpServletRequest httpServletRequest) {
        return Double.compare(apiVersionCondition.getApiVersion(), this.apiVersion);
    }


    private static boolean isNotBlank(CharSequence cs) {
        return !isBlank(cs);
    }
    private static boolean isBlank(CharSequence cs) {
        int strLen = length(cs);
        if (strLen == 0) {
            return true;
        } else {
            for(int i = 0; i < strLen; ++i) {
                if (!Character.isWhitespace(cs.charAt(i))) {
                    return false;
                }
            }

            return true;
        }
    }

    private static int length(CharSequence cs) {
        return cs == null ? 0 : cs.length();
    }
}
