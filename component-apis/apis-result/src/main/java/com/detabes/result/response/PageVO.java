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
 * @author lxw
 * @date 2018年5月11日
 */
@ApiModel("分页下标实体类")
@ToString
@Getter
@Setter
@Accessors(chain = true)
public class PageVO {
    @ApiModelProperty("页码，默认第一页")
    private Integer pageIndex;
    @ApiModelProperty("一页显示几条,默认20条")
    private Integer pageSize;
}
