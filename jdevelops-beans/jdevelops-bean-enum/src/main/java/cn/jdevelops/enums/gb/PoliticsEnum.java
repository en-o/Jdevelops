package cn.jdevelops.enums.gb;



/**
 * 政治面貌
 *
 * @author tn
 * @date 2021-11-12 12:46
 */
public enum PoliticsEnum {
    /* 中国共产党党员 */
    GCD("01", "中国共产党党员"),
    /* 中国共产党预备党员 */
    YGCD("02", "中国共产党预备党员"),
    /* 中国共产主义青年团团员 */
    GCQT("03", "中国共产主义青年团团员"),
    /* 中国国民党革命委员会会员 */
    GMDGWH("04", "中国国民党革命委员会会员"),
    /* 中国民主同盟盟员 */
    MZTM("05", "中国民主同盟盟员"),
    /* 中国民主建国会会员 */
    MZJG("06", "中国民主建国会会员"),
    /* 中国民主促进会会员 */
    MZCJ("07", "中国民主促进会会员"),
    /* 中国农工民主党党员 */
    NGD("08", "中国农工民主党党员"),
    /* 中国致公党党员 */
    ZGD("09", "中国致公党党员"),
    /* 九三学社社员 */
    JS("10", "九三学社社员"),
    /* 台湾民主自治同盟盟员 */
    TZTM("11", "台湾民主自治同盟盟员"),
    /* 无党派民主人士 */
    WD("12", "无党派民主人士"),
    /* 群众 */
    QZ("13", "群众"),

    ;
    /**
     * 代码
     */
    private final String code;
    /**
     * 注释
     */
    private final String desc;

    PoliticsEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static String getCodeByDesc(String desc) {
        if (CommonUtil.isBlank(desc)) {
            return null;
        }
        PoliticsEnum[] politicsEnums = values();
        for (PoliticsEnum politicsEnum : politicsEnums) {
            if (politicsEnum.getDesc().contains(desc)) {
                return politicsEnum.getCode();
            }
        }
        return null;
    }

    public static String getDescByCode(String code) {
        if (CommonUtil.isBlank(code)) {
            return null;
        }
        PoliticsEnum[] nationalityEnums = values();
        for (PoliticsEnum politicsEnum : nationalityEnums) {
            if (politicsEnum.getCode().contains(code)) {
                return politicsEnum.getDesc();
            }
        }
        return null;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
