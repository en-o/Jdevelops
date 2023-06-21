package cn.jdevelops.api.result.response;

import cn.jdevelops.api.result.common.ResultCommon;
import cn.jdevelops.api.result.emums.ExceptionCode;
import cn.jdevelops.api.result.emums.ResultCode;
import cn.jdevelops.api.result.exception.ServiceException;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;


/**
 * 分页全局结果集直接放回
 * @param <P>  分页实体JpaPageResult
 * @author tn
 * @version 1
 * @date 2020/6/8 17:28
 */
@Schema(description = "分页全局结果集")
public class ResultPageVO<P extends PageResult> extends ResultCommon {

    /**
     * 数据
     */
    @Schema(description = "数据")
    private P data;


    public ResultPageVO() {
    }

    public ResultPageVO(Integer code, String message) {
        super(code, message);
    }


    public ResultPageVO(Integer code, String message, P data) {
        super(code, message);
        this.data = data;
    }


    /**
     * 成功返回
     * @param data 数据
     */
    public static <B,P extends PageResult<B>> ResultPageVO< P> success(P data) {
        return of(data, ResultCode.SUCCESS);
    }

    /**
     * 成功返回
     * @param data 数据
     */
    public static <B,P extends PageResult<B>> ResultPageVO< P> success(P data,String message) {
        ResultPageVO<P> resultPage = of(data, ResultCode.SUCCESS);
        resultPage.setMessage(message);
        return resultPage;
    }

    /**
     * 错误返回
     * @param message 消息
     */
    public static <B,P extends PageResult<B>> ResultPageVO< P> fail(String message) {
        return of(null, ResultCode.SUCCESS);
    }

    /**
     * 静态的公共方法
     *
     * @param body       数据
     * @param resultCode 状态
     * @param <B>        data的类型
     * @return ResultVO
     */
    public static <B,P extends PageResult<B>> ResultPageVO<P>  of(P body, ExceptionCode resultCode) {
        ResultPageVO<P> result = new ResultPageVO<>();
        result.setData(body);
        result.setCode(resultCode.getCode());
        result.setMessage(resultCode.getMessage());
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
    public static <B,P extends PageResult<B>> ResultPageVO<P>   of(P body, int code, String message) {
        ResultPageVO<P> result = new ResultPageVO<>();
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
    public  P getCheckedData() {
        checkError();
        return data;
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
