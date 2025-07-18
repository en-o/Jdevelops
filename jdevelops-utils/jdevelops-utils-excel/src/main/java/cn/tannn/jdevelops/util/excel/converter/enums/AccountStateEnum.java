package cn.tannn.jdevelops.util.excel.converter.enums;

/**
 * 账号状态枚举: 1[正常]，2[锁定]，3[回收站-逻辑删除]
 *
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @version V1.0
 * @date 2025/7/18 12:02
 */
public enum AccountStateEnum implements ExcelConvertEnum {
    NORMAL(1, "正常"),
    LOCKED(2, "锁定"),
    DELETED(3, "回收站");

    private final Integer dbValue;
    private final String excelDisplay;

    AccountStateEnum(Integer dbValue, String excelDisplay) {
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
    public static AccountStateEnum getByDbValue(Integer dbValue) {
        if (dbValue == null) {
            return NORMAL;
        }
        for (AccountStateEnum state : values()) {
            if (state.getDbValue().equals(dbValue)) {
                return state;
            }
        }
        return NORMAL;
    }

    /**
     * 根据Excel显示值获取枚举
     */
    public static AccountStateEnum getByExcelDisplay(String excelDisplay) {
        if (excelDisplay == null || excelDisplay.trim().isEmpty()) {
            return NORMAL;
        }
        for (AccountStateEnum state : values()) {
            if (state.getExcelDisplay().equalsIgnoreCase(excelDisplay.trim())) {
                return state;
            }
        }
        return NORMAL;
    }
}
