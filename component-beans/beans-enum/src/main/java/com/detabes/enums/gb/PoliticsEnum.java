package com.detabes.enums.gb;


import lombok.Getter;

/**
 * 政治面貌
 *
 * @author tn
 * @date 2021-11-12 12:46
 */
@Getter
public enum PoliticsEnum {

    GCD("1", "中国共产党党员"),
    YGCD("2", "中国共产党预备党员"),
    GCQT("3", "中国共产主义青年团团员"),
    GMDGWH("4", "中国国民党革命委员会会员"),
    MZTM("5", "中国民主同盟盟员"),
    MZJG("6", "中国民主建国会会员"),
    MZCJ("7", "中国民主促进会会员"),
    NGD("8", "中国农工民主党党员"),
    ZGD("9", "中国致公党党员"),
    JS("10", "九三学社社员"),
    TZTM("11", "台湾民主自治同盟盟员"),
    WD("12", "无党派民主人士"),
    QZ("13", "群众"),

    ;
    private String code;
    private String desc;

    PoliticsEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static String getCodeByDesc(String desc) {
        try {
            if (CommonUtil.isBlank(desc)) {
                return null;
            }
            PoliticsEnum[] politicsEnums = values();
            for (int i = 0; i < politicsEnums.length; i++) {
                PoliticsEnum politicsEnum = politicsEnums[i];
                if (politicsEnum.getDesc().contains(desc)) {
                    return politicsEnum.getCode();
                }
            }
        } catch (Exception ignored) {
        }
        return null;
    }

    public static String getDescByCode(String code) {
        try {
            if (CommonUtil.isBlank(code)) {
                return null;
            }
            PoliticsEnum[] nationalityEnums = values();
            for (int i = 0; i < nationalityEnums.length; i++) {
                PoliticsEnum politicsEnum = nationalityEnums[i];
                if (politicsEnum.getCode().contains(code)) {
                    return politicsEnum.getDesc();
                }
            }
        } catch (Exception ignored) {
        }
        return null;
    }

}
