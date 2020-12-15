package com.deabes.environment.entity;

import lombok.Data;

/**
 *  经纬度纠偏工具类 - bean
 * @ClassName: MapPoint
 * @author  zhaowei
 * @date 2020/6/30 0:07
 * @version  V1.1.0
 */
@Data
public class MapPoint
{
    private double latitude;

    private double longitude;

    private double x;

    private double y;
}
