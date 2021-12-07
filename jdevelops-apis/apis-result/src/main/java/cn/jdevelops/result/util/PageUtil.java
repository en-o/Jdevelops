package cn.jdevelops.result.util;

import cn.jdevelops.result.response.PageVO;
import cn.jdevelops.result.response.RoutinePageDTO;

/**
 * 分页相关
 *
 * @author tn
 * @version 1
 * @date 2021/1/26 23:55
 */
public class PageUtil {

    /**
     * PageVO 参数为空是设置默认值 和 index-1(Page index 从0开始)
     *
     * @param pageVO pageVO
     * @return PageVO
     */
    public static PageVO setNullPageVoDef(PageVO pageVO) {
        if (pageVO == null) {
            pageVO = new PageVO();
            pageVO.setPageIndex(0);
            pageVO.setPageSize(20);
        } else {
            if (pageVO.getPageIndex() == null) {
                pageVO.setPageIndex(0);
            } else {
                pageVO.setPageIndex(pageVO.getPageIndex() - 1);
            }
            if (pageVO.getPageSize() == null) {
                pageVO.setPageSize(20);
            }
        }

        return pageVO;
    }


    /**
     * PageVO 参数为空是设置默认值 和 index-1(Page index 从0开始)
     *
     * @param pageDTO pageDTO
     * @return PageVO
     */
    public static RoutinePageDTO setNullRoutinePageDTODef(RoutinePageDTO pageDTO) {
        if (pageDTO == null) {
            pageDTO = new RoutinePageDTO();
            pageDTO.setPageIndex(0);
            pageDTO.setPageSize(20);
            pageDTO.setOrderBy("id");
            pageDTO.setOrderDesc(0);
        } else {
            if (pageDTO.getPageIndex() == null) {
                pageDTO.setPageIndex(0);
            } else {
                pageDTO.setPageIndex(pageDTO.getPageIndex() - 1);
            }
            if (pageDTO.getPageSize() == null) {
                pageDTO.setPageSize(20);
            }
        }
        return pageDTO;
    }

}
