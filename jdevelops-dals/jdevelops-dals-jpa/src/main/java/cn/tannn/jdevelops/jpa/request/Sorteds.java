package cn.tannn.jdevelops.jpa.request;

import cn.tannn.jdevelops.result.request.Sorted;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Objects;

/**
 * 排序 jpa 扩展
 *
 * @author <a href="https://tannn.cn/">tnnn</a>
 * @version V1.0
 * @date 2024/5/12 下午12:19
 */
public class Sorteds extends Sorted {


    /**
     * Sorted to Sort
     *
     * @return Sort
     */
    public Sort sort() {
        return Sort.by(direction(getOrderDesc()), getOrderBy());
    }


    public static Sorteds defs(){
        return new Sorteds();
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
     * Sorteds  转成 Sort
     * @param sort SortDTO
     * @return Sort
     */
    public static Sort sorteds2Sort(List<Sorteds> sort) {
        return sort.stream()
                .map(s -> {
                    // （正序0，反序1）
                    if (s.getOrderDesc() == 0) {
                        return Sort.by(s.getOrderBy()).ascending();
                    }else {
                        return Sort.by(s.getOrderBy()).descending();
                    }
                })
                .reduce(Sort::and).orElse(null);
    }

    /**
     * Sorteds  转成 Sort
     * @param sort SortDTO
     * @return Sort
     */
    public static Sort sorteds2Sort2(List<Sorted> sort) {
        return sort.stream()
                .map(s -> {
                    // （正序0，反序1）
                    if (s.getOrderDesc() == 0) {
                        return Sort.by(s.getOrderBy()).ascending();
                    }else {
                        return Sort.by(s.getOrderBy()).descending();
                    }
                })
                .reduce(Sort::and).orElse(null);
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
