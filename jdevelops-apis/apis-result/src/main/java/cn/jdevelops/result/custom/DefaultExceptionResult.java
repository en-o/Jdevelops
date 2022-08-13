package cn.jdevelops.result.custom;

import cn.jdevelops.result.result.ResultVO;

/**
 * 默认返回
 *
 * @author tn
 * @version 1
 * @date 2021-12-09 16:50
 */
public class DefaultExceptionResult implements ExceptionResult<ResultVO<?>> {

    @Override
    public ResultVO<?> success(int code, String message, Object object) {
        return ResultVO.success(code, message, object);
    }

    @Override
    public ResultVO<?> error(int code, String message, Object object) {
        return ResultVO.fail(code, message, object);
    }

    @Override
    public ResultVO<?> error(int code, String message) {
        return ResultVO.fail(code, message);
    }
}
