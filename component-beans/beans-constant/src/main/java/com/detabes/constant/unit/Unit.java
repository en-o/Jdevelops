package com.detabes.constant.unit;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author tn
 * @ClassName UnitType
 * @description 单位表
 * @date 2020-09-18 09:21
 */
@ApiModel(value = "单位表", description = "单位表")
public interface Unit {

    @ApiModelProperty(value = "温度")
    public static String TEMPERATURE_UNIT = "℃";

    @ApiModelProperty(value = "湿度")
    public static String HUMIDITY_UNIT = "%RH";

    @ApiModelProperty(value = "风向角度")
    public static  String WDIRECTANGLE_UNIT = "°";

    @ApiModelProperty(value = "风速")
    public static  String WSPEED_UNIT = "m/s";

    @ApiModelProperty(value = "大气压")
    public static  String APRESS_UNIT = "hPa";

    @ApiModelProperty(value = "VOC（挥发性有机化合物）体积浓度")
    public static  String VOC_UNIT = "ppb";

    @ApiModelProperty(value = "VOC（挥发性有机化合物）质量浓度")
    public static  String VOC2_UNIT = "mg/m³";

    @ApiModelProperty(value = "VOC告警标准值")
    public static  String STANDARD_UNIT = "mg/m³";

    @ApiModelProperty(value = "CO2(二氧化碳浓度)")
    public static  String CO2_UNIT = "ppm";

    @ApiModelProperty(value = "PM2.5(细颗粒物)")
    public static  String PM2_5_UNIT = "μg/m³";

    @ApiModelProperty(value = "o2(氧气)")
    public static  String O2_UNIT = "vol%";

    @ApiModelProperty(value = "TVOC(总挥发性有机化合物)")
    public static  String TVOC_UNIT = "mg/m³";

    @ApiModelProperty(value = "烟雾")
    public static  String SMOG_UNIT = "ppm";

    @ApiModelProperty(value = "CH20(甲醛)")
    public static  String CH20_UNIT = "ppm";

    @ApiModelProperty(value = "CO(一氧化碳)")
    public static  String CO_UNIT = "ppm";

    @ApiModelProperty(value = "CH4(甲烷)")
    public static  String CH4_UNIT = "%LEL";

    @ApiModelProperty(value = "PM10(可吸入颗粒物)")
    public static  String PM10_UNIT = "μg/m³";

    @ApiModelProperty(value = "功率因数")
    public static  String PF_UNIT = "VA";

    @ApiModelProperty(value = "有功功率")
    public static  String P_UNIT = "KWh";

    @ApiModelProperty(value = "无功功率")
    public static  String Q_UNIT = "Kvarh";

    @ApiModelProperty(value = "电压")
    public static  String V_UNIT = "V";

    @ApiModelProperty(value = "功率")
    public static  String W_UNIT = "W";

    @ApiModelProperty(value = "电流")
    public static  String A_UNIT = "A";

    @ApiModelProperty(value = "频率")
    public static  String Hz_UNIT = "Hz";

}
