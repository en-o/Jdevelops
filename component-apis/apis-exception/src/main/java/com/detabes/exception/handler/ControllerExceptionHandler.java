package com.detabes.exception.handler;


import com.detabes.enums.result.ResultCodeEnum;
import com.detabes.exception.exception.BusinessException;
import com.detabes.result.result.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Objects;

/**
 * 全局异常处理
 * @author tn
 * @description 全局异常处理
 */
@Slf4j
@RestControllerAdvice
public class ControllerExceptionHandler {

    @Resource
    private HttpServletResponse response;

    private static final String JSON_ERROR_INFO = "JSON parse error:";
    private static final String SEMICOLON = ";";
    private static final char BLANK = ' ';
    private static final int CUT_LENGTH = 100;

    /**
     * 处理自定义异常
     * @param e 异常
     * @return 返回异常信息
     */
    @ExceptionHandler(BusinessException.class)
    public ResultVO<?> handleBusinessException(BusinessException e) {
        log.error(e.getMessage(), e);
        response.setHeader("content-type", MediaType.APPLICATION_JSON_UTF8_VALUE);
        return ResultVO.fail(e.getCode(), e.getErrorMessage());
    }


    /**
     *  404 拦截必须在配置文件加这个
     * <pre>
     *    spring.mvc.throw-exception-if-no-handler-found=true #出现错误时, 直接抛出异常
     *    spring.resources.add-mappings=false   #不要为我们工程中的资源文件建立映射
     * </pre>
     * @param e 错误
     * @return 返回错误
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResultVO<?> exceptionHandler(NoHandlerFoundException e) {
        response.setHeader("content-type", MediaType.APPLICATION_JSON_UTF8_VALUE);
        return ResultVO.fail(ResultCodeEnum.AuthError.getCode(), "路径不存在，请检查路径是否正确");
    }


    @ExceptionHandler(NullPointerException.class)
    public ResultVO<?> handleNullPointerException(NullPointerException e) {
        response.setHeader("content-type", MediaType.APPLICATION_JSON_UTF8_VALUE);
        log.error("空指针异常->", e);
        return ResultVO.fail(ResultCodeEnum.SysError.getCode(), "暂时无法获取数据");
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResultVO<?> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        response.setHeader("content-type", MediaType.APPLICATION_JSON_UTF8_VALUE);
        return ResultVO.fail(ResultCodeEnum.AuthError.getCode(), "请求方式不对 - get post ");
    }


    @ExceptionHandler
    public ResultVO<?>  exceptionHandler(HttpMessageNotReadableException e) {
        response.setHeader("content-type", MediaType.APPLICATION_JSON_UTF8_VALUE);
        log.error("[Handle_HttpMessageNotReadableException] - {}", e);
        String jsonErrorMsg;
        if (e.getLocalizedMessage().contains(JSON_ERROR_INFO) &&
                Objects.nonNull(jsonErrorMsg =
                        dealWithJsonExceptionError(e.getLocalizedMessage()))) {
            return ResultVO.fail(ResultCodeEnum.JsonError.getCode(),"格式转换错误,请检查" + jsonErrorMsg + "字段");

        }
        return ResultVO.fail(ResultCodeEnum.MessageNoReading.getCode(),"消息不可读：" + StringUtils.substring(e.getMessage(), 0, CUT_LENGTH));
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResultVO<?> exception(MethodArgumentNotValidException e) {
        response.setHeader("content-type", MediaType.APPLICATION_JSON_UTF8_VALUE);
        BindingResult bindingResult = e.getBindingResult();
        List<ObjectError> allErrors = bindingResult.getAllErrors();
        StringBuilder sb = new StringBuilder();
        allErrors.forEach(objectError -> {
            FieldError fieldError = (FieldError) objectError;
            sb.append(";").append(fieldError.getField()).append(":").append(fieldError.getDefaultMessage());
        });
        String message = sb.length() > 0 ? sb.substring(1) : sb.toString();
        return ResultVO.fail(ResultCodeEnum.CheckError.getCode(), message);
    }

    @ExceptionHandler(Exception.class)
    public ResultVO<?> handleException(Exception e) {
        log.error(e.getMessage(), e);
        response.setHeader("content-type", MediaType.APPLICATION_JSON_UTF8_VALUE);
        return ResultVO.fail(ResultCodeEnum.SysError.getCode(), e.getMessage());
    }



    @ExceptionHandler(BindException.class)
    public ResultVO<?> bindException(BindException e) {
        response.setHeader("content-type", MediaType.APPLICATION_JSON_UTF8_VALUE);
        log.error("Valid 数据格式校验异常->", e);
        StringBuilder resqStr = new StringBuilder();
        e.getFieldErrors().forEach(it -> {
            resqStr.append("字段:"+it.getField()+" ==》 验证不通过，原因是："+it.getDefaultMessage());
            resqStr.append("。  ");
        });
        return ResultVO.fail(ResultCodeEnum.SysError.getCode(), resqStr.toString());
    }



    /**
     * 针对性处理JsonException,友好返回异常信息
     *
     * @param errorMsg 错误消息
     * @return 返回字符串
     */
    private String dealWithJsonExceptionError(String errorMsg) {
        int lastSemicolon = errorMsg.lastIndexOf(SEMICOLON);
        if (lastSemicolon != -1) {
            int startIndex = lastSemicolon - 1;
            while (startIndex > 0 && errorMsg.charAt(startIndex) != BLANK) {
                startIndex--;
            }
            return errorMsg.substring(++startIndex, lastSemicolon);
        }

        return null;
    }

}
