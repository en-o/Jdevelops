package cn.jdevelop.jap.core.util;

import cn.jdevelop.entity.basics.vo.SerializableVO;
import cn.jdevelop.result.page.ResourcePage;
import cn.jdevelop.result.response.PageVO;
import cn.jdevelop.result.response.RoutinePageDTO;
import cn.jdevelop.result.response.SortVO;
import cn.jdevelop.result.util.PageUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
     * @param sortVO SortVO
     * @return Sort
     */
    public static Sort getSv2S(SortVO sortVO) {
        if (sortVO == null) {
            return Sort.by(Sort.Direction.DESC, "id");
        } else {
            if (sortVO.getOrderDesc().equals(0)) {
                return Sort.by(Sort.Direction.ASC,
                        StringUtils.isNotBlank(sortVO.getOrderBy()) ? sortVO.getOrderBy() : "id");
            }
            return Sort.by(Sort.Direction.DESC,
                    StringUtils.isNotBlank(sortVO.getOrderBy()) ? sortVO.getOrderBy() : "id");
        }
    }


    /**
     * RoutinePageDTO  转成 Sort
     *
     * @param sort sort
     * @return Sort
     */
    public static Sort getSv2S(RoutinePageDTO sort) {
        if (StringUtils.isBlank(sort.getOrderBy()) && sort.getOrderDesc() == null) {
            return Sort.by(Sort.Direction.DESC, "id");
        } else {
            if (sort.getOrderDesc().equals(0)) {
                return Sort.by(Sort.Direction.ASC,
                        StringUtils.isNotBlank(sort.getOrderBy()) ? sort.getOrderBy() : "id");
            }
            return Sort.by(Sort.Direction.DESC,
                    StringUtils.isNotBlank(sort.getOrderBy()) ? sort.getOrderBy() : "id");
        }
    }

    /**
     * 获取分页 Pageable
     *
     * @param pageVO 分页
     * @param sortVO 排序
     * @return Pageable
     */
    public static Pageable getPageable(PageVO pageVO, SortVO sortVO) {
        PageVO pageVoDef = PageUtil.setNullPageVoDef(pageVO);
        return PageRequest.of(pageVoDef.getPageIndex(),
                pageVoDef.getPageSize(),
                getSv2S(sortVO));
    }


    /**
     * 获取分页 Pageable
     *
     * @param pageDTO 分页 排序
     * @return Pageable
     */
    public static Pageable getPageable(RoutinePageDTO pageDTO) {
        RoutinePageDTO pageDTODef = PageUtil.setNullRoutinePageDTODef(pageDTO);
        return PageRequest.of(pageDTODef.getPageIndex(),
                pageDTODef.getPageSize(),
                getSv2S(pageDTODef));
    }


    /**
     * page
     */
    public static <T, S extends SerializableVO> ResourcePage<List<T>> to(Page<S> page, Class<T> clazz) {
        if (page != null && !page.isEmpty()) {
            List<S> content = page.getContent();

            List<T> result = new ArrayList(content.size());

            Iterator var3 = content.iterator();

            while (var3.hasNext()) {
                SerializableVO abs = (SerializableVO) var3.next();
                result.add((T) abs.to(clazz));
            }

            return ResourcePage.page(page.getNumber(),
                    page.getSize(),
                    page.getTotalPages(),
                    page.getTotalElements(),
                    result);
        } else {
            return ResourcePage.page(1,
                    page.getPageable().getPageSize(),
                    0,
                    0L,
                    null);
        }
    }

}
