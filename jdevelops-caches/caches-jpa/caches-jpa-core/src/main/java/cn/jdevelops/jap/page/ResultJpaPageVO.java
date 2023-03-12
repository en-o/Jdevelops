package cn.jdevelops.jap.page;

import cn.jdevelops.enums.result.ResultCodeEnum;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.yomahub.tlog.context.TLogContext;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;
import org.apache.skywalking.apm.toolkit.trace.TraceContext;

import java.io.Serializable;

/**
 * 分页全局结果集直接放回
 * @author tn
 * @version 1
 * @date 2020/6/8 17:28
 */
@Getter
@Setter
@ToString
@Schema(description = "JPA分页全局结果集，根据ResultPageVO几乎一样，只是规定的data的对象不同")
public class ResultJpaPageVO<T> implements Serializable {

    private static final long serialVersionUID = -7719394736046024902L;


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
     * Page数据
     */
    @Schema(description = "数据")
    private ResourceJpaPage<T> data;


    /**
     * 时间戳
     */
    @Schema(description = "毫秒")
    private Long ts;

    /**
     * traceId
     */
    @Schema(description = "链路追踪ID(skywalking_traceId)")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String traceId;

    /**
     * 自动转换success的返回值：true,false
     */
    public boolean isSuccess() {
        return this.code == ResultCodeEnum.SUCCESS.getCode();
    }

    public Long getTs() {
        return  System.currentTimeMillis();
    }

    public static <T> ResultJpaPageVO<T> success() {
        ResultJpaPageVO<T> resultVO = new ResultJpaPageVO<>();
        resultVO.setCode(ResultCodeEnum.SUCCESS.getCode());
        resultVO.setMessage("success");
        return resultVO;
    }

    public static <T> ResultJpaPageVO<T> success(String message) {
        ResultJpaPageVO<T> resultVO = new ResultJpaPageVO<>();
        resultVO.setCode(ResultCodeEnum.SUCCESS.getCode());
        resultVO.setMessage(message);
        return resultVO;
    }
    public static <T> ResultJpaPageVO<T> success(ResourceJpaPage<T> data, String message) {
        ResultJpaPageVO<T> resultVO = new ResultJpaPageVO<>();
        resultVO.setCode(ResultCodeEnum.SUCCESS.getCode());
        resultVO.setMessage("success");
        resultVO.setData(data);
        return resultVO;
    }

    public static <T> ResultJpaPageVO<T> success(ResourceJpaPage<T> data) {
        ResultJpaPageVO<T> resultVO = new ResultJpaPageVO<>();
        resultVO.setCode(ResultCodeEnum.SUCCESS.getCode());
        resultVO.setMessage("success");
        resultVO.setData(data);
        return resultVO;
    }


    public static <T> ResultJpaPageVO<T> fail(int code, String message) {
        ResultJpaPageVO<T> resultVO = new ResultJpaPageVO<>();
        resultVO.setCode(code);
        resultVO.setMessage(message);
        return resultVO;
    }


    public static <T> ResultJpaPageVO<T> fail(ResultCodeEnum resultCodeEnum) {
        ResultJpaPageVO<T> resultVO = new ResultJpaPageVO<>();
        resultVO.setCode(resultCodeEnum.getCode());
        resultVO.setMessage(resultCodeEnum.getMessage());
        return resultVO;
    }

    public static <T> ResultJpaPageVO<T> fail(String message) {
        ResultJpaPageVO<T> resultVO = new ResultJpaPageVO<>();
        resultVO.setCode(ResultCodeEnum.BIZ_ERROR.getCode());
        resultVO.setMessage(message);
        return resultVO;
    }


    /**
     * @param isok   返回 (true)success   false(fail)
     * @param msgStr 返回消息
     * @return { msgStr+"成功" or msgStr+"失败" }
     */
    public static ResultJpaPageVO<String> resultMsg(boolean isok, String msgStr) {
        if (isok) {
            return ResultJpaPageVO.success(msgStr + "成功");
        } else {
            return ResultJpaPageVO.fail(msgStr + "失败");
        }
    }


    public String getTraceId() {
        try {
//            // 如果设置了就用设置的
            if (StringUtils.isNotBlank(traceId)) {
                return traceId;
            } else { // 没设置就从系统中找找看
//                 skywalking traceId 优先
                traceId = TraceContext.traceId();
                if (StringUtils.isBlank(traceId)) {
//                     tlog traceId
                    traceId = TLogContext.getTraceId();
                }
                return traceId;
            }
        } catch (Exception e) {
            return traceId;
        }
    }

}
