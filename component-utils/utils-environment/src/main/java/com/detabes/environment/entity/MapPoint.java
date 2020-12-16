package com.detabes.environment.entity;

import lombok.Data;

/**
 * 经纬度纠偏工具类 - bean
 *
 * @author zhaowei
 * @version V1.1.0
 * @ClassName: MapPoint
 * @date 2020/6/30 0:07
 */
@Data
public class MapPoint {
	/**
	 * 纬度
	 */
	private double latitude;
	/**
	 * 经度
	 */
	private double longitude;
	/**
	 * X坐标
	 */
	private double x;
	/**
	 * Y坐标
	 */
	private double y;
}
