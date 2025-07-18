package cn.tannn.jdevelops.util.excel.converter.enums;

/**
 * 有无枚举: 0[无]，1[有]
 *
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @version V1.0
 * @date 2025/7/18 12:02
 */
public enum YouWuEnum implements ExcelConvertEnum {
    WU(0, "无"),
    YOU(1, "有");

    private final Integer dbValue;
    private final String excelDisplay;

    YouWuEnum(Integer dbValue, String excelDisplay) {
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
    public static YouWuEnum getByDbValue(Integer dbValue) {
        if (dbValue == null) {
            return WU;
        }
        for (YouWuEnum youWu : values()) {
            if (youWu.getDbValue().equals(dbValue)) {
                return youWu;
            }
        }
        return WU;
    }

    /**
     * 根据Excel显示值获取枚举
     */
    public static YouWuEnum getByExcelDisplay(String excelDisplay) {
        if (excelDisplay == null || excelDisplay.trim().isEmpty()) {
            return WU;
        }
        for (YouWuEnum youWu : values()) {
            if (youWu.getExcelDisplay().equalsIgnoreCase(excelDisplay.trim())) {
                return youWu;
            }
        }
        return WU;
    }
}
