package com.detabes.result.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 分页排序
 *
 * @author cl
 * @date 2018年5月11日
 */
@ApiModel("分页实体类")
@ToString
@Getter
@Setter
@Accessors(chain = true)
public class PageVO {
    @ApiModelProperty("页码，默认第一页")
    private Integer pageIndex = 1;
    @ApiModelProperty("一页显示几条,默认20条")
    private Integer pageSize = 20;
    @ApiModelProperty("根据那一列排序")
    private String orderBy;
    @ApiModelProperty("正序0--Direction.ASC，反序1--Direction.DESC")
    private Integer orderDesc = 1;
}
