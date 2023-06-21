package cn.jdevelops.api.result.custom;

import cn.jdevelops.api.result.emums.ExceptionCode;
import cn.jdevelops.api.result.response.ResultVO;

/**
 * 默认返回
 *
 * @author tn
 * @version 1
 * @date 2021-12-09 16:50
 */
public class DefaultExceptionResult implements ExceptionResult<ResultVO<?>> {


    @Override
    public ResultVO<?> result(ExceptionCode resultCode) {
        return ResultVO.of(resultCode);
    }

    @Override
    public ResultVO<?> result(int code, String message) {
        return ResultVO.of(code, message);
    }

    @Override
    public ResultVO<?> result(int code, String message, Object data) {
        return ResultVO.of(data, code, message);
    }

    @Override
    public ResultVO<?> result(ExceptionCode resultCode, Object data) {
        return ResultVO.of(data, resultCode);
    }


    @Override
    public ResultVO<?> success() {
        return ResultVO.success();
    }

    @Override
    public ResultVO<?> success(Object data) {
        return ResultVO.success(data);
    }

    @Override
    public ResultVO<?> fail(String message) {
        return ResultVO.failMessage(message);
    }

    @Override
    public ResultVO<?> fail() {
        return ResultVO.fail();
    }
}
