package cn.tannn.jdevelops.util.excel.converter.enums;

/**
 * 状态枚举: 0[停用]，1[启用]
 *
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @version V1.0
 * @date 2025/7/18 12:02
 */
public enum StatusEnum implements ExcelConvertEnum {
    DISABLED(0, "停用"),
    ENABLED(1, "启用");

    private final Integer dbValue;
    private final String excelDisplay;

    StatusEnum(Integer dbValue, String excelDisplay) {
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
    public static StatusEnum getByDbValue(Integer dbValue) {
        if (dbValue == null) {
            return DISABLED;
        }
        for (StatusEnum status : values()) {
            if (status.getDbValue().equals(dbValue)) {
                return status;
            }
        }
        return DISABLED;
    }

    /**
     * 根据Excel显示值获取枚举
     */
    public static StatusEnum getByExcelDisplay(String excelDisplay) {
        if (excelDisplay == null || excelDisplay.trim().isEmpty()) {
            return DISABLED;
        }
        for (StatusEnum status : values()) {
            if (status.getExcelDisplay().equalsIgnoreCase(excelDisplay.trim())) {
                return status;
            }
        }
        return DISABLED;
    }
}
