package cn.tannn.jdevelops.util.excel.converter.enums;

/**
 * 系统配置枚举: 1[系统基础信息]，2[系统备案信息]，3[忘记密码提示语]，4[网站变灰]，5[系统默认值]
 *
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @version V1.0
 * @date 2025/7/18 12:02
 */
public enum SysConfigEnum implements ExcelConvertEnum {
    BASIC_INFO(1, "系统基础信息"),
    RECORD_INFO(2, "系统备案信息"),
    PASSWORD_HINT(3, "忘记密码提示语"),
    WEBSITE_GRAY(4, "网站变灰"),
    DEFAULT_VALUE(5, "系统默认值");

    private final Integer dbValue;
    private final String excelDisplay;

    SysConfigEnum(Integer dbValue, String excelDisplay) {
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
    public static SysConfigEnum getByDbValue(Integer dbValue) {
        if (dbValue == null) {
            return BASIC_INFO;
        }
        for (SysConfigEnum sysConfig : values()) {
            if (sysConfig.getDbValue().equals(dbValue)) {
                return sysConfig;
            }
        }
        return BASIC_INFO;
    }

    /**
     * 根据Excel显示值获取枚举
     */
    public static SysConfigEnum getByExcelDisplay(String excelDisplay) {
        if (excelDisplay == null || excelDisplay.trim().isEmpty()) {
            return BASIC_INFO;
        }
        for (SysConfigEnum sysConfig : values()) {
            if (sysConfig.getExcelDisplay().equalsIgnoreCase(excelDisplay.trim())) {
                return sysConfig;
            }
        }
        return BASIC_INFO;
    }
}
