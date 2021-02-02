package com.detabes.result.result;

import com.detabes.enums.result.ResultCodeEnum;
import com.detabes.result.page.ResourcePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 分页全局结果集
 * @author tn
 * @version 1
 * @ClassName ResultVO
 * @description 分页全局结果集
 * @date 2020/6/8 17:28
 */
@Getter
@Setter
@ToString
@ApiModel(value = "分页全局结果集",description = "全局返回对象")
public class ResultPageVO<T> implements Serializable {

    private static final long serialVersionUID = -7719394736046024902L;

    /** 返回结果状态码 */
    @ApiModelProperty(value = "返回结果状态码")
    private Integer code;

    /** 返回消息 */
    @ApiModelProperty(value = "返回消息")
    private String message;

    /**
     * 当前页
     */
    @ApiModelProperty("当前页")
    private Integer currentPage;
    /**
     * 每页显示条数
     */
    @ApiModelProperty("每页显示条数")
    private Integer pageSize;
    /**
     * 总页数
     */
    @ApiModelProperty("总页数")
    private Integer totalPages;
    /**
     * 总记录数
     */
    @ApiModelProperty("总记录数")
    private Long total;
    /**
     * 数据
     */
    @ApiModelProperty("数据")
    private T rows;

    /** 时间戳 */
    @ApiModelProperty(value = "时间戳")
    private Long ts = System.currentTimeMillis();

    /** 自动转换success的返回值：true,false*/
    public boolean isSuccess() {
        return this.code == ResultCodeEnum.Success.getCode();
    }


    /**
     * @param pageIndex  当前页
     * @param pageSize   每页显示条数
     * @param totalPages 总页数
     * @param total      总记录数
     * @param rows       数据
     * @param <T>        t
     * @return
     */
    public static <T> ResultPageVO<T> pageSuccess(Integer pageIndex,
                                           Integer pageSize,
                                           Integer totalPages,
                                           Long total,
                                           T rows) {
        ResultPageVO<T> resultPageVO = new ResultPageVO<>();
        resultPageVO.setCode(ResultCodeEnum.Success.getCode());
        resultPageVO.setMessage("success");
        resultPageVO.setCurrentPage(pageIndex);
        resultPageVO.setPageSize(pageSize);
        resultPageVO.setTotalPages(totalPages);
        resultPageVO.setTotal(total);
        resultPageVO.setRows(rows);
        return resultPageVO;
    }

    /**
     * @param pageIndex  当前页
     * @param pageSize   每页显示条数
     * @param totalPages 总页数
     * @param total      总记录数
     * @param rows       数据
     * @param message    消息
     * @param <T>        t
     * @return
     */
    public static <T> ResultPageVO<T> pageSuccess(Integer pageIndex,
                                                  Integer pageSize,
                                                  Integer totalPages,
                                                  Long total,
                                                  T rows,
                                                  String message) {
        ResultPageVO<T> resultPageVO = new ResultPageVO<>();
        resultPageVO.setCode(ResultCodeEnum.Success.getCode());
        resultPageVO.setMessage(message);
        resultPageVO.setCurrentPage(pageIndex);
        resultPageVO.setPageSize(pageSize);
        resultPageVO.setTotalPages(totalPages);
        resultPageVO.setTotal(total);
        resultPageVO.setRows(rows);
        return resultPageVO;
    }

    public static <T> ResultPageVO<T> pageSuccess(Long total, T rows) {
        ResultPageVO<T> pageVO = new ResultPageVO<>();
        pageVO.setTotal(total);
        pageVO.setRows(rows);
        pageVO.setCode(ResultCodeEnum.Success.getCode());
        pageVO.setMessage("success");
        return pageVO;
    }

    /**
     *
     * @param total      总记录数
     * @param rows       数据
     * @param message    消息
     * @param <T> T
     * @return ResultPageVO
     */
    public static <T> ResultPageVO<T> pageSuccess(Long total, T rows,String message) {
        ResultPageVO<T> pageVO = new ResultPageVO<>();
        pageVO.setTotal(total);
        pageVO.setRows(rows);
        pageVO.setCode(ResultCodeEnum.Success.getCode());
        pageVO.setMessage("success");
        return pageVO;
    }


}
