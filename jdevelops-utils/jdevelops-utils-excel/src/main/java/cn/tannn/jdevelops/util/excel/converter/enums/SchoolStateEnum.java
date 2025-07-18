package cn.tannn.jdevelops.util.excel.converter.enums;

/**
 * 学校状态枚举: 0[校外]，1[本校]
 *
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @version V1.0
 * @date 2025/7/18 12:02
 */
public enum SchoolStateEnum implements ExcelConvertEnum {
    OUTSIDE(0, "校外"),
    INSIDE(1, "本校");

    private final Integer dbValue;
    private final String excelDisplay;

    SchoolStateEnum(Integer dbValue, String excelDisplay) {
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
    public static SchoolStateEnum getByDbValue(Integer dbValue) {
        if (dbValue == null) {
            return INSIDE;
        }
        for (SchoolStateEnum state : values()) {
            if (state.getDbValue().equals(dbValue)) {
                return state;
            }
        }
        return INSIDE;
    }

    /**
     * 根据Excel显示值获取枚举
     */
    public static SchoolStateEnum getByExcelDisplay(String excelDisplay) {
        if (excelDisplay == null || excelDisplay.trim().isEmpty()) {
            return INSIDE;
        }
        for (SchoolStateEnum state : values()) {
            if (state.getExcelDisplay().equalsIgnoreCase(excelDisplay.trim())) {
                return state;
            }
        }
        return INSIDE;
    }
}
