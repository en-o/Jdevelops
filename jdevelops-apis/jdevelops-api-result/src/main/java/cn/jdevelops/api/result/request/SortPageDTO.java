package cn.jdevelops.api.result.request;


import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;

/**
 * 分页排序组合
 *
 * @author tn
 * @date 2021-01-25 13:59
 */
@Schema(description = "公共分页参数")
public class SortPageDTO extends PageDTO {


    @Valid
    List<SortDTO> sorts;

    public SortPageDTO() {
    }



    /**
     * 分页排序
     * @param pageSize 数量
     */
    public SortPageDTO(Integer pageSize) {
        super(pageSize);
    }


    /**
     * 分页排序
     * @param pageSize 数量
     * @param sorts 排序
     */
    public SortPageDTO(Integer pageSize, List<SortDTO> sorts) {
        super(pageSize);
        this.sorts = sorts;
    }


    /**
     * 分页排序
     * @param pageSize 数量
     * @param sorts 排序
     */
    public SortPageDTO(Integer pageSize, SortDTO sorts) {
        super(pageSize);
        this.sorts = Collections.singletonList(sorts);
    }


    /**
     * 分页排序
     * @param sorts 排序
     */
    public SortPageDTO(List<SortDTO> sorts) {
        this.sorts = sorts;
    }


    /**
     * 分页排序
     * @param pageIndex 页码
     * @param pageSize 数量
     */
    public SortPageDTO(Integer pageIndex, Integer pageSize) {
        super(pageIndex, pageSize);
    }


    /**
     * 分页排序
     * @param pageIndex 页码
     * @param pageSize 数量
     * @param sorts 排序
     */
    public SortPageDTO(Integer pageIndex, Integer pageSize, List<SortDTO> sorts) {
        super(pageIndex, pageSize);
        this.sorts = sorts;
    }


    /**
     * 分页排序
     * @param pageIndex 页码
     * @param pageSize 数量
     * @param orderDesc 排序方式
     * @param orderBy 排序字段
     */
    public SortPageDTO(Integer pageIndex, Integer pageSize, Integer orderDesc, String... orderBy) {
        super(pageIndex, pageSize);
        this.sorts = Collections.singletonList(new SortDTO(orderDesc,  orderBy));
    }


    /**
     * 分页排序
     * @param pageIndex 页码
     * @param pageSize 数量
     * @param sorts 排序
     */
    public SortPageDTO(Integer pageIndex, Integer pageSize, SortDTO sorts) {
        super(pageIndex, pageSize);
        this.sorts = Collections.singletonList(sorts);
    }


    public List<SortDTO> getSorts() {
        if(null == sorts || sorts.isEmpty()){
            return Collections.singletonList(new SortDTO());
        }else {
            return sorts;
        }
    }

    public void setSorts(List<SortDTO> sorts) {
        this.sorts = sorts;
    }




    @Override
    public String toString() {
        return "SortPageDTO{" +
                "sorts=" + sorts +
                '}';
    }
}
