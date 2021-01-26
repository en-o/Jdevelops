package com.detabes.result.util;

import com.detabes.result.response.PageVO;
import com.detabes.result.response.SortVO;

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

}
