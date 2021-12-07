package cn.jdevelops.version.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.mvc.condition.RequestCondition;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 版本匹配
 *
 * @author tnnn
 * @link https://www.cnblogs.com/amuge/articles/13821162.html
 */
@Slf4j
public class ApiVersionCondition implements RequestCondition<ApiVersionCondition> {
    private static final Pattern VERSION_PREFIX_PATTERN = Pattern.compile("^\\S+/([1-9][.][0-9])$");
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
            String parameter = httpServletRequest.getParameter("version");
            String header = httpServletRequest.getHeader("version");
            Matcher matcher = VERSION_PREFIX_PATTERN.matcher(httpServletRequest.getRequestURI());
            if (StringUtils.isNotBlank(parameter)) {
                Double version = Double.valueOf(parameter);
                if (version >= this.apiVersion) {
                    return this;
                }
            } else if (StringUtils.isNotBlank(header)) {
                Double version = Double.valueOf(header);
                if (version >= this.apiVersion) {
                    return this;
                }
            } else if (matcher.find()) {
                Double version = Double.valueOf(matcher.group(1));
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
}