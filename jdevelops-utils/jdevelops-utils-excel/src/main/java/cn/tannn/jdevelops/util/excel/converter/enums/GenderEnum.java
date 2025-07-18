package cn.tannn.jdevelops.util.excel.converter.enums;

/**
 * 性别枚举: 0[未知]，1[男性]，2[女性]
 *
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @version V1.0
 * @date 2025/7/18 12:02
 */
public enum GenderEnum implements ExcelConvertEnum {
    UNKNOWN(0, "未知"),
    MALE(1, "男性"),
    FEMALE(2, "女性");

    private final Integer dbValue;
    private final String excelDisplay;

    GenderEnum(Integer dbValue, String excelDisplay) {
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
    public static GenderEnum getByDbValue(Integer dbValue) {
        if (dbValue == null) {
            return UNKNOWN;
        }
        for (GenderEnum gender : values()) {
            if (gender.getDbValue().equals(dbValue)) {
                return gender;
            }
        }
        return UNKNOWN;
    }

    /**
     * 根据Excel显示值获取枚举
     */
    public static GenderEnum getByExcelDisplay(String excelDisplay) {
        if (excelDisplay == null || excelDisplay.trim().isEmpty()) {
            return UNKNOWN;
        }
        for (GenderEnum gender : values()) {
            if (gender.getExcelDisplay().equalsIgnoreCase(excelDisplay.trim())) {
                return gender;
            }
        }
        // 兼容简化的男女写法
        if ("男".equalsIgnoreCase(excelDisplay.trim())) {
            return MALE;
        }
        if ("女".equalsIgnoreCase(excelDisplay.trim())) {
            return FEMALE;
        }
        return UNKNOWN;
    }
}
