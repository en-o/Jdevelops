package cn.jdevelops.result.custom;

import cn.jdevelops.result.emums.ResultCodeEnum;
import cn.jdevelops.result.response.ResultVO;

/**
 * 默认返回
 *
 * @author tn
 * @version 1
 * @date 2021-12-09 16:50
 */
public class DefaultExceptionResult implements ExceptionResult<ResultVO<?>> {

    @Override
    public ResultVO<?> result(int code, String message) {
        return ResultVO.result(code, message);
    }
    @Override
    public ResultVO<?> result(ResultCodeEnum resultCodeEnum) {
        return ResultVO.result(resultCodeEnum);
    }


    @Override
    public ResultVO<?> success() {
        return ResultVO.success();
    }

    @Override
    public ResultVO<?> error() {
        return ResultVO.fail();
    }



}
