package cn.jdevelops.environment.entity;


import java.util.Objects;

/**
 * 经纬度纠偏工具类 - bean
 *
 * @author zhaowei
 * @version V1.1.0
 * @date 2020/6/30 0:07
 */
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
	private Double x;
	/**
	 * Y坐标
	 */
	private Double y;


	public double getX() {
		if(x == null){
			return 0L;
		}
		return x;
	}

	public double getY() {
		if(y == null){
			return 0L;
		}
		return y;
	}

	public MapPoint() {
	}

	public MapPoint(double latitude, double longitude, Double x, Double y) {
		this.latitude = latitude;
		this.longitude = longitude;
		this.x = x;
		this.y = y;
	}

	@Override
	public String toString() {
		return "MapPoint{" +
				"latitude=" + latitude +
				", longitude=" + longitude +
				", x=" + x +
				", y=" + y +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		MapPoint mapPoint = (MapPoint) o;
		return Double.compare(mapPoint.latitude, latitude) == 0 && Double.compare(mapPoint.longitude, longitude) == 0 && Objects.equals(x, mapPoint.x) && Objects.equals(y, mapPoint.y);
	}

	@Override
	public int hashCode() {
		return Objects.hash(latitude, longitude, x, y);
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public void setX(Double x) {
		this.x = x;
	}

	public void setY(Double y) {
		this.y = y;
	}
}
