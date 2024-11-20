package cn.tannn.jdevelops.exception.config;

import cn.tannn.jdevelops.exception.enums.ValidationMessageFormat;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.MediaType;

import java.util.Objects;



/**
 * 异常配置
 *
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @date 2023/8/7 10:00
 */
@ConfigurationProperties(prefix = "jdevelops.exception")
public class ExceptionConfig {


    /**
     * 日志打印 (默认级别为ERROR)
     * <p>默认true 打印</p>
     */
    private Boolean logInput;


    /**
     * 是否设置 HttpServletResponse.status且跟自定义的code同步
     * <p>默认 false 不设置</p>
     */
    private Boolean httpServletResponseStatus;

    /**
     * 设置 content-type
     * @see MediaType
     * <p>默认 application/json;charset=UTF-8</p>
     */
    private MediaType httpServletResponseHeaderContentType;


    /**
     * 处理validation异常提示格式 默认{@link ValidationMessageFormat#FIELD_MESSAGE}
     *
     * @see ValidationMessageFormat
     */
    private ValidationMessageFormat validationMessage;


    public ExceptionConfig() {
    }

    public ExceptionConfig(Boolean logInput
            , Boolean httpServletResponseStatus
            , MediaType httpServletResponseHeaderContentType
            , ValidationMessageFormat validationMessage) {
        this.logInput = logInput;
        this.httpServletResponseStatus = httpServletResponseStatus;
        this.httpServletResponseHeaderContentType = httpServletResponseHeaderContentType;
        this.validationMessage = validationMessage;
    }

    @Override
    public String toString() {
        return "ExceptionConfig{" +
                "logInput=" + logInput +
                ", httpServletResponseStatus=" + httpServletResponseStatus +
                ", httpServletResponseHeaderContentType='" + httpServletResponseHeaderContentType + '\'' +
                ", validationMessage=" + validationMessage +
                '}';
    }

    public Boolean getLogInput() {
        if (null == logInput) {
            return true;
        }
        return logInput;
    }

    public void setLogInput(Boolean logInput) {
        this.logInput = logInput;
    }

    public Boolean getHttpServletResponseStatus() {
        return httpServletResponseStatus != null && httpServletResponseStatus;
    }

    public void setHttpServletResponseStatus(Boolean httpServletResponseStatus) {
        this.httpServletResponseStatus = httpServletResponseStatus;
    }

    public MediaType getHttpServletResponseHeaderContentType() {
        return Objects.requireNonNullElse(httpServletResponseHeaderContentType,  MediaType.APPLICATION_JSON);
    }

    public void setHttpServletResponseHeaderContentType(MediaType httpServletResponseHeaderContentType) {
        this.httpServletResponseHeaderContentType = httpServletResponseHeaderContentType;
    }

    public ValidationMessageFormat getValidationMessage() {
        return validationMessage == null ? ValidationMessageFormat.FIELD_MESSAGE : validationMessage;
    }

    public void setValidationMessage(ValidationMessageFormat validationMessage) {
        this.validationMessage = validationMessage;
    }
}
