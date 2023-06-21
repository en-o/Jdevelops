package cn.jdevelops.api.result.response;

import cn.jdevelops.api.result.common.ResultCommon;
import cn.jdevelops.api.result.emums.ResultCode;
import cn.jdevelops.api.result.exception.ServiceException;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 全局结果集
 *
 * @author tn
 * @version 1
 * @date 2020/6/8 17:28
 */
@Schema(description = "全局返回对象")
public class ResultVO<B> extends ResultCommon {

    /**
     * 数据
     */
    @Schema(description = "数据")
    private B data;


    public ResultVO() {
    }

    public ResultVO(Integer code, String message) {
        super(code, message);
    }

    public ResultVO(Integer code, String message, String traceId) {
        super(code, message, traceId);
    }

    public ResultVO(Integer code, String message, B data) {
        super(code, message);
        this.data = data;
    }

    public ResultVO(Integer code, String message, String traceId, B data) {
        super(code, message, traceId);
        this.data = data;
    }

    /**
     * 成功默认返回
     *
     * @param <B> 实体
     */
    public static <B> ResultVO<B> success() {
        return of(null, ResultCode.SUCCESS);
    }

    /**
     * 成功返回
     *
     * @param body 数据体
     * @param <B>  body的类型
     * @return ResultVO
     */
    public static <B> ResultVO<B> success(B body) {
        return of(body, ResultCode.SUCCESS);
    }

    /**
     * 成功返回
     *
     * @param message 消息
     * @return ResultVO
     */
    public static ResultVO<String> successMessage(String message) {
        ResultVO<String> result = of("", ResultCode.SUCCESS);
        result.setMessage(message);
        return result;
    }

    /**
     * 成功返回
     *
     * @param message 消息
     * @param body    数据体
     * @param <B>     body的类型
     * @return ResultVO
     */
    public static <B> ResultVO<B> success(String message, B body) {
        ResultVO<B> result = of(body, ResultCode.SUCCESS);
        result.setMessage(message);
        return result;
    }



    /**
     * 失败返回
     *
     * @param <B> 实体
     */
    public static <B> ResultVO<B> fail() {
        return of(null, ResultCode.FAIL);
    }


    /**
     * 失败返回
     *
     * @param <B> 实体
     */
    public static <B> ResultVO<B> fail(B body) {
        return of(body, ResultCode.FAIL);
    }


    /**
     * 失败返回
     *
     * @param message 失败消息
     * @return ResultVO
     */
    public static ResultVO<String> failMessage(String message) {
        ResultVO<String> result = of("", ResultCode.FAIL);
        result.setMessage(message);
        return result;
    }

    /**
     * 失败返回
     *
     * @param message 失败消息
     * @param body    实体数据体
     * @param <B>     body的类型
     * @return ResultVO
     */
    public static <B> ResultVO<B> fail(String message, B body) {
        ResultVO<B> result = of(body, ResultCode.FAIL);
        result.setMessage(message);
        return result;
    }

    /**
     * 静态的公共方法
     *
     * @param resultCode 状态
     * @param <B>        data的类型
     * @return ResultVO
     */
    public static <B> ResultVO<B> of(ResultCode resultCode) {
        ResultVO<B> result = new ResultVO<>();
        result.setCode(resultCode.getCode());
        result.setMessage(resultCode.getMessage());
        return result;
    }

    /**
     * 静态的公共方法
     *
     * @param body       数据
     * @param resultCode 状态
     * @param <B>        data的类型
     * @return ResultVO
     */
    public static <B> ResultVO<B> of(B body, ResultCode resultCode) {
        ResultVO<B> result = new ResultVO<>();
        result.setData(body);
        result.setCode(resultCode.getCode());
        result.setMessage(resultCode.getMessage());
        return result;
    }

    /**
     * 静态的公共方法
     *
     * @param code    状态
     * @param message 状态
     * @param <B>     data的类型
     * @return ResultVO
     */
    public static <B> ResultVO<B> of(int code, String message) {
        ResultVO<B> result = new ResultVO<>();
        result.setCode(code);
        result.setMessage(message);
        return result;
    }
    /**
     * 静态的公共方法
     *
     * @param body    数据
     * @param code    状态
     * @param message 状态
     * @param <B>     data的类型
     * @return ResultVO
     */
    public static <B> ResultVO<B> of(B body, int code, String message) {
        ResultVO<B> result = new ResultVO<>();
        result.setData(body);
        result.setCode(code);
        result.setMessage(message);
        return result;
    }


    /**
     * 判断是否有异常。如果有，则抛出 {@link ServiceException} 异常
     * 如果没有，则返回 {@link #data} 数据
     * JsonIgnore避免 jackson 序列化
     */
    @JsonIgnore
    public B getCheckedData() {
        checkError();
        return data;
    }


    public B getData() {
        return data;
    }

    public void setData(B data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ResultVO{" +
                "data=" + data +
                '}';
    }
}
