package cn.tannn.jdevelops.util.excel.converter.enums;

/**
 * 是否枚举: 0[否]，1[是]
 *
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @version V1.0
 * @date 2025/7/18 12:02
 */
public enum WhetherEnum implements ExcelConvertEnum {
    NO(0, "否"),
    YES(1, "是");

    private final Integer dbValue;
    private final String excelDisplay;

    WhetherEnum(Integer dbValue, String excelDisplay) {
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
    public static WhetherEnum getByDbValue(Integer dbValue) {
        if (dbValue == null) {
            return NO;
        }
        for (WhetherEnum whether : values()) {
            if (whether.getDbValue().equals(dbValue)) {
                return whether;
            }
        }
        return NO;
    }

    /**
     * 根据Excel显示值获取枚举
     */
    public static WhetherEnum getByExcelDisplay(String excelDisplay) {
        if (excelDisplay == null || excelDisplay.trim().isEmpty()) {
            return NO;
        }
        for (WhetherEnum whether : values()) {
            if (whether.getExcelDisplay().equalsIgnoreCase(excelDisplay.trim())) {
                return whether;
            }
        }
        return NO;
    }
}
