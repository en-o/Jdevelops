package cn.tannn.jdevelops.result.request;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

/**
 * 排序
 *
 * @author <a href="https://tannn.cn/">tan</a>
 * @date 2024/5/8 上午11:00
 */
@Schema(description = "排序参数")
public class Sorted implements Serializable {

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


    public Sorted() {
    }

    /**
     * 默认
     * @return Sorted
     */
    public static Sorted def(){
        return new Sorted();
    }

    /**
     * 默认
     * @return Sorted
     */
    public static Sorted def(String orderBy){
        return new Sorted(orderBy);
    }

    /**
     * 默认倒序
     *
     * @param orderBy 排序字段
     */
    public Sorted(String... orderBy) {
        this.orderBy = orderBy;
        this.orderDesc = 1;
    }


    /**
     * 排序
     *
     * @param orderDesc 正序0--Direction.ASC，反序1--Direction.DESC
     * @param orderBy   排序字段
     */
    public Sorted(Integer orderDesc, String... orderBy) {
        this.orderBy = orderBy;
        this.orderDesc = orderDesc;
    }


    /**
     * 修改排序
     *
     * <p> 当排序为空是修改默认的排序字段
     * <p> 若不为空则不做任何操作
     *
     * @param orderBy  排序字段 （默认排序方式为倒叙）
     */
    public Sorted fixSort(String... orderBy) {
        if (this.orderBy == null) {
            this.orderBy = orderBy;
        }
        return this;
    }

    /**
     * 修改排序
     *
     * <p> 当排序为空是修改默认的排序字段
     * <p> 若不为空则不做任何操作
     *
     * @param orderDesc 排序方式 正序0--Direction.ASC，反序1--Direction.DESC [0-1]
     */
    public Sorted fixSort(Integer orderDesc) {
        if (this.orderDesc == null) {
            this.orderDesc = orderDesc;
        }
        return this;
    }


    /**
     * 修改排序
     *
     * <p> 当排序为空是修改默认的排序字段
     * <p> 若不为空则不做任何操作
     *
     * @param orderDesc 排序方式 正序0--Direction.ASC，反序1--Direction.DESC [0-1]
     * @param orderBy  排序字段 （默认排序方式为倒叙）
     */
    public Sorted fixSort(Integer orderDesc, String... orderBy) {
        if (this.orderBy == null) {
            this.orderBy = orderBy;
        }
        if (this.orderDesc == null) {
            this.orderDesc = orderDesc;
        }
        return this;
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
        return "Sorted{" +
                "orderBy=" + Arrays.toString(orderBy) +
                ", orderDesc=" + orderDesc +
                '}';
    }
}
