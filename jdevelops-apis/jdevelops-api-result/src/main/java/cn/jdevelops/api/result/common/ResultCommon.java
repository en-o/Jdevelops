package cn.jdevelops.api.result.common;

import cn.jdevelops.api.result.emums.ResultCode;
import cn.jdevelops.api.result.exception.ServiceException;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.yomahub.tlog.context.TLogContext;
import io.swagger.v3.oas.annotations.media.Schema;
import org.apache.commons.lang3.StringUtils;
import org.apache.skywalking.apm.toolkit.trace.TraceContext;

import java.io.Serializable;

/**
 * 公共参数
 *
 * @author tnnn
 * @version V1.0
 * @date 2023-03-19 14:13
 */
public class ResultCommon implements Serializable {
    /**
     * 返回结果状态码
     */
    @Schema(description = "状态码")
    private Integer code;

    /**
     * 返回消息
     */
    @Schema(description = "接口消息")
    private String message;

    /**
     * 时间戳
     */
    @Schema(description = "时间戳")
    private Long ts;

    /**
     * traceId
     */
    @Schema(description = "链路追踪ID(skywalking_traceId)")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String traceId;

    public ResultCommon() {
    }

    public ResultCommon(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public ResultCommon(Integer code, String message,String traceId) {
        this.code = code;
        this.message = message;
        this.traceId = traceId;
    }

    /**
     * 自动转换success的返回值：true,false
     */
    public boolean isSuccess() {
        return this.code == ResultCode.SUCCESS.getCode();
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getTs() {
        return  System.currentTimeMillis();
    }


    /**
     * 如果没有手动设置则使用系统中的链路
     * 1. kywalking traceId 优先
     * 2. tlog traceId
     * @return String
     */
    public String getTraceId() {
        try {
            if (!StringUtils.isNotBlank(traceId)) {
                traceId = TraceContext.traceId();
                if (StringUtils.isBlank(traceId)) {
                    traceId = TLogContext.getTraceId();
                }
            }
            return traceId;
        } catch (Exception e) {
            return traceId;
        }
    }


    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    @Override
    public String toString() {
        return "ResultCommon{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", ts=" + ts +
                ", traceId='" + traceId + '\'' +
                '}';
    }


    /**
     * 判断是否有异常。如果有，则抛出 {@link ServiceException} 异常
     */
    public void checkError() throws ServiceException {
        if (isSuccess()) {
            return;
        }
        // 业务异常
        throw new ServiceException(code,message);
    }

}
