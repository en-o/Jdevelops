package cn.tannn.jdevelops.result.response;

import cn.tannn.jdevelops.result.constant.ResultCode;
import cn.tannn.jdevelops.result.exception.ExceptionCode;
import cn.tannn.jdevelops.result.exception.ServiceException;
import cn.tannn.jdevelops.result.views.Views;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 分页接口返回类
 *
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @date 2024/5/8 上午10:59
 */
@JsonView(Views.Public.class)
@Schema(description = "分页全局结果集")
public class ResultPageVO <B,P extends PageResult<B>> extends ResultBasics{

    /**
     * 数据
     */
    @Schema(description = "数据")
    private P data;

    public ResultPageVO() {
    }

    /**
     * 静态的公共方法
     *
     * @param body       数据
     * @param resultCode 状态
     * @param <B>        data的类型
     * @return ResultVO
     */
    public static <B,P extends PageResult<B>> ResultPageVO<B,P>  of(P body, ExceptionCode resultCode) {
        ResultPageVO<B,P> result = new ResultPageVO<>();
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
    public static <B,P extends PageResult<B>> ResultPageVO<B,P>   of(P body, int code, String message) {
        ResultPageVO<B,P> result = new ResultPageVO<>();
        result.setData(body);
        result.setCode(code);
        result.setMessage(message);
        return result;
    }


    /**
     * 成功返回
     * @param data 数据
     */
    public static <B,P extends PageResult<B>> ResultPageVO<B,P> success(P data) {
        return of(data, ResultCode.SUCCESS);
    }

    /**
     * 成功返回
     * @param data 数据
     */
    public static <B,P extends PageResult<B>> ResultPageVO<B,P> success(P data,String message) {
        ResultPageVO<B,P> resultPage = of(data, ResultCode.SUCCESS);
        resultPage.setMessage(message);
        return resultPage;
    }

    /**
     * 错误返回
     * @param message 消息
     */
    public static <B,P extends PageResult<B>> ResultPageVO<B,P> fail(String message) {
        return of(null, ResultCode.SUCCESS);
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




}
