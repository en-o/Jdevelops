package cn.tannn.jdevelops.util.excel.converter;

import cn.tannn.jdevelops.util.excel.converter.enums.LeaveStateEnum;

/**
 * 请假状态转换器
 *
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @version V1.0
 * @date 2025/7/18 12:02
 */
public class LeaveStateConverter extends GenericExcelConverter {
    public LeaveStateConverter() {
        super(LeaveStateEnum.class);
    }
}
