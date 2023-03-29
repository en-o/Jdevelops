package cn.jdevelops.result.response;

import cn.jdevelops.result.common.ResultCommon;
import cn.jdevelops.result.emums.ResultCodeEnum;
import io.swagger.v3.oas.annotations.media.Schema;


/**
 * 分页全局结果集直接放回
 * @param <B>  rows 实体
 * @param <P>  分页实体JpaPageResult
 * @author tn
 * @version 1
 * @date 2020/6/8 17:28
 */
@Schema(description = "分页全局结果集")
public class ResultPageVO<B, P extends PageResult<B>> extends ResultCommon {

    /**
     * 数据
     */
    @Schema(description = "数据")
    private P data;


    /**
     * 成功返回
     * @param data 数据
     * @param message 消息
     */
    public static <B,P extends PageResult<B>> ResultPageVO<B, P> success(P data, String message) {
        return new ResultPageVO<>(
                ResultCodeEnum.SUCCESS.getCode(),
                message,data);
    }

    /**
     * 成功返回
     * @param data 数据
     */
    public static <B,P extends PageResult<B>> ResultPageVO<B, P> success(P data) {
        return new ResultPageVO<>(
                ResultCodeEnum.SUCCESS.getCode(),
                "查询成功",data);
    }

    /**
     * 错误返回
     * @param message 消息
     */
    public static  <B,P extends PageResult<B>> ResultPageVO<B, P>  fail(String message) {
        return new ResultPageVO<>(ResultCodeEnum.FAIL.getCode(),message);
    }
    /**
     * 自定义 code 和 message
     * @param message 消息
     */
    public static <B,P extends PageResult<B>> ResultPageVO<B, P> result(int code, String message) {
        return new ResultPageVO<>(code,message);
    }

    /**
     * 自定义 code 和 message
     *
     * @param resultCodeEnum ResultCodeEnum
     * @param <B>     实体
     */
    public static <B> ResultVO<B> result(ResultCodeEnum resultCodeEnum) {
        return new ResultVO<>(resultCodeEnum.getCode(),
                resultCodeEnum.getMessage());
    }

    public ResultPageVO() {
    }

    public ResultPageVO(Integer code, String message) {
        super(code, message);
    }


    public ResultPageVO(Integer code, String message, P data) {
        super(code, message);
        this.data = data;
    }


    public P getData() {
        return data;
    }

    public void setData(P data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ResultPageVO{" +
                "data=" + data +
                '}';
    }
}
