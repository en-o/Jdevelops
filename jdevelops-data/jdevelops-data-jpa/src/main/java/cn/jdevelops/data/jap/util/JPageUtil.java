package cn.jdevelops.data.jap.util;

import cn.jdevelops.result.bean.SerializableBean;
import cn.jdevelops.result.request.PageDTO;
import cn.jdevelops.result.request.SortDTO;
import cn.jdevelops.result.request.SortPageDTO;
import cn.jdevelops.result.response.PageResult;
import cn.jdevelops.result.util.ListTo;
import cn.jdevelops.result.util.PageUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

/**
 * 分页相关
 *
 * @author tn
 * @version 1
 * @date 2021/1/26 23:55
 */
public class JPageUtil {
    /**
     * SortVO  转成 Sort
     *
     * @param page page
     * @return Sort
     */
    public static Sort getSv2S(SortDTO page) {
        if (page == null) {
            return Sort.by(Sort.Direction.DESC, "id");
        } else {
            return getOrders(page.getOrderDesc(), page.getOrderBy());
        }
    }


    /**
     * RoutinePageDTO  转成 Sort
     *
     * @param sort sort
     * @return Sort
     */
    public static Sort getSv2S(SortPageDTO sort) {
        if (StringUtils.isBlank(sort.getOrderBy()) && sort.getOrderDesc() == null) {
            return Sort.by(Sort.Direction.DESC, "id");
        } else {
            return getOrders(sort.getOrderDesc(), sort.getOrderBy());
        }
    }

    @NotNull
    private static Sort getOrders(Integer orderDesc, String orderBy) {
        if (!Objects.isNull(orderDesc) && 0 == orderDesc) {
            return Sort.by(Sort.Direction.ASC,
                    StringUtils.isNotBlank(orderBy) ? orderBy : "id");
        }
        return Sort.by(Sort.Direction.DESC,
                StringUtils.isNotBlank(orderBy) ? orderBy : "id");
    }

    /**
     * 获取分页 Pageable
     *
     * @param page 分页
     * @param sort 排序
     * @return Pageable
     */
    public static Pageable getPageable(PageDTO page, SortDTO sort) {
        PageDTO pageVoDef = PageUtil.pageDef(page);
        return PageRequest.of(pageVoDef.getPageIndex(),
                pageVoDef.getPageSize(),
                getSv2S(sort));
    }

    /**
     * 获取分页 Pageable
     *
     * @param page 分页
     * @param sort 排序Sort
     * @return Pageable
     */
    public static Pageable getPageable(PageDTO page, Sort sort) {
        PageDTO pageVoDef = PageUtil.pageDef(page);
        return PageRequest.of(pageVoDef.getPageIndex(),
                pageVoDef.getPageSize(),
                sort);
    }


    /**
     * 获取分页 Pageable
     * @param page 分页
     * @return Pageable
     */
    public static Pageable getPageable(PageDTO page) {
        PageDTO pageVoDef = PageUtil.pageDef(page);
        return PageRequest.of(pageVoDef.getPageIndex(),
                pageVoDef.getPageSize());
    }


    /**
     * 获取分页 Pageable
     *
     * @param sortPage 分页 排序
     * @return Pageable
     */
    public static Pageable getPageable(SortPageDTO sortPage) {
        SortPageDTO pageDef = PageUtil.sortPageDef(sortPage);
        return PageRequest.of(pageDef.getPageIndex(),
                pageDef.getPageSize(),
                getSv2S(pageDef));
    }


    /**
     * page
     */
    public static <R, S extends SerializableBean> PageResult<R> to(Page<S> page, Class<R> clazz) {
        if (page != null && !page.isEmpty()) {
            List<S> content = page.getContent();
            List<R> result = ListTo.to(clazz, content);
            return PageResult.page(page.getNumber(),
                    page.getSize(),
                    page.getTotalPages(),
                    page.getTotalElements(),
                    result);
        } else {
            return PageResult.page(1,
                    20,
                    0,
                    0L,
                    null);
        }
    }

}
