package cn.jdevelops.enums.gb;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 民族
 *
 * @author tnnn
 * <a href="https://blog.csdn.net/zona_wzq/article/details/103207065">来源</a>
 */
public enum NationalityEnum {


    /* 汉族 */
    HAN("01", "汉族"),

    /* 蒙古族 */
    MONGOL("02", "蒙古族"),

    /* 回族 */
    HUI("03", "回族"),

    /* 藏族 */
    TIBETAN("04", "藏族"),

    /* 维吾尔族 */
    UYGHUR("05", "维吾尔族"),

    /* 苗族 */
    MIAO("06", "苗族"),

    /* 彝族 */
    YI("07", "彝族"),

    /* 壮族 */
    ZHUANG("08", "壮族"),

    /* 布依族 */
    BUYEI("09", "布依族"),

    /* 朝鲜族 */
    KOREAN("10", "朝鲜族"),

    /* 满族 */
    MANCHU("11", "满族"),

    /* 侗族 */
    DONG("12", "侗族"),

    /* 瑶族 */
    YAO("13", "瑶族"),

    /* 白族 */
    BAI("14", "白族"),

    /* 土家族 */
    TUJIA("15", "土家族"),

    /* 哈尼族 */
    HANI("16", "哈尼族"),

    /* 哈萨克族 */
    KAZAK("17", "哈萨克族"),

    /* 傣族 */
    DAI("18", "傣族"),

    /* 黎族 */
    LI("19", "黎族"),

    /* 傈僳族 */
    LISU("20", "傈僳族"),

    /* 佤族 */
    VA("21", "佤族"),

    /* 畲族 */
    SHE("22", "畲族"),

    /* 高山族 */
    GAOSHAN("23", "高山族"),

    /* 拉祜族 */
    LAHU("24", "拉祜族"),

    /* 水族 */
    SUI("25", "水族"),

    /* 东乡族 */
    DONGXIANG("26", "东乡族"),

    /* 纳西族 */
    NAXI("27", "纳西族"),

    /* 景颇族 */
    JINGPO("28", "景颇族"),

    /* 柯尔克孜族 */
    KIRGIZ("29", "柯尔克孜族"),

    /* 土族 */
    TU("30", "土族"),

    /* 达斡尔族 */
    DAUR("31", "达斡尔族"),

    /* 仫佬族 */
    MULAO("32", "仫佬族"),

    /* 羌族 */
    QIANG("33", "羌族"),

    /* 布朗族 */
    BLANG("34", "布朗族"),

    /* 撒拉族 */
    SALAR("35", "撒拉族"),

    /* 毛南族 */
    MAONAN("36", "毛南族"),

    /* 仡佬族 */
    GELAO("37", "仡佬族"),

    /* 锡伯族 */
    XIBE("38", "锡伯族"),

    /* 阿昌族 */
    ACHANG("39", "阿昌族"),

    /* 普米族 */
    PUMI("40", "普米族"),

    /* 塔吉克族 */
    TAJIK("41", "塔吉克族"),

    /* 怒族 */
    NU("42", "怒族"),

    /* 乌孜别克族 */
    UZBEK("43", "乌孜别克族"),

    /* 俄罗斯族 */
    RUSSIANS("44", "俄罗斯族"),

    /* 鄂温克族 */
    EWENKI("45", "鄂温克族"),

    /* 德昂族 */
    DEANG("46", "德昂族"),

    /* 保安族 */
    BONAN("47", "保安族"),

    /* 裕固族 */
    YUGUR("48", "裕固族"),

    /* 京族 */
    GIN("49", "京族"),

    /* 塔塔尔族 */
    TATAR("50", "塔塔尔族"),

    /* 独龙族 */
    DERUNG("51", "独龙族"),

    /* 鄂伦春族 */
    OROQEN("52", "鄂伦春族"),

    /* 赫哲族 */
    HEZHEN("53", "赫哲族"),

    /* 门巴族 */
    MONBA("54", "门巴族"),

    /* 珞巴族 */
    LHOBA("55", "珞巴族"),

    /* 基诺族 */
    JINO("56", "基诺族"),

    /* 其他 */
    OTHER("57", "其他"),

    /* 外国血统中国籍人士 */
    FOREIGN_COUNTRY("58", "外国血统中国籍人士");

    private final String code;
    private final String desc;

    NationalityEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static String getCodeByDesc(String desc) {
        if (CommonUtil.isBlank(desc)) {
            return null;
        }
        NationalityEnum[] nationalityEnums = values();
        for (NationalityEnum nationalityEnum : nationalityEnums) {
            if (nationalityEnum.getDesc().contains(desc)) {
                return nationalityEnum.getCode();
            }
        }
        return null;
    }

    public static String getDescByCode(String code) {
        if (CommonUtil.isBlank(code)) {
            return null;
        }
        NationalityEnum[] nationalityEnums = values();
        for (NationalityEnum nationalityEnum : nationalityEnums) {
            if (nationalityEnum.getCode().contains(code)) {
                return nationalityEnum.getDesc();
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

