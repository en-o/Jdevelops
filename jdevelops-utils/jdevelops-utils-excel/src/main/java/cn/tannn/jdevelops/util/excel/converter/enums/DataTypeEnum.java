package cn.tannn.jdevelops.util.excel.converter.enums;

/**
 * 数据类型枚举: 1[内置]，2[自建]
 *
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @version V1.0
 * @date 2025/7/18 12:02
 */
public enum DataTypeEnum implements ExcelConvertEnum {
    BUILTIN(1, "内置"),
    CUSTOM(2, "自建");

    private final Integer dbValue;
    private final String excelDisplay;

    DataTypeEnum(Integer dbValue, String excelDisplay) {
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
    public static DataTypeEnum getByDbValue(Integer dbValue) {
        if (dbValue == null) {
            return BUILTIN;
        }
        for (DataTypeEnum dataType : values()) {
            if (dataType.getDbValue().equals(dbValue)) {
                return dataType;
            }
        }
        return BUILTIN;
    }

    /**
     * 根据Excel显示值获取枚举
     */
    public static DataTypeEnum getByExcelDisplay(String excelDisplay) {
        if (excelDisplay == null || excelDisplay.trim().isEmpty()) {
            return BUILTIN;
        }
        for (DataTypeEnum dataType : values()) {
            if (dataType.getExcelDisplay().equalsIgnoreCase(excelDisplay.trim())) {
                return dataType;
            }
        }
        return BUILTIN;
    }
}
