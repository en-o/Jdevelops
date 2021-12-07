package cn.jdevelops.result.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 公共的分页DTO
 *
 * @author tn
 * @date 2021-01-25 13:59
 */
@ApiModel("分页实体")
@ToString
@Getter
@Setter
public class RoutinePageDTO {

    @ApiModelProperty("根据那一列排序")
    private String orderBy;
    @ApiModelProperty("正序0--Direction.ASC，反序1--Direction.DESC")
    private Integer orderDesc;
    @ApiModelProperty(value = "页码，默认第一页", example = "1")
    private Integer pageIndex;
    @ApiModelProperty(value = "一页显示几条,默认20条",example = "20")
    private Integer pageSize;
}
