package cn.tannn.jdevelops.util.excel;

import cn.tannn.jdevelops.util.excel.handler.CellMenu;
import cn.tannn.jdevelops.util.excel.model.HeaderMenuData;

import java.util.Arrays;
import java.util.List;

/**
 * excel相关功能
 *
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @version V1.0
 * @date 2024/10/21 下午3:28
 */
public class ExcelService {

    /**
     * 请假状态
     */
    public static List<String> LEAVE_STATE = Arrays.asList("请假","正常");
    /**
     * 用户类型
     */
    public static List<String> SCHOOL_STATE =  Arrays.asList("本校","校外");


    /**
     * 下拉数据
     * <code>
           <p> EasyExcelFactory.write(responseHeader.getOutputStream(), Bean.class)
           <p> .registerWriteHandler(ExcelService.dropDownListLeaveState(6,LEAVE_STATE))
     * </code>
     * @param index  需要下拉数据的表头下标
     * @param menuItems 列表数据
     * @return CellMenu
     */
    public static CellMenu dropDownList(Integer index,List<String> menuItems) {
        // 设置下拉
        HeaderMenuData headerMenuData = new HeaderMenuData(index, menuItems );
        return new CellMenu(headerMenuData);
    }


}
