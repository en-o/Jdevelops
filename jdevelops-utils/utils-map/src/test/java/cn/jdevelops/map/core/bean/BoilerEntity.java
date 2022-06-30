package cn.jdevelops.map.core.bean;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 锅炉信息表
 *
 * @author tnnn
 * @version V1.0
 * @date 2022-6-23
 */

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class BoilerEntity extends CommonBean<BoilerEntity> {


	/**
	 * 企业编号
	 */
	private String companyNo;


	/**
	 * 锅炉编号;用两个字母GL（代表锅炉）和一个流水号组成锅炉编号，每一设备一个编号。
	 */
	private String no;


	/**
	 * 锅炉名称
	 */
	private String name;


	/**
	 * 锅炉型号
	 */
	private String model;


	/**
	 * 开始使用时间
	 */
	private String startTime;

	/**
	 * 供热率设计值
	 */
	private Double designHeatingRate;


	/**
	 * 供热率正常值
	 */
	private Double normalHeatingRate;


	/**
	 * 供热率单位
	 */
	private String heatingRateUnit;


	/**
	 * 每天正常工作小时数
	 */
	private Double hoursWorkedPerDay;


	/**
	 * 每星期正常工作天数
	 */
	private Double daysWorkedPerWeek;


	/**
	 * 每年正常工作天数
	 */
	private Double daysWorkedPerYear;


	/**
	 * 每月实际燃料消耗量1
	 */
	private Double monthlyFuelUsageJan;


	/**
	 * 每月实际燃料消耗量2
	 */
	private Double monthlyFuelUsageFeb;


	/**
	 * 每月实际燃料消耗量3
	 */
	private Double monthlyFuelUsageMar;


	/**
	 * 每月实际燃料消耗量4
	 */
	private Double monthlyFuelUsageApr;


	/**
	 * 每月实际燃料消耗量5
	 */
	private Double monthlyFuelUsageMay;


	/**
	 * 每月实际燃料消耗量6
	 */
	private Double monthlyFuelUsageJun;


	/**
	 * 每月实际燃料消耗量7
	 */
	private Double monthlyFuelUsageJul;


	/**
	 * 每月实际燃料消耗量8
	 */
	private Double monthlyFuelUsageAug;


	/**
	 * 每月实际燃料消耗量9
	 */
	private Double monthlyFuelUsageSep;


	/**
	 * 每月实际燃料消耗量10
	 */
	private Double monthlyFuelUsageOct;


	/**
	 * 每月实际燃料消耗量11
	 */
	private Double monthlyFuelUsageNov;


	/**
	 * 每月实际燃料消耗量12
	 */
	private Double monthlyFuelUsageDec;


	/**
	 * 年实际燃料消耗量
	 */
	private Double annualFuelUsage;


	/**
	 * 燃料消耗量单位
	 */
	private String fuelUsageUnit;


	/**
	 * 工艺类别
	 */
	private String processCategory;

}
