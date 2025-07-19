package cn.tannn.jdevelops.result.response;

import cn.tannn.jdevelops.result.constant.ResultCode;
import cn.tannn.jdevelops.result.exception.ExceptionCode;
import cn.tannn.jdevelops.result.exception.ServiceException;
import cn.tannn.jdevelops.result.views.Views;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.StringJoiner;

/**
 * 常规接口返回类
 *
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @date 2024/5/8 上午10:58
 */
@JsonView(Views.Public.class)
@Schema(description = "全局返回对象")
public class ResultVO<B> extends ResultBasics{
    /**
     * 数据
     */
    @Schema(description = "数据")
    private B data;


    public ResultVO() {
    }

    /**
     * 静态的公共方法
     *
     * @param resultCode 状态
     * @param <B>        data的类型
     * @return ResultVO
     */
    public static <B> ResultVO<B> of(ExceptionCode resultCode) {
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
    public static <B> ResultVO<B> of(B body, ExceptionCode resultCode) {
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
    public  static <B> ResultVO<B> successMessage(String message) {
        ResultVO<B> result = of(null, ResultCode.SUCCESS);
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
    public static <B> ResultVO<B> failMessage(String message) {
        ResultVO<B> result = of(null, ResultCode.FAIL);
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
     * 判断是否有异常。如果有，则抛出 {@link ServiceException} 异常
     * 如果没有，则返回 {@link #data} 数据
     * JsonIgnore避免 jackson 序列化
     */
    @JsonIgnore
    public B getCheckedData() {
        checkError();
        return data;
    }


    /**
     * ResultVO resultMsg
     * @param result 业务查询返回的 true,false
     * @param message message+result(成功，失败)
     * @return ResultVO
     */
    public static ResultVO<String> resultMsg(boolean result, String message) {
        if(result){
            return ResultVO.successMessage(message+"成功");
        }
        return ResultVO.failMessage(message+"失败");
    }


    public B getData() {
        return data;
    }

    public void setData(B data) {
        this.data = data;
    }


    @Override
    public String toString() {
        return new StringJoiner(", ", ResultVO.class.getSimpleName() + "[", "]")
                .add("data=" + data)
                .toString();
    }
}
