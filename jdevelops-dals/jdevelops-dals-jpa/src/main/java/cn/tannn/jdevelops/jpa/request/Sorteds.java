package cn.tannn.jdevelops.jpa.request;

import cn.tannn.jdevelops.result.request.Sorted;
import org.springframework.data.domain.Sort;

import java.util.Objects;

/**
 * 排序 jpa 扩展
 *
 * @author <a href="https://tannn.cn/">tnnn</a>
 * @version V1.0
 * @date 2024/5/12 下午12:19
 */
public class Sorteds extends Sorted {

    public static Sorteds defs(){
        return new Sorteds();
    }

    /**
     * Sorted to Sort
     *
     * @return Sort
     */
    public Sort sort() {
        return Sort.by(direction(getOrderDesc()), getOrderBy());
    }

    /**
     * Sorted to Sort
     *
     * @param sort {@link Sorted}
     * @return Sort
     */
    public static Sort sort(Sorteds sort) {
        if (sort == null) {
            sort = Sorteds.defs();
        }
        return sort.sort();
    }


    /**
     * Sorted to Sort
     *
     * @param sort {@link Sorted}
     * @return Sort
     */
    public static Sort sort(Sorted sort) {
        if (sort == null) {
            sort = Sorted.def();
        }
        return Sort.by(direction(sort.getOrderDesc()), sort.getOrderBy());
    }

    /**
     * Sorted#getOrderDesc() -> Sort.Direction
     *
     * @param orderDesc {@link Sorted#getOrderDesc()}
     * @return Sort.Direction
     */
    public static Sort.Direction direction(Integer orderDesc) {
        if (!Objects.isNull(orderDesc) && 0 == orderDesc) {
            return Sort.Direction.ASC;
        }
        return Sort.Direction.DESC;
    }
}
