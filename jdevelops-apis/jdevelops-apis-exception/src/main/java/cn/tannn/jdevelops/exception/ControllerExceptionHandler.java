package cn.tannn.jdevelops.exception;

import cn.tannn.jdevelops.exception.built.BusinessException;
import cn.tannn.jdevelops.exception.config.ExceptionConfig;
import cn.tannn.jdevelops.exception.enums.ValidationMessageFormat;
import cn.tannn.jdevelops.result.constant.ParamCode;
import cn.tannn.jdevelops.result.exception.ExceptionResultWrap;
import cn.tannn.jdevelops.result.exception.ServiceException;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.List;
import java.util.Objects;

import static cn.tannn.jdevelops.result.constant.ResultCode.SYS_ERROR;


/**
 * 全局异常处理
 *
 * @author tn
 */
@RestControllerAdvice
public class ControllerExceptionHandler implements ResponseBodyAdvice<Object> {

    private static final Logger log = LoggerFactory.getLogger(ControllerExceptionHandler.class);
    private static final String JSON_ERROR_INFO = "JSON parse error:";
    private static final String SEMICOLON = ";";
    private static final char BLANK = ' ';
    private static final int CUT_LENGTH = 20;


    @Resource
    private ExceptionConfig exceptionConfig;


    /**
     * 处理自定义异常 - BusinessException
     *
     * @param e 异常
     * @return 返回异常信息
     */
    @ExceptionHandler(BusinessException.class)
    public Object handleBusinessException(BusinessException e, HttpServletResponse response) {
        responseConfigCustom(response, e, e.getCode());
        return ExceptionResultWrap.result(e.getCode(), e.getErrorMessage());
    }

    /**
     * spring data 处理
     * @param ex
     * @param response
     * @return
     */
    @ExceptionHandler(org.springframework.dao.DataAccessException.class)
    public Object handleSQLException(org.springframework.dao.DataAccessException ex, HttpServletResponse response) {
        log.error("SQL exception occurred");
        responseConfig(response, ex, SYS_ERROR.getCode());
        return ExceptionResultWrap.result(SYS_ERROR.getCode(), "数据库操作失败，请稍后再试");
    }

    /**
     * 处理自定义异常 - ServiceException
     *
     * @param e 异常
     * @return 返回异常信息
     */
    @ExceptionHandler(ServiceException.class)
    public Object handleBusinessException(ServiceException e, HttpServletResponse response) {
        responseConfig(response, e, e.getCode());
        return ExceptionResultWrap.result(e.getCode(), e.getMessage());
    }

    /**
     * 404 拦截必须在配置文件加这个
     * <pre>
     *    spring.mvc.throw-exception-if-no-handler-found=true #出现错误时, 直接抛出异常
     *    spring.resources.add-mappings=false   #不要为我们工程中的资源文件建立映射
     * </pre>
     *
     * @param e 错误
     * @return 返回错误
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public Object exceptionHandler(NoHandlerFoundException e, HttpServletResponse response) {
        responseConfig(response, e, 403);
        return ExceptionResultWrap.result(403, "路径不存在，请检查路径是否正确");
    }

    /**
     * 空指针处理
     */
    @ExceptionHandler(NullPointerException.class)
    public Object handleNullPointerException(NullPointerException e, HttpServletResponse response) {
        responseConfig(response, e, SYS_ERROR.getCode());
        // 空指针异常
        return ExceptionResultWrap.result(SYS_ERROR.getCode(), "暂时无法获取数据");
    }

    /**
     * 请求方式处理
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Object handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e, HttpServletResponse response) {
        responseConfig(response, e, SYS_ERROR.getCode());
        return ExceptionResultWrap.result(SYS_ERROR.getCode(), "请求方式不对 - 请检查接口 method 是 get/post/put/delete ");
    }

    /**
     * 数据异常处理
     */
    @ExceptionHandler
    public Object exceptionHandler(HttpMessageNotReadableException e, HttpServletResponse response) {
        responseConfig(response, e, SYS_ERROR.getCode());
        String jsonErrorMsg;
        if (e.getLocalizedMessage().contains(JSON_ERROR_INFO) &&
                Objects.nonNull(jsonErrorMsg =
                        dealWithJsonExceptionError(e.getLocalizedMessage()))) {
            return ExceptionResultWrap.result(ParamCode.JSON_ERROR.getCode(), "请求参数格式错误,请检查。错误消息：" + jsonErrorMsg);
        }
        log.warn("消息不可读：", e);
        return ExceptionResultWrap.result(ParamCode.MESSAGE_NO_READING.getCode(), "消息不可读：" + StringUtils.substring(e.getMessage(), 0, CUT_LENGTH));
    }

