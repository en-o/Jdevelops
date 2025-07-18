package cn.tannn.jdevelops.util.excel.converter.enums;


/**
 * 请假状态枚举: 0[请假]，1[正常]
 *
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @version V1.0
 * @date 2025/7/18 12:02
 */
public enum LeaveStateEnum implements ExcelConvertEnum {
    LEAVE(0, "请假"),
    NORMAL(1, "正常");

    private final Integer dbValue;
    private final String excelDisplay;

    LeaveStateEnum(Integer dbValue, String excelDisplay) {
        this.dbValue = dbValue;
        this.excelDisplay = excelDisplay;
    }

    @Override
    public String getExcelDisplay() {
        return excelDisplay;
    }

    @Override
    public Integer getDbValue() {
        return dbValue;
    }

    /**
     * 根据数据库值获取枚举
     */
    public static LeaveStateEnum getByDbValue(Integer dbValue) {
        if (dbValue == null) {
            return NORMAL;
        }
        for (LeaveStateEnum state : values()) {
            if (state.getDbValue().equals(dbValue)) {
                return state;
            }
        }
        return NORMAL;
    }

    /**
     * 根据Excel显示值获取枚举
     */
    public static LeaveStateEnum getByExcelDisplay(String excelDisplay) {
        if (excelDisplay == null || excelDisplay.trim().isEmpty()) {
            return NORMAL;
        }
        for (LeaveStateEnum state : values()) {
            if (state.getExcelDisplay().equalsIgnoreCase(excelDisplay.trim())) {
                return state;
            }
        }
        return NORMAL;
    }
}
