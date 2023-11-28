package cn.jdevelops.api.result.request;

import cn.jdevelops.api.result.util.bean.ColumnSFunction;
import cn.jdevelops.api.result.util.bean.ColumnUtil;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

/**
 * 分页
 *
 * @author tn
 * @date 2020-12-17 14:26
 */
@Schema(description = "排序实体类")
public class SortDTO implements Serializable {

    /**
     * 排序字段 (可多字段[1-5])
     * 默认id
     */
    @Schema(description = "排序字段（实体的有效字段）", defaultValue = "id", example = "id")
    @Size(max = 5, message = "排序字段超出了阈值")
    private String[] orderBy;

    /**
     * 排序方式 正序0--Direction.ASC，反序1--Direction.DESC [0-1]
     * 默认倒叙
     */
    @Schema(description = "排序方式（正序0，反序1）", defaultValue = "1", example = "1")
    @Max(value = 1, message = "请正确选择排序方式")
    @Min(value = 0, message = "请正确选择排序方式")
    private Integer orderDesc;


    /**
     * 默认倒序
     *
     * @param orderBy 排序字段
     */
    public SortDTO(String... orderBy) {
        this.orderBy = orderBy;
        this.orderDesc = 1;
    }


    /**
     * 默认倒序
     *
     * @param orderBy 排序字段
     */
    public <T> SortDTO(ColumnSFunction<T, ?>... orderBy) {
        String[] list = new String[10];
        for (int i = 0; i < orderBy.length; i++) {
            list[i] = (ColumnUtil.getFieldName(orderBy[i]));
        }
        this.orderBy = list;
        this.orderDesc = 1;
    }


    /**
     * 排序
     *
     * @param orderDesc 正序0--Direction.ASC，反序1--Direction.DESC
     * @param orderBy   排序字段
     */
    public SortDTO(Integer orderDesc, String... orderBy) {
        this.orderBy = orderBy;
        this.orderDesc = orderDesc;
    }


    /**
     * 排序
     *
     * @param orderDesc 正序0--Direction.ASC，反序1--Direction.DESC
     * @param orderBy   排序字段
     */
    public <T> SortDTO(Integer orderDesc, ColumnSFunction<T, ?>... orderBy) {
        String[] list = new String[10];
        for (int i = 0; i < orderBy.length; i++) {
            list[i] = (ColumnUtil.getFieldName(orderBy[i]));
        }
        this.orderBy = list;
        this.orderDesc = orderDesc;
    }

    public SortDTO() {
    }


    public String[] getOrderBy() {
        if (Objects.isNull(orderBy) || orderBy.length == 0) {
            return new String[]{"id"};
        }
        return orderBy;
    }

    public void setOrderBy(String... orderBy) {
        this.orderBy = orderBy;
    }

    public Integer getOrderDesc() {
        if (Objects.isNull(orderDesc) || orderDesc > 2 || orderDesc < 0) {
            return 1;
        }
        return orderDesc;
    }

    public void setOrderDesc(Integer orderDesc) {
        this.orderDesc = orderDesc;
    }


    @Override
    public String toString() {
        return "SortDTO{" +
                "orderBy=" + Arrays.toString(orderBy) +
                ", orderDesc=" + orderDesc +
                '}';
    }
}
