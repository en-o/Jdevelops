package com.detabes.result.vo;

import com.detabes.enums.result.ResultCodeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 全局结果集
 * @author tn
 * @version 1
 * @ClassName ResultVO
 * @description 全局结果集
 * @date 2020/6/8 17:28
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "返回结果集",description = "全局返回对象")
public class ResultVO<T> implements Serializable {

    private static final long serialVersionUID = -7719394736046024902L;

    /** 返回结果状态码 */
    @ApiModelProperty(value = "返回结果状态码")
    private Integer code;

    /** 返回消息 */
    @ApiModelProperty(value = "返回消息")
    private String message;

    /** 数据 */
    @ApiModelProperty(value = "数据")
    private T data;

    /** 时间戳 */
    @ApiModelProperty(value = "时间戳")
    private Long ts = System.currentTimeMillis();

    /** 自动转换success的返回值：true,false*/
    public boolean isSuccess() {
        return this.code == ResultCodeEnum.Success.getCode();
    }

    public static <T> ResultVO<T> success() {
        ResultVO<T> resultVO = new ResultVO<>();
        resultVO.setCode(ResultCodeEnum.Success.getCode());
        resultVO.setMessage("success");
        return resultVO;
    }

    public static <T> ResultVO<T> success(String message) {
        ResultVO<T> resultVO = new ResultVO<>();
        resultVO.setCode(ResultCodeEnum.Success.getCode());
        resultVO.setMessage(message);
        return resultVO;
    }

    public static <T> ResultVO<T> success(T data) {
        ResultVO<T> resultVO = new ResultVO<>();
        resultVO.setCode(ResultCodeEnum.Success.getCode());
        resultVO.setMessage("success");
        resultVO.setData(data);
        return resultVO;
    }


    /**
     * 不要使用
     */
    @Deprecated
    public static <T> ResultVO<T> success(int code, String message) {
        ResultVO<T> resultVO = new ResultVO<>();
        resultVO.setCode(code);
        resultVO.setMessage(message);
        return resultVO;
    }


    public static <T> ResultVO<T> success(T data, String message) {
        ResultVO<T> resultVO = new ResultVO<>();
        resultVO.setCode(ResultCodeEnum.Success.getCode());
        resultVO.setMessage(message);
        resultVO.setData(data);
        return resultVO;
    }


    public static <T> ResultVO<ResultPage> success(String message, ResultPage<T> resultPage) {
        ResultVO<ResultPage> resultVO = new ResultVO<>();
        resultVO.setCode(ResultCodeEnum.Success.getCode());
        resultVO.setMessage(message);
        resultVO.setData(resultPage);
        return resultVO;
    }


    public static <T> ResultVO<T> fail(int code, String message, T data) {
        ResultVO<T> resultVO = new ResultVO<>();
        resultVO.setCode(code);
        resultVO.setMessage(message);
        resultVO.setData(data);
        return resultVO;
    }

    public static <T> ResultVO<T> fail(ResultCodeEnum resultCodeEnum, T data) {
        ResultVO<T> resultVO = new ResultVO<>();
        resultVO.setCode(resultCodeEnum.getCode());
        resultVO.setMessage(resultCodeEnum.getMessage());
        resultVO.setData(data);
        return resultVO;
    }

    public static <T> ResultVO<T> fail(int code, String message) {
        ResultVO<T> resultVO = new ResultVO<>();
        resultVO.setCode(code);
        resultVO.setMessage(message);
        return resultVO;
    }

    public static <T> ResultVO<ResultPage> fail(int code, String message, ResultPage<T> resultPage) {
        ResultVO<ResultPage> resultVO = new ResultVO<>();
        resultVO.setCode(code);
        resultVO.setMessage(message);
        resultVO.setData(resultPage);
        return resultVO;
    }


    public static <T> ResultVO<T> fail(ResultCodeEnum resultCodeEnum) {
        ResultVO<T> resultVO = new ResultVO<>();
        resultVO.setCode(resultCodeEnum.getCode());
        resultVO.setMessage(resultCodeEnum.getMessage());
        return resultVO;
    }

    public static <T> ResultVO<T> fail(String message) {
        ResultVO<T> resultVO = new ResultVO<>();
        resultVO.setCode(ResultCodeEnum.BizError.getCode());
        resultVO.setMessage(message);
        return resultVO;
    }


    /**
     * @param isok   返回 (true)success   false(fail)
     * @param msgStr 返回消息
     * @return { msgStr+"成功" or msgStr+"失败" }
     */
    public static ResultVO<String> resultMsg(Boolean isok, String msgStr) {
        if (isok) {
            return ResultVO.success(msgStr + "成功");
        } else {
            return ResultVO.fail(msgStr + "失败");
        }
    }

    /**
     * @param isok   返回 (true)success   false(fail)
     * @param obj 对象数据
     * @param msgStr 返回消息
     * @return { msgStr+"成功" or msgStr+"失败" }
     */
    public static ResultVO<Object> resultDataMsg(Boolean isok, Object obj, String msgStr) {
        if (isok) {
            return ResultVO.success(obj, msgStr + "成功");
        } else {
            return ResultVO.fail(msgStr + "失败");
        }
    }

    /**
     * @param isok   返回 (true)success   false(fail)
     * @param obj 对象数据
     * @param msgStr 返回消息
     * @return { msgStr+"成功" or msgStr+"失败" }
     */
    public static<T> ResultVO<T> resultDataMsgForT(Boolean isok, T obj, String msgStr) {
        if (isok) {
            return ResultVO.success(obj, msgStr + "成功");
        } else {
            return ResultVO.fail(msgStr + "失败");
        }
    }
}
