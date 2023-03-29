package cn.jdevelops.result.response;

import cn.jdevelops.result.common.ResultCommon;
import cn.jdevelops.result.emums.ResultCodeEnum;
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

    public ResultVO(Integer code, String message, B data) {
        super(code, message);
        this.data = data;
    }


    /**
     * 成功默认返回
     *
     * @param <B> 实体
     */
    public static <B> ResultVO<B> success() {
        ResultCodeEnum success = ResultCodeEnum.SUCCESS;
        return new ResultVO<>(success.getCode(),
                success.getMessage());
    }

    /**
     * 成功默认返回
     *
     * @param <B> 实体
     */
    public static <B> ResultVO<B> success(String message) {
        ResultCodeEnum success = ResultCodeEnum.SUCCESS;
        return new ResultVO<>(success.getCode(),message);
    }

    /**
     * 成功返回
     *
     * @param data 数据
     * @param <B>  实体
     */
    public static <B> ResultVO<B> successForData(B data) {
        ResultCodeEnum success = ResultCodeEnum.SUCCESS;
        return new ResultVO<>(success.getCode(),
                success.getMessage(),
                data);
    }

    /**
     * 成功返回
     *
     * @param message 消息
     * @param data    数据
     * @param <B>     实体
     */
    public static <B> ResultVO<B> success(String message, B data) {
        return new ResultVO<>(ResultCodeEnum.SUCCESS.getCode(),
                message,
                data);
    }


    /**
     * 失败返回
     * @param <B>     实体
     */
    public static <B> ResultVO<B> fail() {
        ResultCodeEnum fail = ResultCodeEnum.FAIL;
        return new ResultVO<>(fail.getCode(), fail.getMessage());
    }


    /**
     * 失败返回
     *
     * @param message 消息
     * @param data    数据
     * @param <B>     实体
     */
    public static <B> ResultVO<B> fail(String message, B data) {
        ResultCodeEnum fail = ResultCodeEnum.FAIL;
        return new ResultVO<>(fail.getCode(), message, data);
    }

    /**
     * 失败返回
     *
     * @param message 消息
     * @param <B>     实体
     */
    public static <B> ResultVO<B> fail(String message) {
        ResultCodeEnum fail = ResultCodeEnum.FAIL;
        return new ResultVO<>(fail.getCode(), message);
    }

    /**
     * 失败返回
     *
     * @param data 数据
     * @param <B>  实体
     */
    public static <B> ResultVO<B> failForData(B data) {
        ResultCodeEnum fail = ResultCodeEnum.FAIL;
        return new ResultVO<>(fail.getCode(), fail.getMessage(), data);
    }


    /**
     * 自定义 code 和 message
     *
     * @param code    状态码
     * @param message 消息
     * @param data    数据
     * @param <B>     实体
     */
    public static <B> ResultVO<B> result(int code, String message, B data) {
        return new ResultVO<>(code,
                message,
                data);
    }

    /**
     * 自定义 code 和 message
     *
     * @param code    状态码
     * @param message 消息
     * @param <B>     实体
     */
    public static <B> ResultVO<B> result(int code, String message) {
        return new ResultVO<>(code,
                message);
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


    /**
     * @param isOk   返回 (true)success   false(fail)
     * @param msgStr 返回消息
     * @return { msgStr+"成功" or msgStr+"失败" }
     */
    public static ResultVO<String> resultMsg(boolean isOk, String msgStr) {
        if (isOk) {
            return ResultVO.success(msgStr + "成功");
        } else {
            return ResultVO.fail(msgStr + "失败");
        }
    }

    /**
     * @param isOk   返回 (true)success   false(fail)
     * @param obj    对象数据
     * @param msgStr 返回消息
     * @return { msgStr+"成功" or msgStr+"失败" }
     */
    public static <B> ResultVO<B> resultDataMsgForT(boolean isOk, B obj, String msgStr) {
        if (isOk) {
            return ResultVO.success(msgStr + "成功", obj);
        } else {
            return ResultVO.fail(msgStr + "失败");
        }
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
