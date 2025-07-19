package cn.tannn.jdevelops.util.excel.converter.enums;

/**
 * 账号可用状态枚举: -1[待激活]，0[已激活]，1[审核中]，2[审核通过]，3[审核不通过]
 *
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @version V1.0
 * @date 2025/7/18 12:02
 */
public enum AccountAvailableEnum implements ExcelConvertEnum {
    PENDING_ACTIVATION(-1, "待激活"),
    ACTIVATED(0, "已激活"),
    UNDER_REVIEW(1, "审核中"),
    APPROVED(2, "审核通过"),
    REJECTED(3, "审核不通过");

    private final Integer dbValue;
    private final String excelDisplay;

    AccountAvailableEnum(Integer dbValue, String excelDisplay) {
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
    public static AccountAvailableEnum getByDbValue(Integer dbValue) {
        if (dbValue == null) {
            return UNDER_REVIEW;
        }
        for (AccountAvailableEnum available : values()) {
            if (available.getDbValue().equals(dbValue)) {
                return available;
            }
        }
        return UNDER_REVIEW;
    }

    /**
     * 根据Excel显示值获取枚举
     */
    public static AccountAvailableEnum getByExcelDisplay(String excelDisplay) {
        if (excelDisplay == null || excelDisplay.trim().isEmpty()) {
            return UNDER_REVIEW;
        }
        for (AccountAvailableEnum available : values()) {
            if (available.getExcelDisplay().equalsIgnoreCase(excelDisplay.trim())) {
                return available;
            }
        }
        return UNDER_REVIEW;
    }
}
