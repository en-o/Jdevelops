package cn.jdevelops.result.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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
public class PageVO {
    @ApiModelProperty(value = "页码，默认第一页",example = "1")
    private Integer pageIndex;
    @ApiModelProperty(value = "一页显示几条,默认20条",example = "20")
    private Integer pageSize;

    public PageVO() {
    }

    /**
     * 默认 pageIndex = 1
     * @param pageSize 一页显示几条
     */
    public PageVO(Integer pageSize) {
        this.pageIndex = 1;
        this.pageSize = pageSize;
    }

    /**
     * 分页
     * @param pageIndex 页码
     * @param pageSize 一页显示几条
     */
    public PageVO(Integer pageIndex, Integer pageSize) {
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
    }
}
