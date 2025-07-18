package cn.tannn.jdevelops.util.excel.converter.enums;

/**
 * 组织类型枚举: 0[单位]，1[部门]，2[岗位]
 *
 * @author <a href="https://t.tannn.cn/">tan</a>
 * @version V1.0
 * @date 2025/7/18 12:02
 */
public enum OrgTypeEnum implements ExcelConvertEnum {
    UNIT(0, "单位"),
    DEPARTMENT(1, "部门"),
    POSITION(2, "岗位");

    private final Integer dbValue;
    private final String excelDisplay;

    OrgTypeEnum(Integer dbValue, String excelDisplay) {
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
    public static OrgTypeEnum getByDbValue(Integer dbValue) {
        if (dbValue == null) {
            return UNIT;
        }
        for (OrgTypeEnum orgType : values()) {
            if (orgType.getDbValue().equals(dbValue)) {
                return orgType;
            }
        }
        return UNIT;
    }

    /**
     * 根据Excel显示值获取枚举
     */
    public static OrgTypeEnum getByExcelDisplay(String excelDisplay) {
        if (excelDisplay == null || excelDisplay.trim().isEmpty()) {
            return UNIT;
        }
        for (OrgTypeEnum orgType : values()) {
            if (orgType.getExcelDisplay().equalsIgnoreCase(excelDisplay.trim())) {
                return orgType;
            }
        }
        return UNIT;
    }
}
