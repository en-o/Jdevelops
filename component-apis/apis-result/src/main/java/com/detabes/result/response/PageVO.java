package com.detabes.result.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.ToString;

/**
 * 分页排序
 *
 * @author cl
 * @date 2018年5月11日
 */
@ApiModel("分页实体类")
@ToString
public class PageVO {
    @ApiModelProperty("页码，默认第一页")
    private Integer pageIndex = 1;
    @ApiModelProperty("一页显示几条,默认20条")
    private Integer pageSize = 20;
    @ApiModelProperty("根据那一列排序")
    private String orderBy;
    @ApiModelProperty("正序0--Direction.ASC，反序1--Direction.DESC")
    private Integer orderDesc = 1;

    public Integer getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(Integer pageIndex) {
        this.pageIndex = pageIndex;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public Integer getOrderDesc() {
        return orderDesc;
    }

    public void setOrderDesc(Integer orderDesc) {
        this.orderDesc = orderDesc;
    }
}
