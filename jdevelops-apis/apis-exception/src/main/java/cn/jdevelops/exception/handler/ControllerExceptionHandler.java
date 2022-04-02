package cn.jdevelops.exception.handler;

import cn.jdevelops.enums.result.ResultCodeEnum;
import cn.jdevelops.exception.exception.BusinessException;
import cn.jdevelops.exception.result.ExceptionResultWrap;
import cn.jdevelops.result.result.ResultVO;
import cn.jdevelops.string.StringCommon;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 */
@RestControllerAdvice
public class ControllerExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(ControllerExceptionHandler.class);

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
    public Object handleBusinessException(BusinessException e) {
        response.setHeader("content-type", MediaType.APPLICATION_JSON_UTF8_VALUE);
        return ExceptionResultWrap.error(e.getCode(), e.getErrorMessage(),null);
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
    public Object exceptionHandler(NoHandlerFoundException e) {
        response.setHeader("content-type", MediaType.APPLICATION_JSON_UTF8_VALUE);
        return ExceptionResultWrap.error(ResultCodeEnum.AUTH_ERROR.getCode(), "路径不存在，请检查路径是否正确");
    }


    @ExceptionHandler(NullPointerException.class)
    public Object handleNullPointerException(NullPointerException e) {
        response.setHeader("content-type", MediaType.APPLICATION_JSON_UTF8_VALUE);
        // 空指针异常
        return ExceptionResultWrap.error(ResultCodeEnum.SYS_ERROR.getCode(), "暂时无法获取数据");
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Object handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        response.setHeader("content-type", MediaType.APPLICATION_JSON_UTF8_VALUE);
        return ExceptionResultWrap.error(ResultCodeEnum.AUTH_ERROR.getCode(), "请求方式不对 - get post ");
    }


    @ExceptionHandler
    public Object  exceptionHandler(HttpMessageNotReadableException e) {
        response.setHeader("content-type", MediaType.APPLICATION_JSON_UTF8_VALUE);
        String jsonErrorMsg;
        if (e.getLocalizedMessage().contains(JSON_ERROR_INFO) &&
                Objects.nonNull(jsonErrorMsg =
                        dealWithJsonExceptionError(e.getLocalizedMessage()))) {
            return ExceptionResultWrap.error(ResultCodeEnum.JSON_ERROR.getCode(),"格式转换错误,请检查" + jsonErrorMsg + "字段");

        }
        return ExceptionResultWrap.error(ResultCodeEnum.MESSAGE_NO_READING.getCode(),"消息不可读：" + StringUtils.substring(e.getMessage(), 0, CUT_LENGTH));
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Object exception(MethodArgumentNotValidException e) {
        response.setHeader("content-type", MediaType.APPLICATION_JSON_UTF8_VALUE);
        BindingResult bindingResult = e.getBindingResult();
        List<ObjectError> allErrors = bindingResult.getAllErrors();
        StringBuilder sb = new StringBuilder();
        allErrors.forEach(objectError -> {
            FieldError fieldError = (FieldError) objectError;
            sb.append(";").append(fieldError.getField()).append(":").append(fieldError.getDefaultMessage());
        });
        String message = sb.length() > 0 ? sb.substring(1) : sb.toString();
        return ExceptionResultWrap.error(ResultCodeEnum.CHECK_ERROR.getCode(), message);
    }

    @ExceptionHandler(Exception.class)
    public Object handleException(Exception e) {
        log.error(e.getMessage(), e);
        response.setHeader("content-type", MediaType.APPLICATION_JSON_UTF8_VALUE);
        return ExceptionResultWrap.error(ResultCodeEnum.SYS_ERROR.getCode(), "系统异常，请联系管理员");
    }


    @ExceptionHandler(BindException.class)
    public Object bindException(BindException e) {
        response.setHeader("content-type", MediaType.APPLICATION_JSON_UTF8_VALUE);
        // Valid 数据格式校验异常
        StringBuilder resqStr = new StringBuilder();
        e.getFieldErrors().forEach(it -> {
            resqStr.append("字段:").append(it.getField()).append(" ==》 验证不通过，原因是：").append(it.getDefaultMessage());
            resqStr.append("。  ");
        });
        return ExceptionResultWrap.error(ResultCodeEnum.SYS_ERROR.getCode(), resqStr.toString());
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
