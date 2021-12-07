package cn.jdevelops.result.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 分页
 * @author tn
 * @date 2020-12-17 14:26
 */
@ApiModel("分页排序实体类")
@ToString
@Getter
@Setter
public class SortVO {
    @ApiModelProperty("根据那一列排序")
    private String orderBy;
    @ApiModelProperty("正序0--Direction.ASC，反序1--Direction.DESC")
    private Integer orderDesc;
}
