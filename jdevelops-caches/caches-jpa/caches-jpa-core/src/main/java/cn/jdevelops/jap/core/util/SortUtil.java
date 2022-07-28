package cn.jdevelops.jap.core.util;

import cn.jdevelops.map.core.bean.ColumnUtil;
import cn.jdevelops.result.response.SortVO;
import org.springframework.data.domain.Sort;

import java.util.Objects;

/**
 * 排序相关工具
 *
 * @author lsc
 * @date 2022/7/4  11:59
 */
public class SortUtil {
    /**
     * 传入排序字段构建Sort
     * ps， sortVO.OrderDesc 存在则以 OrderDesc为准
     * @param sortVO    排序实体类
     * @param fieldName 排序字段
     * @return com.detabes.result.response.SortVO
     * @author lsc
     * @date 2022/7/4 12:22
     */
    public static <T> SortVO getSort(SortVO sortVO, ColumnUtil.SFunction<T, ?> fieldName) {
        String fieldName1 = ColumnUtil.getFieldName(fieldName);
        if (sortVO == null) {
            sortVO = new SortVO();
            sortVO.setOrderBy(fieldName1);
            sortVO.setOrderDesc(0);
        } else  {
            if(Objects.isNull(sortVO.getOrderBy())){
                if (Objects.isNull(fieldName1)) {
                    fieldName1 = "id";
                }
                sortVO.setOrderBy(fieldName1);
            }
            if(Objects.isNull(sortVO.getOrderDesc())){
                sortVO.setOrderDesc(0);
            }
        }
        return sortVO;
    }

    /**
     * 未传入排序字段构建Sort
     *
     * @param sortVO 排序实体类
     * @return com.detabes.result.response.SortVO
     * @author lsc
     * @date 2022/7/4 12:24
     */
    public static SortVO getSort(SortVO sortVO) {
        if (sortVO == null) {
            sortVO = new SortVO();
            sortVO.setOrderBy("id");
            sortVO.setOrderDesc(0);
        } else {
            if(Objects.isNull(sortVO.getOrderBy())){
                sortVO.setOrderBy("id");
            }
            if(Objects.isNull(sortVO.getOrderDesc())){
                sortVO.setOrderDesc(0);
            }
        }
        return sortVO;
    }

    /**
     * 获取jpa 的 sort
     * @param direction 排序
     * @param fieldName 排序字段
     * @return org.springframework.data.domain.Sort
     * @param <T>  排序字段的实体
     */
    public static <T> Sort sort(Sort.Direction direction , ColumnUtil.SFunction<T, ?> fieldName){
        String sortField = ColumnUtil.getFieldName(fieldName);
        if(Objects.nonNull(sortField)){
            return Sort.by(direction,sortField);
        }else {
            return Sort.by(direction);
        }
    }
}
