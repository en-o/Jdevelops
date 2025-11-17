package cn.tannn.jdevelops.util.jpa.request;

import cn.tannn.jdevelops.result.request.Sorted;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Objects;

/**
 * 排序 jpa 扩展
 *
 * @author <a href="https://t.tannn.cn/">tnnn</a>
 * @version V1.0
 * @date 2024/5/12 下午12:19
 */
public class Sorteds extends Sorted {

    public Sorteds() {
    }

    public Sorteds(String... orderBy) {
        super(orderBy);
    }

    public Sorteds(Integer orderDesc, String... orderBy) {
        super(orderDesc, orderBy);
    }

    @Override
    public Sorteds fixSort(String... orderBy) {
        return sorted(super.fixSort(orderBy));
    }

    @Override
    public Sorteds fixSort(Integer orderDesc) {
        return sorted(super.fixSort(orderDesc));
    }

    @Override
    public Sorteds fixSort(Integer orderDesc, String... orderBy) {
        return sorted(super.fixSort(orderDesc, orderBy));
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
     *  fix 内用
     * @param sorted Sorted
     * @return Sorteds
     */
    public Sorteds sorted(Sorted sorted) {
        setOrderBy(sorted.getOrderBy());
        setOrderDesc(sorted.getOrderDesc());
        return this;
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
