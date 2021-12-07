package cn.jdevelops.constant.unit;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 单位表
 *
 * @author tn
 * @date 2020-09-18 09:21
 */
@ApiModel(value = "单位表", description = "单位表")
public interface HjUnit {

    @ApiModelProperty(value = "温度")
    String TEMPERATURE_UNIT = "℃";

    @ApiModelProperty(value = "湿度")
    String HUMIDITY_UNIT = "%RH";

    @ApiModelProperty(value = "风向角度")
    String WDIRECTANGLE_UNIT = "°";

    @ApiModelProperty(value = "风速")
    String WSPEED_UNIT = "m/s";

    @ApiModelProperty(value = "大气压")
    String APRESS_UNIT = "hPa";

    @ApiModelProperty(value = "VOC（挥发性有机化合物）体积浓度")
    String VOC_UNIT = "ppb";

    @ApiModelProperty(value = "VOC（挥发性有机化合物）质量浓度")
    String VOC2_UNIT = "mg/m³";

    @ApiModelProperty(value = "VOC告警标准值")
    String STANDARD_UNIT = "mg/m³";

    @ApiModelProperty(value = "CO2(二氧化碳浓度)")
    String CO2_UNIT = "ppm";

    @ApiModelProperty(value = "PM2.5(细颗粒物)")
    String PM2_5_UNIT = "μg/m³";

    @ApiModelProperty(value = "o2(氧气)")
    String O2_UNIT = "vol%";

    @ApiModelProperty(value = "TVOC(总挥发性有机化合物)")
    String TVOC_UNIT = "mg/m³";

    @ApiModelProperty(value = "烟雾")
    String SMOG_UNIT = "ppm";

    @ApiModelProperty(value = "CH20(甲醛)")
    String CH20_UNIT = "ppm";

    @ApiModelProperty(value = "CO(一氧化碳)")
    String CO_UNIT = "ppm";

    @ApiModelProperty(value = "CH4(甲烷)")
    String CH4_UNIT = "%LEL";

    @ApiModelProperty(value = "PM10(可吸入颗粒物)")
    String PM10_UNIT = "μg/m³";

    @ApiModelProperty(value = "功率因数")
    String PF_UNIT = "VA";

    @ApiModelProperty(value = "有功功率")
    String P_UNIT = "KWh";

    @ApiModelProperty(value = "无功功率")
    String Q_UNIT = "Kvarh";

    @ApiModelProperty(value = "电压")
    String V_UNIT = "V";

    @ApiModelProperty(value = "功率")
    String W_UNIT = "W";

    @ApiModelProperty(value = "电流")
    String A_UNIT = "A";

    @ApiModelProperty(value = "频率")
    String HZ_UNIT = "Hz";

}
