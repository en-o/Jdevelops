package cn.tannn.jdevelops.result.domin.column;


/**
 * 锅炉信息表
 *
 * @author tnnn
 * @version V1.0
 * @date 2022-6-23
 */

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


	public BoilerEntity() {
	}

	public BoilerEntity(String companyNo, String no, String name, String model, String startTime, Double designHeatingRate, Double normalHeatingRate, String heatingRateUnit, Double hoursWorkedPerDay, Double daysWorkedPerWeek, Double daysWorkedPerYear, Double monthlyFuelUsageJan, Double monthlyFuelUsageFeb, Double monthlyFuelUsageMar, Double monthlyFuelUsageApr, Double monthlyFuelUsageMay, Double monthlyFuelUsageJun, Double monthlyFuelUsageJul, Double monthlyFuelUsageAug, Double monthlyFuelUsageSep, Double monthlyFuelUsageOct, Double monthlyFuelUsageNov, Double monthlyFuelUsageDec, Double annualFuelUsage, String fuelUsageUnit, String processCategory) {
		this.companyNo = companyNo;
		this.no = no;
		this.name = name;
		this.model = model;
		this.startTime = startTime;
		this.designHeatingRate = designHeatingRate;
		this.normalHeatingRate = normalHeatingRate;
		this.heatingRateUnit = heatingRateUnit;
		this.hoursWorkedPerDay = hoursWorkedPerDay;
		this.daysWorkedPerWeek = daysWorkedPerWeek;
		this.daysWorkedPerYear = daysWorkedPerYear;
		this.monthlyFuelUsageJan = monthlyFuelUsageJan;
		this.monthlyFuelUsageFeb = monthlyFuelUsageFeb;
		this.monthlyFuelUsageMar = monthlyFuelUsageMar;
		this.monthlyFuelUsageApr = monthlyFuelUsageApr;
		this.monthlyFuelUsageMay = monthlyFuelUsageMay;
		this.monthlyFuelUsageJun = monthlyFuelUsageJun;
		this.monthlyFuelUsageJul = monthlyFuelUsageJul;
		this.monthlyFuelUsageAug = monthlyFuelUsageAug;
		this.monthlyFuelUsageSep = monthlyFuelUsageSep;
		this.monthlyFuelUsageOct = monthlyFuelUsageOct;
		this.monthlyFuelUsageNov = monthlyFuelUsageNov;
		this.monthlyFuelUsageDec = monthlyFuelUsageDec;
		this.annualFuelUsage = annualFuelUsage;
		this.fuelUsageUnit = fuelUsageUnit;
		this.processCategory = processCategory;
	}

	@Override
	public String toString() {
		return "BoilerEntity{" +
				"companyNo='" + companyNo + '\'' +
				", no='" + no + '\'' +
				", name='" + name + '\'' +
				", model='" + model + '\'' +
				", startTime='" + startTime + '\'' +
				", designHeatingRate=" + designHeatingRate +
				", normalHeatingRate=" + normalHeatingRate +
				", heatingRateUnit='" + heatingRateUnit + '\'' +
				", hoursWorkedPerDay=" + hoursWorkedPerDay +
				", daysWorkedPerWeek=" + daysWorkedPerWeek +
				", daysWorkedPerYear=" + daysWorkedPerYear +
				", monthlyFuelUsageJan=" + monthlyFuelUsageJan +
				", monthlyFuelUsageFeb=" + monthlyFuelUsageFeb +
				", monthlyFuelUsageMar=" + monthlyFuelUsageMar +
				", monthlyFuelUsageApr=" + monthlyFuelUsageApr +
				", monthlyFuelUsageMay=" + monthlyFuelUsageMay +
				", monthlyFuelUsageJun=" + monthlyFuelUsageJun +
				", monthlyFuelUsageJul=" + monthlyFuelUsageJul +
				", monthlyFuelUsageAug=" + monthlyFuelUsageAug +
				", monthlyFuelUsageSep=" + monthlyFuelUsageSep +
				", monthlyFuelUsageOct=" + monthlyFuelUsageOct +
				", monthlyFuelUsageNov=" + monthlyFuelUsageNov +
				", monthlyFuelUsageDec=" + monthlyFuelUsageDec +
				", annualFuelUsage=" + annualFuelUsage +
				", fuelUsageUnit='" + fuelUsageUnit + '\'' +
				", processCategory='" + processCategory + '\'' +
				'}';
	}

	public String getCompanyNo() {
		return companyNo;
	}

	public void setCompanyNo(String companyNo) {
		this.companyNo = companyNo;
	}

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public Double getDesignHeatingRate() {
		return designHeatingRate;
	}

	public void setDesignHeatingRate(Double designHeatingRate) {
		this.designHeatingRate = designHeatingRate;
	}

	public Double getNormalHeatingRate() {
		return normalHeatingRate;
	}

	public void setNormalHeatingRate(Double normalHeatingRate) {
		this.normalHeatingRate = normalHeatingRate;
	}

	public String getHeatingRateUnit() {
		return heatingRateUnit;
	}

	public void setHeatingRateUnit(String heatingRateUnit) {
		this.heatingRateUnit = heatingRateUnit;
	}

	public Double getHoursWorkedPerDay() {
		return hoursWorkedPerDay;
	}

	public void setHoursWorkedPerDay(Double hoursWorkedPerDay) {
		this.hoursWorkedPerDay = hoursWorkedPerDay;
	}

	public Double getDaysWorkedPerWeek() {
		return daysWorkedPerWeek;
	}

	public void setDaysWorkedPerWeek(Double daysWorkedPerWeek) {
		this.daysWorkedPerWeek = daysWorkedPerWeek;
	}

	public Double getDaysWorkedPerYear() {
		return daysWorkedPerYear;
	}

	public void setDaysWorkedPerYear(Double daysWorkedPerYear) {
		this.daysWorkedPerYear = daysWorkedPerYear;
	}

	public Double getMonthlyFuelUsageJan() {
		return monthlyFuelUsageJan;
	}

	public void setMonthlyFuelUsageJan(Double monthlyFuelUsageJan) {
		this.monthlyFuelUsageJan = monthlyFuelUsageJan;
	}

	public Double getMonthlyFuelUsageFeb() {
		return monthlyFuelUsageFeb;
	}

	public void setMonthlyFuelUsageFeb(Double monthlyFuelUsageFeb) {
		this.monthlyFuelUsageFeb = monthlyFuelUsageFeb;
	}

	public Double getMonthlyFuelUsageMar() {
		return monthlyFuelUsageMar;
	}

	public void setMonthlyFuelUsageMar(Double monthlyFuelUsageMar) {
		this.monthlyFuelUsageMar = monthlyFuelUsageMar;
	}

	public Double getMonthlyFuelUsageApr() {
		return monthlyFuelUsageApr;
	}

	public void setMonthlyFuelUsageApr(Double monthlyFuelUsageApr) {
		this.monthlyFuelUsageApr = monthlyFuelUsageApr;
	}

	public Double getMonthlyFuelUsageMay() {
		return monthlyFuelUsageMay;
	}

	public void setMonthlyFuelUsageMay(Double monthlyFuelUsageMay) {
		this.monthlyFuelUsageMay = monthlyFuelUsageMay;
	}

	public Double getMonthlyFuelUsageJun() {
		return monthlyFuelUsageJun;
	}

	public void setMonthlyFuelUsageJun(Double monthlyFuelUsageJun) {
		this.monthlyFuelUsageJun = monthlyFuelUsageJun;
	}

	public Double getMonthlyFuelUsageJul() {
		return monthlyFuelUsageJul;
	}

	public void setMonthlyFuelUsageJul(Double monthlyFuelUsageJul) {
		this.monthlyFuelUsageJul = monthlyFuelUsageJul;
	}

	public Double getMonthlyFuelUsageAug() {
		return monthlyFuelUsageAug;
	}

	public void setMonthlyFuelUsageAug(Double monthlyFuelUsageAug) {
		this.monthlyFuelUsageAug = monthlyFuelUsageAug;
	}

	public Double getMonthlyFuelUsageSep() {
		return monthlyFuelUsageSep;
	}

	public void setMonthlyFuelUsageSep(Double monthlyFuelUsageSep) {
		this.monthlyFuelUsageSep = monthlyFuelUsageSep;
	}

	public Double getMonthlyFuelUsageOct() {
		return monthlyFuelUsageOct;
	}

	public void setMonthlyFuelUsageOct(Double monthlyFuelUsageOct) {
		this.monthlyFuelUsageOct = monthlyFuelUsageOct;
	}

	public Double getMonthlyFuelUsageNov() {
		return monthlyFuelUsageNov;
	}

	public void setMonthlyFuelUsageNov(Double monthlyFuelUsageNov) {
		this.monthlyFuelUsageNov = monthlyFuelUsageNov;
	}

	public Double getMonthlyFuelUsageDec() {
		return monthlyFuelUsageDec;
	}

	public void setMonthlyFuelUsageDec(Double monthlyFuelUsageDec) {
		this.monthlyFuelUsageDec = monthlyFuelUsageDec;
	}

	public Double getAnnualFuelUsage() {
		return annualFuelUsage;
	}

	public void setAnnualFuelUsage(Double annualFuelUsage) {
		this.annualFuelUsage = annualFuelUsage;
	}

	public String getFuelUsageUnit() {
		return fuelUsageUnit;
	}

	public void setFuelUsageUnit(String fuelUsageUnit) {
		this.fuelUsageUnit = fuelUsageUnit;
	}

	public String getProcessCategory() {
		return processCategory;
	}

	public void setProcessCategory(String processCategory) {
		this.processCategory = processCategory;
	}
}