    /**
     * validation错误处理
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Object exception(MethodArgumentNotValidException e, HttpServletResponse response) {
        responseConfig(response, e, SYS_ERROR.getCode());
        BindingResult bindingResult = e.getBindingResult();
        List<ObjectError> allErrors = bindingResult.getAllErrors();
        StringBuilder sb = new StringBuilder();
        for (ObjectError objectError : allErrors) {
            FieldError fieldError = (FieldError) objectError;
            ValidationMessageFormat validationMessage = exceptionConfig.getValidationMessage();
            if (Objects.requireNonNull(validationMessage) == ValidationMessageFormat.FIELD_MESSAGE) {
                sb.append(";").append(fieldError.getField()).append(":").append(fieldError.getDefaultMessage());
            } else if (validationMessage == ValidationMessageFormat.MESSAGE) {
                sb.append(";").append(fieldError.getDefaultMessage());
            } else if (validationMessage == ValidationMessageFormat.ONLY_ONE) {
                return ExceptionResultWrap.result(ParamCode.CHECK_ERROR.getCode(), fieldError.getDefaultMessage());
            }
        }

        String message = !sb.isEmpty() ? sb.substring(1) : sb.toString();
        return ExceptionResultWrap.result(ParamCode.CHECK_ERROR.getCode(), message);
    }


    @ExceptionHandler(RuntimeException.class)
    public Object handleRuntimeException(RuntimeException e, HttpServletResponse response) {
        responseConfig(response, e, SYS_ERROR.getCode());
        return ExceptionResultWrap.result(e);
    }


    @ExceptionHandler(BindException.class)
    public Object bindException(BindException e, HttpServletResponse response) {
        responseConfig(response, e, SYS_ERROR.getCode());
        // Valid 数据格式校验异常
        StringBuilder resqStr = new StringBuilder();
        e.getFieldErrors().forEach(it -> {
            resqStr.append("字段:").append(it.getField()).append(" ==》 验证不通过，原因是：").append(it.getDefaultMessage());
            resqStr.append("。  ");
        });
        return ExceptionResultWrap.result(SYS_ERROR.getCode(), resqStr.toString());
    }

    @ExceptionHandler(Exception.class)
    public Object handleException(Exception e, HttpServletResponse response) {
        responseConfig(response, e, SYS_ERROR.getCode());
        return ExceptionResultWrap.result(e);
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

    private void responseConfigCustom(HttpServletResponse response, BusinessException e, int code) {
        logInput(e);
        // 以自己设置的 http servlet response status 为主
        if (Boolean.TRUE.equals(e.getHttpServletResponseStatus())) {
            response.setStatus(code);
        } else {
            responseStatus(response, code);
        }
    }


    private void responseConfig(HttpServletResponse response, Exception e, int code) {
        logInput(e);
        responseStatus(response, code);
    }


    /**
     * 设置 response status
     *
     * @param response HttpServletResponse
     * @param code     code
     */
    private void responseStatus(HttpServletResponse response, int code) {
        if (Boolean.TRUE.equals(exceptionConfig.getHttpServletResponseStatus())) {
            response.setStatus(code);
        }
    }

    /**
     * 设置 log.error
     *
     * @param e Exception
     */
    private void logInput(Exception e) {
        if (Boolean.TRUE.equals(exceptionConfig.getLogInput())) {
            log.error(e.getMessage(), e);
        }
    }


    /**
     * 控制beforeBodyWrite方法的执行范围
     *
     * @param returnType    当前请求的方法参数类型
     * @param converterType 消息转换器的类型
     * @return 决定是否对特定的请求应用beforeBodyWrite方法
     */
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        // returnType = ControllerExceptionHandler 触发
        // ，如果不处理将全局有效
        return this.getClass().isAssignableFrom(returnType.getContainingClass());
    }

    /**
     * 响应体写入HTTP响应之前进行自定义处理
     *
     * @param body                  the body to be written
     * @param returnType            the return type of the controller method
     * @param selectedContentType   the content type selected through content negotiation
     * @param selectedConverterType the converter type selected to write to the response
     * @param request               the current request
     * @param response              the current response
     */
    @Override
    public Object beforeBodyWrite(Object body
            , MethodParameter returnType
            , MediaType selectedContentType
            , Class<? extends HttpMessageConverter<?>> selectedConverterType
            , ServerHttpRequest request, ServerHttpResponse response) {
        response.getHeaders().setContentType(exceptionConfig.getHttpServletResponseHeaderContentType());
        return body;
    }
}
