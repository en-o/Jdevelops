package com.deabes.result.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 分页返回统一工具类
 *
 * @author lxw
 * @date 2019年1月17日
 */
@ApiModel(value = "分页返回统一工具类", description = "分页返回统一工具类")
public class ResultPage<T> implements Serializable {

    @ApiModelProperty("当前页")
    private Integer currentPage;
    @ApiModelProperty("每页显示条数")
    private Integer pageSize;
    @ApiModelProperty("总页数")
    private Integer totalPages;
    @ApiModelProperty("总记录数")
    private Long total;
    @ApiModelProperty("数据")
    private T rows;

    public ResultPage() {
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
    public static <T> ResultPage<T> page(Integer pageIndex,
                                         Integer pageSize,
                                         Integer totalPages,
                                         Long total,
                                         T rows) {
        ResultPage<T> tResultPageUtil = new ResultPage<>();
        tResultPageUtil.setCurrentPage(pageIndex);
        tResultPageUtil.setPageSize(pageSize);
        tResultPageUtil.setTotalPages(totalPages);
        tResultPageUtil.setTotal(total);
        tResultPageUtil.setRows(rows);
        return tResultPageUtil;
    }

    public static <T> ResultPage<T> page(Long total, T rows) {
        ResultPage<T> tResultPageUtil = new ResultPage<>();
        tResultPageUtil.setTotal(total);
        tResultPageUtil.setRows(rows);
        return tResultPageUtil;
    }

    /**
     * 获取当前页
     *
     * @return
     * @author lxw
     * @date 2019年1月17日
     */
    public Integer getCurrentPage() {
        return currentPage;
    }

    /**
     * 设定当前页
     *
     * @param currentPage
     * @author lxw
     * @date 2019年1月17日
     */
    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    /**
     * 获取每页显示条数
     *
     * @return
     * @author lxw
     * @date 2019年1月17日
     */
    public Integer getPageSize() {
        return pageSize;
    }

    /**
     * 设定每页显示条数
     *
     * @param pageSize
     * @author lxw
     * @date 2019年1月17日
     */
    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    /**
     * 获取总记录数
     *
     * @return
     * @author lxw
     * @date 2019年1月17日
     */
    public Integer getTotalPages() {
        return totalPages;
    }

    /**
     * 设定总页数
     *
     * @param totalPages
     * @author lxw
     * @date 2019年1月17日
     */
    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    /**
     * 总记录数
     *
     * @return
     * @author lxw
     * @date 2019年3月25日
     */
    public Long getTotal() {
        return total;
    }

    /**
     * 总记录数
     *
     * @param total
     * @author lxw
     * @date 2019年3月25日
     */
    public void setTotal(Long total) {
        this.total = total;
    }

    /**
     * 当前页结果集
     *
     * @return
     * @author lxw
     * @date 2019年3月25日
     */
    public Object getRows() {
        return rows;
    }

    /**
     * 当前页结果集
     *
     * @param rows
     * @author lxw
     * @date 2019年3月25日
     */
    public void setRows(T rows) {
        this.rows = rows;
    }


}
