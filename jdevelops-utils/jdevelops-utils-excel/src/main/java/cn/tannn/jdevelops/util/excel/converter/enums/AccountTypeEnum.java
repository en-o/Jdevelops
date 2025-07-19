package cn.tannn.jdevelops.util.excel.converter.enums;

/**
 * 账号类型枚举: 0[内置]，1[添加]，2[注册]，3[同步]
 *
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @version V1.0
 * @date 2025/7/18 12:02
 */
public enum AccountTypeEnum implements ExcelConvertEnum {
    BUILTIN(0, "内置"),
    ADDED(1, "添加"),
    REGISTERED(2, "注册"),
    SYNCED(3, "同步");

    private final Integer dbValue;
    private final String excelDisplay;

    AccountTypeEnum(Integer dbValue, String excelDisplay) {
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
    public static AccountTypeEnum getByDbValue(Integer dbValue) {
        if (dbValue == null) {
            return BUILTIN;
        }
        for (AccountTypeEnum type : values()) {
            if (type.getDbValue().equals(dbValue)) {
                return type;
            }
        }
        return BUILTIN;
    }

    /**
     * 根据Excel显示值获取枚举
     */
    public static AccountTypeEnum getByExcelDisplay(String excelDisplay) {
        if (excelDisplay == null || excelDisplay.trim().isEmpty()) {
            return BUILTIN;
        }
        for (AccountTypeEnum type : values()) {
            if (type.getExcelDisplay().equalsIgnoreCase(excelDisplay.trim())) {
                return type;
            }
        }
        return BUILTIN;
    }
}
